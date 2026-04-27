package com.cobblemoncosmetics.registry;

import com.cobblemoncosmetics.CobblemonCosmetics;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Registers a custom Creative Mode tab "Cobblemon Cosmetics".
 * All cosmetic items from CosmeticsItems.ALL_COSMETICS are added here.
 * The tab icon is the first registered cosmetic item.
 */
public class CosmeticsItemGroups {

    public static final ItemGroup COSMETICS_GROUP = FabricItemGroup.builder()
        .icon(() -> CosmeticsItems.ALL_COSMETICS.isEmpty()
            ? ItemStack.EMPTY
            : new ItemStack(CosmeticsItems.ALL_COSMETICS.get(0)))
        .displayName(Text.translatable("itemgroup.cobblemon_cosmetics.cosmetics"))
        .entries((context, entries) -> {
            // Add all cosmetics organized by slot
            for (int slot = 1; slot <= 6; slot++) {
                final int s = slot;
                CosmeticsItems.ALL_COSMETICS.stream()
                    .filter(c -> c.getCosmeticSlot() == s)
                    .forEach(entries::add);
            }
        })
        .build();

    public static void register() {
        Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(CobblemonCosmetics.MOD_ID, "cosmetics"),
            COSMETICS_GROUP
        );
        CobblemonCosmetics.LOGGER.info("Registered Cobblemon Cosmetics creative tab.");
    }
}
