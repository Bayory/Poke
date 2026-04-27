package com.cobblemoncosmetics.item;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemoncosmetics.CobblemonCosmetics;
import com.cobblemoncosmetics.util.CosmeticNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

/**
 * CosmeticItem - an item that can apply a cosmetic/skin to a Pokémon.
 *
 * Usage: Right-click on a Pokémon while holding this item.
 * The cosmetic is stored in the Pokémon's NBT data.
 *
 * This item is NOT craftable (no recipe file added).
 * It can only be obtained via /give or the Creative Mode tab.
 */
public class CosmeticItem extends Item {

    /** The cosmetic ID stored in this item. Used to identify the skin. */
    private final String cosmeticId;

    /** Display name shown in tooltip */
    private final String cosmeticDisplayName;

    /** Which cosmetic slot this item occupies (1-6) */
    private final int cosmeticSlot;

    public CosmeticItem(Settings settings, String cosmeticId, String cosmeticDisplayName, int cosmeticSlot) {
        super(settings);
        this.cosmeticId = cosmeticId;
        this.cosmeticDisplayName = cosmeticDisplayName;
        this.cosmeticSlot = cosmeticSlot;
    }

    /**
     * Called when the player right-clicks an entity (Pokémon).
     * Applies the cosmetic to the Pokémon if it is valid.
     */
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, net.minecraft.entity.LivingEntity entity, Hand hand) {
        if (entity instanceof PokemonEntity pokemonEntity) {
            Pokemon pokemon = pokemonEntity.getPokemon();

            if (!player.getWorld().isClient) {
                // Store the cosmetic in the Pokémon's persistent NBT
                NbtCompound nbt = pokemon.getPersistentData();
                String slotKey = CosmeticNBTHelper.getSlotKey(cosmeticSlot);
                nbt.putString(slotKey, cosmeticId);

                // Sync the changes to all clients
                pokemon.markDirty();

                player.sendMessage(
                    Text.translatable("cobblemon_cosmetics.applied", cosmeticDisplayName, pokemon.getSpecies().getName()),
                    true
                );

                CobblemonCosmetics.LOGGER.info(
                    "Applied cosmetic '{}' (slot {}) to {} owned by {}",
                    cosmeticId, cosmeticSlot, pokemon.getSpecies().getName(), player.getName().getString()
                );
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    /**
     * Add tooltip lines showing cosmetic info.
     */
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("cobblemon_cosmetics.tooltip.cosmetic_id", cosmeticId));
        tooltip.add(Text.translatable("cobblemon_cosmetics.tooltip.slot", cosmeticSlot));
        tooltip.add(Text.translatable("cobblemon_cosmetics.tooltip.how_to_use"));
    }

    public String getCosmeticId() {
        return cosmeticId;
    }

    public int getCosmeticSlot() {
        return cosmeticSlot;
    }

    public String getCosmeticDisplayName() {
        return cosmeticDisplayName;
    }
}
