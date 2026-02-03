package com.bloodmod.particle;

import com.bloodmod.BloodModClient;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

public class BloodParticle extends BillboardParticle {

    // ---------------------------------------------------------------
    // Tuning Constants
    // ---------------------------------------------------------------
    private static final float GRAVITY   = 0.04f;   // blocks / tick²
    private static final float DRAG      = 0.98f;   // per-tick velocity multiplier
    private static final int   BASE_LIFE = 40;      // ticks (2 s)
    private static final int   JITTER    = 10;      // ± ticks

    // Base particle size range (will be multiplied by config)
    private static final float MIN_SCALE = 0.08f;
    private static final float MAX_SCALE = 0.16f;

    // Color variation for deep red blood
    private static final float RED_BASE   = 0.55f;
    private static final float RED_RANGE  = 0.10f;
    private static final float GREEN_BASE = 0.03f;
    private static final float GREEN_RANGE = 0.05f;
    private static final float BLUE_BASE  = 0.03f;
    private static final float BLUE_RANGE = 0.05f;

    // ---------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------
    protected BloodParticle(ClientWorld world,
                            double x, double y, double z,
                            double velX, double velY, double velZ,
                            Sprite sprite,
                            float sizeMultiplier) {
        super(world, x, y, z, velX, velY, velZ, sprite);

        // Jittered lifetime so a burst doesn't vanish in one frame
        this.maxAge = BASE_LIFE + (int)(world.random.nextFloat() * JITTER * 2) - JITTER;

        // Deep red with slight per-particle variation
        this.setColor(
                RED_BASE + world.random.nextFloat() * RED_RANGE,
                GREEN_BASE + world.random.nextFloat() * GREEN_RANGE,
                BLUE_BASE + world.random.nextFloat() * BLUE_RANGE
        );

        this.alpha = 1.0f;

        // Particle size with config multiplier
        float baseScale = MIN_SCALE + world.random.nextFloat() * (MAX_SCALE - MIN_SCALE);
        this.scale = baseScale * sizeMultiplier;
    }

    // ---------------------------------------------------------------
    // Tick
    // ---------------------------------------------------------------
    @Override
    public void tick() {
        super.tick(); // position += velocity, age++

        velocityY -= GRAVITY; // gravity

        velocityX *= DRAG; // air drag on every axis
        velocityY *= DRAG;
        velocityZ *= DRAG;

        // Fade out over the last 25% of lifetime
        float lifeFraction = 1.0f - (float) age / maxAge;
        if (lifeFraction < 0.25f) {
            this.alpha = lifeFraction / 0.25f;
        }
    }

    // ---------------------------------------------------------------
    // Render type
    // ---------------------------------------------------------------
    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_TRANSLUCENT;
    }

    // ---------------------------------------------------------------
    // Factory
    // ---------------------------------------------------------------
    /**
     * Registered via ParticleFactoryRegistry.getInstance().register().
     *
     * Uses SpriteProvider to randomly select from all available textures
     * defined in the particle JSON file, creating visual variety.
     */
    public static class Factory implements ParticleFactory<SimpleParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        // 1.21.11: createParticle has a Random as the 9th (last) parameter
        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientWorld world,
                                       double x, double y, double z,
                                       double velX, double velY, double velZ,
                                       Random random) {
            // Get particle size multiplier from config
            float sizeMultiplier = BloodModClient.getConfig().particleSizeMultiplier();

            // Use random to select from available textures instead of always using index 0
            Sprite sprite = this.spriteProvider.getSprite(random);
            return new BloodParticle(world, x, y, z, velX, velY, velZ, sprite, sizeMultiplier);
        }
    }
}