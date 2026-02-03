package com.bloodmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minimal server-side initialization - only registers particle types.
 * All actual blood spawning logic is client-side.
 */
public class BloodMod implements ModInitializer {
    public static final String MOD_ID = "bloodmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static volatile BloodModConfig config;

    public static BloodModConfig getConfig() {
        if (config == null) {
            // On dedicated servers, just use defaults (particles won't spawn anyway)
            config = new BloodModConfig();
        }
        return config;
    }

    public static void setConfig(BloodModConfig c) {
        config = c;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Simple Blood (Client-Only) - Server registration...");

        // Register particle types (required on both sides for networking)
        BloodParticles.register();

        LOGGER.info("Simple Blood particle types registered!");
    }

    /**
     * Checks the config for whether this entity type should produce blood.
     */
    public static boolean shouldEntityBleed(LivingEntity entity) {
        BloodModConfig cfg = getConfig();
        Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());
        return cfg.doesEntityBleed(id.getPath());
    }
}