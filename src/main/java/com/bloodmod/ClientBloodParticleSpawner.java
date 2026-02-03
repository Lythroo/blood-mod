package com.bloodmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ClientBloodParticleSpawner {

    /**
     * One-shot death burst. Spawns a large number of drips and splashes
     * centered on the entity at the moment it dies. Counts and spread
     * scale with the entity's width so bigger mobs bleed more.
     */
    public static void spawnBloodOnDeath(ClientWorld world, LivingEntity entity) {
        // Don't spawn blood particles in water
        if (isEntityInWater(entity)) {
            return;
        }

        double posX = entity.getX();
        double posY = entity.getY() + entity.getHeight() * 0.5;
        double posZ = entity.getZ();

        // Scale everything by entity width so a cow bleeds more than a chicken
        float sizeFactor = entity.getWidth(); // chicken ~0.6, cow ~0.9, etc.

        int dripCount   = (int)(30 * sizeFactor); // ~18 for chicken, ~27 for cow
        int splashCount = (int)(25 * sizeFactor); // ~15 for chicken, ~22 for cow

        // Play blood drip sound
        playBloodSound(world, posX, posY, posZ, sizeFactor);

        // --- drips: spread across the full hitbox, fall fast ---
        for (int i = 0; i < dripCount; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * entity.getWidth() * 1.8;
            double offsetY = (world.random.nextDouble()) * entity.getHeight() * 0.8;
            double offsetZ = (world.random.nextDouble() - 0.5) * entity.getWidth() * 1.8;

            double velX = (world.random.nextDouble() - 0.5) * 0.4;
            double velY = -0.5 - world.random.nextDouble() * 1.5;
            double velZ = (world.random.nextDouble() - 0.5) * 0.4;

            MinecraftClient.getInstance().particleManager.addParticle(
                    BloodParticles.BLOOD_DRIP,
                    posX + offsetX, posY + offsetY, posZ + offsetZ,
                    velX, velY, velZ
            );
        }

        // --- splashes: radial burst outward from the center ---
        for (int i = 0; i < splashCount; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double radius = 0.2 + world.random.nextDouble() * entity.getWidth() * 1.2;

            double offsetX = Math.cos(angle) * radius;
            double offsetY = (world.random.nextDouble() - 0.3) * entity.getHeight() * 0.6;
            double offsetZ = Math.sin(angle) * radius;

            // Velocity points outward in the same direction as the offset
            double speed = 0.3 + world.random.nextDouble() * 0.4;
            double velX = Math.cos(angle) * speed;
            double velY = -0.2 - world.random.nextDouble() * 0.6;
            double velZ = Math.sin(angle) * speed;

            MinecraftClient.getInstance().particleManager.addParticle(
                    BloodParticles.BLOOD_SPLASH,
                    posX + offsetX, posY + offsetY, posZ + offsetZ,
                    velX, velY, velZ
            );
        }
    }

    /**
     * Tick-based low-health drip/splash — called from client tick event.
     *
     * @param config the config instance to read the threshold from
     */
    public static void spawnBloodForLowHealth(ClientWorld world, LivingEntity entity, BloodModConfig config) {
        // Don't spawn blood particles in water
        if (isEntityInWater(entity)) {
            return;
        }

        float healthPercent = entity.getHealth() / entity.getMaxHealth();
        float threshold = config.lowHealthThreshold();

        if (healthPercent > threshold) {
            return;
        }

        // Derive inner tiers relative to the configured threshold
        float frequentTier = threshold * 0.5f; // default: 0.25 (5 hearts)
        float splashTier   = threshold * 0.3f; // default: 0.15 (3 hearts)

        // Better visibility: 1/10 chance for low health, 1/6 for very low
        int chance = healthPercent < frequentTier ? 6 : 10;

        if (world.random.nextInt(chance) == 0) {
            double posX = entity.getX();
            double posY = entity.getY() + entity.getHeight() * 0.6;
            double posZ = entity.getZ();

            // Play occasional drip sound (1 in 5 chance when particles spawn)
            if (world.random.nextInt(5) == 0) {
                playBloodSound(world, posX, posY, posZ, 0.3f);
            }

            // Spawn 2-3 drip particles for better visibility
            int dripCount = healthPercent < frequentTier ? 3 : 2;

            for (int i = 0; i < dripCount; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * entity.getWidth() * 0.8;
                double offsetY = (world.random.nextDouble() - 0.5) * 0.2;
                double offsetZ = (world.random.nextDouble() - 0.5) * entity.getWidth() * 0.8;

                double velX = (world.random.nextDouble() - 0.5) * 0.1;
                double velY = -1.5 - world.random.nextDouble() * 0.5;
                double velZ = (world.random.nextDouble() - 0.5) * 0.1;

                MinecraftClient.getInstance().particleManager.addParticle(
                        BloodParticles.BLOOD_DRIP,
                        posX + offsetX,
                        posY + offsetY,
                        posZ + offsetZ,
                        velX, velY, velZ
                );
            }

            // Add splash particles when critically low
            if (healthPercent < splashTier) {
                int splashCount = world.random.nextInt(2) + 1; // 1-2 splashes

                for (int i = 0; i < splashCount; i++) {
                    double offsetX = (world.random.nextDouble() - 0.5) * entity.getWidth() * 0.6;
                    double offsetZ = (world.random.nextDouble() - 0.5) * entity.getWidth() * 0.6;

                    double velX = (world.random.nextDouble() - 0.5) * 0.15;
                    double velZ = (world.random.nextDouble() - 0.5) * 0.15;

                    MinecraftClient.getInstance().particleManager.addParticle(
                            BloodParticles.BLOOD_SPLASH,
                            posX + offsetX,
                            posY - 0.1,
                            posZ + offsetZ,
                            velX,
                            -1.2 - world.random.nextDouble() * 0.4,
                            velZ
                    );
                }
            }
        }
    }

    /**
     * Checks if an entity is in water.
     */
    private static boolean isEntityInWater(LivingEntity entity) {
        return entity.isSubmergedInWater() || entity.isTouchingWater();
    }

    /**
     * Plays a blood drip/splash sound effect.
     * Uses vanilla water drip sound with modified pitch for blood effect.
     * Sound is played at the specified position for directional audio.
     *
     * @param sizeFactor scales volume based on entity size
     */
    private static void playBloodSound(ClientWorld world, double x, double y, double z, float sizeFactor) {
        // Check if sound is enabled in config
        if (!com.bloodmod.BloodModClient.getConfig().soundEnabled()) {
            return;
        }

        float volume = Math.min(0.3f + sizeFactor * 0.2f, 1.0f);
        float pitch = 0.8f + world.random.nextFloat() * 0.3f; // 0.8 - 1.1

        // Play positional sound using sound manager
        MinecraftClient.getInstance().getSoundManager().play(
                new net.minecraft.client.sound.PositionedSoundInstance(
                        SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA,
                        SoundCategory.PLAYERS,
                        volume,
                        pitch,
                        net.minecraft.util.math.random.Random.create(),
                        x, y, z
                )
        );
    }
}