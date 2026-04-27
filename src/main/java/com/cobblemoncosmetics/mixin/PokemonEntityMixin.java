package com.cobblemoncosmetics.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemoncosmetics.network.CosmeticsNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into PokemonEntity to hook into onLoad/tick for cosmetic sync.
 * When a Pokémon entity is loaded or updated, we broadcast its cosmetics
 * to all nearby players so they can render them.
 */
@Mixin(value = PokemonEntity.class, remap = false)
public class PokemonEntityMixin {

    /**
     * When the entity is loaded into the world (e.g. player logs in, chunk loads),
     * broadcast existing cosmetics to nearby players.
     */
    @Inject(method = "onStartedTrackingBy", at = @At("TAIL"))
    private void cobblemon_cosmetics_onTracked(net.minecraft.server.network.ServerPlayerEntity player, CallbackInfo ci) {
        PokemonEntity self = (PokemonEntity) (Object) this;
        if (self.getWorld() instanceof ServerWorld serverWorld) {
            Pokemon pokemon = self.getPokemon();

            // Broadcast all cosmetic slots to the player who just started tracking
            for (int slot = 1; slot <= 6; slot++) {
                String key = "cobblemon_cosmetics_slot_" + slot;
                if (pokemon.getPersistentData().contains(key)) {
                    String cosmeticId = pokemon.getPersistentData().getString(key);
                    if (!cosmeticId.isEmpty()) {
                        ServerPlayNetworking.send(
                            player,
                            new CosmeticsNetworking.ApplyCosmeticPayload(self.getId(), slot, cosmeticId)
                        );
                    }
                }
            }
        }
    }
}
