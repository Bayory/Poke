package com.cobblemoncosmetics.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Helper class for reading and writing cosmetic data
 * in a Pokémon's persistent NBT storage.
 *
 * NBT structure stored inside pokemon.getPersistentData():
 * {
 *   "cobblemon_cosmetics_slot_1": "fire_crown",
 *   "cobblemon_cosmetics_slot_2": "sparkle_wings",
 *   ...
 * }
 */
public class CosmeticNBTHelper {

    private static final String KEY_PREFIX = "cobblemon_cosmetics_slot_";
    public static final int MAX_SLOTS = 6;

    /**
     * Returns the NBT key for a given slot number.
     * @param slot 1-6
     */
    public static String getSlotKey(int slot) {
        return KEY_PREFIX + slot;
    }

    /**
     * Gets the cosmetic ID in a specific slot for a Pokémon.
     * @return the cosmetic ID string, or empty if no cosmetic in that slot
     */
    public static Optional<String> getCosmeticInSlot(Pokemon pokemon, int slot) {
        NbtCompound nbt = pokemon.getPersistentData();
        String key = getSlotKey(slot);
        if (nbt.contains(key)) {
            String value = nbt.getString(key);
            if (!value.isEmpty()) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns all cosmetics applied to a Pokémon as a map of slot -> cosmeticId.
     */
    public static Map<Integer, String> getAllCosmetics(Pokemon pokemon) {
        Map<Integer, String> result = new HashMap<>();
        NbtCompound nbt = pokemon.getPersistentData();
        for (int slot = 1; slot <= MAX_SLOTS; slot++) {
            String key = getSlotKey(slot);
            if (nbt.contains(key)) {
                String value = nbt.getString(key);
                if (!value.isEmpty()) {
                    result.put(slot, value);
                }
            }
        }
        return result;
    }

    /**
     * Removes a cosmetic from a specific slot.
     */
    public static void removeCosmeticInSlot(Pokemon pokemon, int slot) {
        pokemon.getPersistentData().remove(getSlotKey(slot));
        pokemon.markDirty();
    }

    /**
     * Removes all cosmetics from a Pokémon.
     */
    public static void clearAllCosmetics(Pokemon pokemon) {
        NbtCompound nbt = pokemon.getPersistentData();
        for (int slot = 1; slot <= MAX_SLOTS; slot++) {
            nbt.remove(getSlotKey(slot));
        }
        pokemon.markDirty();
    }

    /**
     * Checks if a Pokémon has any cosmetics applied.
     */
    public static boolean hasAnyCosmetic(Pokemon pokemon) {
        return !getAllCosmetics(pokemon).isEmpty();
    }
}
