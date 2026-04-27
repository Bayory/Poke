package com.cobblemoncosmetics.registry;

import com.cobblemoncosmetics.CobblemonCosmetics;
import com.cobblemoncosmetics.item.CosmeticItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for all cosmetic items.
 *
 * =====================================================================
 * HOW TO ADD YOUR OWN COSMETICS:
 * =====================================================================
 * 1. Call registerCosmetic("your_cosmetic_id", "Display Name", slotNumber)
 *    - cosmeticId: internal ID (lowercase, underscores), e.g. "fire_crown"
 *    - displayName: shown to players, e.g. "Fire Crown"
 *    - slot: 1 to 6
 *
 * 2. Add a texture at:
 *    src/main/resources/assets/cobblemon_cosmetics/textures/item/your_cosmetic_id.png
 *
 * 3. Add a model at:
 *    src/main/resources/assets/cobblemon_cosmetics/models/item/your_cosmetic_id.json
 *
 * 4. Add translation in en_us.json:
 *    "item.cobblemon_cosmetics.your_cosmetic_id": "Display Name"
 *
 * Items registered here are NOT craftable - no recipe files are added.
 * They can only be obtained via /give or Creative Mode.
 * =====================================================================
 */
public class CosmeticsItems {

    public static final List<CosmeticItem> ALL_COSMETICS = new ArrayList<>();

    // =====================================================================
    // ADD YOUR COSMETICS HERE
    // =====================================================================

    // Slot 1 cosmetics - example: hats / headwear
    public static final CosmeticItem FIRE_CROWN = registerCosmetic(
        "fire_crown", "Fire Crown", 1
    );

    public static final CosmeticItem WATER_CROWN = registerCosmetic(
        "water_crown", "Water Crown", 1
    );

    // Slot 2 cosmetics - example: back decorations
    public static final CosmeticItem SPARKLE_WINGS = registerCosmetic(
        "sparkle_wings", "Sparkle Wings", 2
    );

    // Slot 3 cosmetics - example: body markings
    public static final CosmeticItem GOLDEN_STRIPES = registerCosmetic(
        "golden_stripes", "Golden Stripes", 3
    );

    // Slot 4 cosmetics
    public static final CosmeticItem RAINBOW_AURA = registerCosmetic(
        "rainbow_aura", "Rainbow Aura", 4
    );

    // Slot 5 cosmetics
    public static final CosmeticItem SHADOW_CAPE = registerCosmetic(
        "shadow_cape", "Shadow Cape", 5
    );

    // Slot 6 cosmetics
    public static final CosmeticItem STAR_TAIL = registerCosmetic(
        "star_tail", "Star Tail", 6
    );

    // =====================================================================
    // END OF COSMETICS LIST
    // =====================================================================

    /**
     * Registers a cosmetic item. Settings are intentionally minimal:
     * maxCount(1) so it's not stackable, no food, no durability.
     */
    private static CosmeticItem registerCosmetic(String id, String displayName, int slot) {
        Item.Settings settings = new Item.Settings().maxCount(1);
        CosmeticItem item = new CosmeticItem(settings, id, displayName, slot);
        Registry.register(Registries.ITEM, Identifier.of(CobblemonCosmetics.MOD_ID, id), item);
        ALL_COSMETICS.add(item);
        return item;
    }

    public static void register() {
        CobblemonCosmetics.LOGGER.info("Registered {} cosmetic items.", ALL_COSMETICS.size());
    }
}
