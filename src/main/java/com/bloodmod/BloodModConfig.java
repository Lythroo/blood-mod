package com.bloodmod;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Config(name = BloodMod.MOD_ID)
public class BloodModConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public GeneralSettings general = new GeneralSettings();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("particles")
    public ParticleSettings particles = new ParticleSettings();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Category("particles")
    public AudioSettings audio = new AudioSettings();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("entities")
    public EntityOverrides entities = new EntityOverrides();

    // ══════════════════════════════════════════════════════════════════════
    // GENERAL SETTINGS
    // ══════════════════════════════════════════════════════════════════════

    public static class GeneralSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean modEnabled = true;

        @ConfigEntry.Gui.Tooltip
        public boolean playerBleed = true;
    }

    // ══════════════════════════════════════════════════════════════════════
    // PARTICLE SETTINGS
    // ══════════════════════════════════════════════════════════════════════

    public static class ParticleSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean hitBurst = true;

        @ConfigEntry.Gui.Tooltip
        public boolean deathBurst = true;

        @ConfigEntry.Gui.Tooltip
        public boolean lowHealthDrip = true;

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.BoundedDiscrete(min = 5, max = 100)
        public int lowHealthThreshold = 50;

        @ConfigEntry.Gui.Tooltip(count = 1)
        @ConfigEntry.BoundedDiscrete(min = 50, max = 300)
        public int particleSize = 210;

        public float getThresholdAsFloat() {
            return lowHealthThreshold / 100.0f;
        }

        public float getParticleSizeMultiplier() {
            return particleSize / 100.0f;
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // AUDIO SETTINGS
    // ══════════════════════════════════════════════════════════════════════

    public static class AudioSettings {
        @ConfigEntry.Gui.Tooltip
        public boolean soundEnabled = true;
    }

    // ══════════════════════════════════════════════════════════════════════
    // BACKWARDS COMPATIBILITY ACCESSORS
    // ══════════════════════════════════════════════════════════════════════

    public boolean globalEnabled() { return general.modEnabled; }
    public boolean playerBleed() { return general.playerBleed; }
    public boolean hitBurstEnabled() { return particles.hitBurst; }
    public boolean deathBurstEnabled() { return particles.deathBurst; }
    public boolean lowHealthEnabled() { return particles.lowHealthDrip; }
    public float lowHealthThreshold() { return particles.getThresholdAsFloat(); }
    public boolean soundEnabled() { return audio.soundEnabled; }
    public float particleSizeMultiplier() { return particles.getParticleSizeMultiplier(); }

    // ══════════════════════════════════════════════════════════════════════
    // ENTITY REGISTRY MAPPING (EXCLUDED FROM GUI)
    // ══════════════════════════════════════════════════════════════════════

    @ConfigEntry.Gui.Excluded
    private static final Map<String, String> REGISTRY_TO_FIELD = Map.ofEntries(
            // ── Undead ────────────────────────────────────────────────────
            Map.entry("skeleton",            "skeleton"),
            Map.entry("wither_skeleton",     "witherSkeleton"),
            Map.entry("stray",               "stray"),
            Map.entry("bogged",              "bogged"),
            Map.entry("parched",             "parched"),
            Map.entry("zombie",              "zombie"),
            Map.entry("zombie_villager",     "zombieVillager"),
            Map.entry("husk",                "husk"),
            Map.entry("drowned",             "drowned"),
            Map.entry("phantom",             "phantom"),
            Map.entry("skeleton_horse",      "skeletonHorse"),
            Map.entry("zombie_horse",        "zombieHorse"),
            Map.entry("zombified_piglin",    "zombifiedPiglin"),

            // ── Bosses & Special ──────────────────────────────────────────
            Map.entry("wither",              "wither"),
            Map.entry("ender_dragon",        "enderDragon"),
            Map.entry("enderman",            "enderman"),
            Map.entry("endermite",           "endermite"),
            Map.entry("shulker",             "shulker"),
            Map.entry("warden",              "warden"),

            // ── Non-Organic ───────────────────────────────────────────────
            Map.entry("slime",               "slime"),
            Map.entry("magma_cube",          "magmaCube"),
            Map.entry("blaze",               "blaze"),
            Map.entry("breeze",              "breeze"),
            Map.entry("ghast",               "ghast"),
            Map.entry("happy_ghast",         "happyGhast"),
            Map.entry("vex",                 "vex"),
            Map.entry("allay",               "allay"),
            Map.entry("iron_golem",          "ironGolem"),
            Map.entry("snow_golem",          "snowGolem"),
            Map.entry("copper_golem",        "copperGolem"),
            Map.entry("creaking",            "creaking"),
            Map.entry("creeper",             "creeper"),
            Map.entry("silverfish",          "silverfish"),

            // ── Passive Animals ───────────────────────────────────────────
            Map.entry("cow",                 "cow"),
            Map.entry("mooshroom",           "mooshroom"),
            Map.entry("chicken",             "chicken"),
            Map.entry("pig",                 "pig"),
            Map.entry("sheep",               "sheep"),
            Map.entry("horse",               "horse"),
            Map.entry("donkey",              "donkey"),
            Map.entry("mule",                "mule"),
            Map.entry("llama",               "llama"),
            Map.entry("trader_llama",        "traderLlama"),
            Map.entry("camel",               "camel"),
            Map.entry("rabbit",              "rabbit"),
            Map.entry("turtle",              "turtle"),
            Map.entry("armadillo",           "armadillo"),
            Map.entry("sniffer",             "sniffer"),

            // ── Tameable & Friendly ───────────────────────────────────────
            Map.entry("wolf",                "wolf"),
            Map.entry("cat",                 "cat"),
            Map.entry("ocelot",              "ocelot"),
            Map.entry("fox",                 "fox"),
            Map.entry("parrot",              "parrot"),

            // ── Aquatic ───────────────────────────────────────────────────
            Map.entry("dolphin",             "dolphin"),
            Map.entry("squid",               "squid"),
            Map.entry("glow_squid",          "glowSquid"),
            Map.entry("cod",                 "cod"),
            Map.entry("salmon",              "salmon"),
            Map.entry("tropical_fish",       "tropicalFish"),
            Map.entry("pufferfish",          "pufferfish"),
            Map.entry("axolotl",             "axolotl"),
            Map.entry("tadpole",             "tadpole"),
            Map.entry("frog",                "frog"),
            Map.entry("guardian",            "guardian"),
            Map.entry("elder_guardian",      "elderGuardian"),

            // ── Neutral & Hostile ─────────────────────────────────────────
            Map.entry("bee",                 "bee"),
            Map.entry("panda",               "panda"),
            Map.entry("polar_bear",          "polarBear"),
            Map.entry("goat",                "goat"),
            Map.entry("spider",              "spider"),
            Map.entry("cave_spider",         "caveSpider"),

            // ── Villagers & Illagers ──────────────────────────────────────
            Map.entry("villager",            "villager"),
            Map.entry("wandering_trader",    "wanderingTrader"),
            Map.entry("witch",               "witch"),
            Map.entry("evoker",              "evoker"),
            Map.entry("vindicator",          "vindicator"),
            Map.entry("pillager",            "pillager"),
            Map.entry("ravager",             "ravager"),
            Map.entry("illusioner",          "illusioner"),

            // ── Nether ────────────────────────────────────────────────────
            Map.entry("strider",             "strider"),
            Map.entry("hoglin",              "hoglin"),
            Map.entry("zoglin",              "zoglin"),
            Map.entry("piglin",              "piglin"),
            Map.entry("piglin_brute",        "piglinBrute")
    );

    /**
     * Entities that should NOT drip blood when at low health.
     * These are entities that don't have traditional circulatory systems:
     * - Skeletons (no flesh/blood)
     * - Golems (constructs)
     * - Slimes/Magma Cubes (gelatinous/molten)
     * - Blazes (fire elementals)
     * - Ghasts (floating gas bags)
     * - Vex/Allay (spirits)
     * - End creatures (teleporting entities)
     * - Phantoms (undead flying creatures)
     * - Breeze (wind elementals)
     * - Wither (undead boss)
     * - Silverfish (bugs, too small)
     */
    @ConfigEntry.Gui.Excluded
    private static final Set<String> NO_DRIP_ENTITIES;

    static {
        NO_DRIP_ENTITIES = new HashSet<>(Arrays.asList(
                // Skeletons - no flesh
                "skeleton",
                "wither_skeleton",
                "stray",
                "bogged",
                "skeleton_horse",
                "parched",  // Dried/mummified mob

                // Constructs/Golems - no blood
                "iron_golem",
                "snow_golem",
                "copper_golem",
                "creaking",  // Wooden construct

                // Slimes - gelatinous
                "slime",
                "magma_cube",

                // Fire/Elemental creatures
                "blaze",
                "breeze",

                // Spirits/Spectral
                "vex",
                "allay",
                "phantom",
                "ghast",
                "happy_ghast",

                // End creatures
                "shulker",

                // Undead bosses
                "wither",
                "warden",

                // Bugs (too small to realistically drip)
                "silverfish"
        ));
    }

    public boolean doesEntityBleed(String registryPath) {
        String fieldName = REGISTRY_TO_FIELD.get(registryPath);
        if (fieldName == null) {
            return true; // unknown entity → bleed by default
        }
        try {
            return (boolean) EntityOverrides.class.getField(fieldName).get(entities);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true;
        }
    }

    /**
     * Checks if an entity should drip blood at low health.
     * Some entities (skeletons, golems, slimes, etc.) don't have circulatory systems
     * so they shouldn't continuously drip blood when wounded.
     *
     * They can still emit blood on hit/death (instantaneous damage), but not passive dripping.
     */
    public boolean shouldEntityDripAtLowHealth(String registryPath) {
        // First check if entity bleeds at all
        if (!doesEntityBleed(registryPath)) {
            return false;
        }

        // Then check if it's in the no-drip list
        return !NO_DRIP_ENTITIES.contains(registryPath);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ENTITY OVERRIDES
    // ══════════════════════════════════════════════════════════════════════

    public static class EntityOverrides {
        // ── Undead ────────────────────────────────────────────────────────
        @ConfigEntry.Category("undead")
        public boolean skeleton           = true;

        @ConfigEntry.Category("undead")
        public boolean witherSkeleton     = true;

        @ConfigEntry.Category("undead")
        public boolean stray              = true;

        @ConfigEntry.Category("undead")
        public boolean bogged             = true;

        @ConfigEntry.Category("undead")
        public boolean parched            = true;

        @ConfigEntry.Category("undead")
        public boolean zombie             = true;

        @ConfigEntry.Category("undead")
        public boolean zombieVillager     = true;

        @ConfigEntry.Category("undead")
        public boolean husk               = true;

        @ConfigEntry.Category("undead")
        public boolean drowned            = true;

        @ConfigEntry.Category("undead")
        public boolean phantom            = true;

        @ConfigEntry.Category("undead")
        public boolean skeletonHorse      = true;

        @ConfigEntry.Category("undead")
        public boolean zombieHorse        = true;

        @ConfigEntry.Category("undead")
        public boolean zombifiedPiglin    = true;

        // ── Bosses & Special ──────────────────────────────────────────────
        @ConfigEntry.Category("bosses")
        public boolean wither             = true;

        @ConfigEntry.Category("bosses")
        public boolean enderDragon        = true;

        @ConfigEntry.Category("bosses")
        public boolean warden             = true;

        @ConfigEntry.Category("bosses")
        public boolean enderman           = true;

        @ConfigEntry.Category("bosses")
        public boolean endermite          = true;

        @ConfigEntry.Category("bosses")
        public boolean shulker            = true;

        // ── Non-Organic ───────────────────────────────────────────────────
        @ConfigEntry.Category("nonorganic")
        public boolean slime              = true;

        @ConfigEntry.Category("nonorganic")
        public boolean magmaCube          = true;

        @ConfigEntry.Category("nonorganic")
        public boolean blaze              = true;

        @ConfigEntry.Category("nonorganic")
        public boolean breeze             = true;

        @ConfigEntry.Category("nonorganic")
        public boolean ghast              = true;

        @ConfigEntry.Category("nonorganic")
        public boolean happyGhast         = true;

        @ConfigEntry.Category("nonorganic")
        public boolean vex                = true;

        @ConfigEntry.Category("nonorganic")
        public boolean allay              = true;

        @ConfigEntry.Category("nonorganic")
        public boolean ironGolem          = true;

        @ConfigEntry.Category("nonorganic")
        public boolean snowGolem          = true;

        @ConfigEntry.Category("nonorganic")
        public boolean copperGolem        = true;

        @ConfigEntry.Category("nonorganic")
        public boolean creaking           = true;

        @ConfigEntry.Category("nonorganic")
        public boolean creeper            = true;

        @ConfigEntry.Category("nonorganic")
        public boolean silverfish         = true;

        // ── Passive Animals ───────────────────────────────────────────────
        @ConfigEntry.Category("passive")
        public boolean cow                = true;

        @ConfigEntry.Category("passive")
        public boolean mooshroom          = true;

        @ConfigEntry.Category("passive")
        public boolean chicken            = true;

        @ConfigEntry.Category("passive")
        public boolean pig                = true;

        @ConfigEntry.Category("passive")
        public boolean sheep              = true;

        @ConfigEntry.Category("passive")
        public boolean horse              = true;

        @ConfigEntry.Category("passive")
        public boolean donkey             = true;

        @ConfigEntry.Category("passive")
        public boolean mule               = true;

        @ConfigEntry.Category("passive")
        public boolean llama              = true;

        @ConfigEntry.Category("passive")
        public boolean traderLlama        = true;

        @ConfigEntry.Category("passive")
        public boolean camel              = true;

        @ConfigEntry.Category("passive")
        public boolean rabbit             = true;

        @ConfigEntry.Category("passive")
        public boolean turtle             = true;

        @ConfigEntry.Category("passive")
        public boolean armadillo          = true;

        @ConfigEntry.Category("passive")
        public boolean sniffer            = true;

        // ── Tameable & Friendly ───────────────────────────────────────────
        @ConfigEntry.Category("tameable")
        public boolean wolf               = true;

        @ConfigEntry.Category("tameable")
        public boolean cat                = true;

        @ConfigEntry.Category("tameable")
        public boolean ocelot             = true;

        @ConfigEntry.Category("tameable")
        public boolean fox                = true;

        @ConfigEntry.Category("tameable")
        public boolean parrot             = true;

        // ── Aquatic ───────────────────────────────────────────────────────
        @ConfigEntry.Category("aquatic")
        public boolean dolphin            = true;

        @ConfigEntry.Category("aquatic")
        public boolean squid              = true;

        @ConfigEntry.Category("aquatic")
        public boolean glowSquid          = true;

        @ConfigEntry.Category("aquatic")
        public boolean cod                = true;

        @ConfigEntry.Category("aquatic")
        public boolean salmon             = true;

        @ConfigEntry.Category("aquatic")
        public boolean tropicalFish       = true;

        @ConfigEntry.Category("aquatic")
        public boolean pufferfish         = true;

        @ConfigEntry.Category("aquatic")
        public boolean axolotl            = true;

        @ConfigEntry.Category("aquatic")
        public boolean tadpole            = true;

        @ConfigEntry.Category("aquatic")
        public boolean frog               = true;

        @ConfigEntry.Category("aquatic")
        public boolean guardian           = true;

        @ConfigEntry.Category("aquatic")
        public boolean elderGuardian      = true;

        // ── Neutral & Hostile ─────────────────────────────────────────────
        @ConfigEntry.Category("neutral")
        public boolean bee                = true;

        @ConfigEntry.Category("neutral")
        public boolean panda              = true;

        @ConfigEntry.Category("neutral")
        public boolean polarBear          = true;

        @ConfigEntry.Category("neutral")
        public boolean goat               = true;

        @ConfigEntry.Category("neutral")
        public boolean spider             = true;

        @ConfigEntry.Category("neutral")
        public boolean caveSpider         = true;

        // ── Villagers & Illagers ──────────────────────────────────────────
        @ConfigEntry.Category("villagers")
        public boolean villager           = true;

        @ConfigEntry.Category("villagers")
        public boolean wanderingTrader    = true;

        @ConfigEntry.Category("villagers")
        public boolean witch              = true;

        @ConfigEntry.Category("villagers")
        public boolean evoker             = true;

        @ConfigEntry.Category("villagers")
        public boolean vindicator         = true;

        @ConfigEntry.Category("villagers")
        public boolean pillager           = true;

        @ConfigEntry.Category("villagers")
        public boolean ravager            = true;

        @ConfigEntry.Category("villagers")
        public boolean illusioner         = true;

        // ── Nether ────────────────────────────────────────────────────────
        @ConfigEntry.Category("nether")
        public boolean strider            = true;

        @ConfigEntry.Category("nether")
        public boolean hoglin             = true;

        @ConfigEntry.Category("nether")
        public boolean zoglin             = true;

        @ConfigEntry.Category("nether")
        public boolean piglin             = true;

        @ConfigEntry.Category("nether")
        public boolean piglinBrute        = true;
    }
}