package com.cobblemoncosmetics.client;

import com.cobblemoncosmetics.CobblemonCosmetics;
import com.cobblemoncosmetics.network.CosmeticsNetworking;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class CobblemonCosmeticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CobblemonCosmetics.LOGGER.info("Cobblemon Cosmetics client initialized!");

        // Listen for ApplyCosmeticPayload from server
        ClientPlayNetworking.registerGlobalReceiver(
            CosmeticsNetworking.ApplyCosmeticPayload.ID,
            (payload, context) -> {
                context.client().execute(() -> {
                    if (context.client().world == null) return;

                    Entity entity = context.client().world.getEntityById(payload.pokemonEntityId());
                    if (entity instanceof PokemonEntity pokemonEntity) {
                        // Store cosmetic in client-side NBT for rendering
                        String slotKey = "cobblemon_cosmetics_slot_" + payload.slot();
                        pokemonEntity.getPokemon().getPersistentData()
                            .putString(slotKey, payload.cosmeticId());

                        CobblemonCosmetics.LOGGER.info(
                            "[CLIENT] Applied cosmetic '{}' slot {} to entity {}",
                            payload.cosmeticId(), payload.slot(), payload.pokemonEntityId()
                        );
                    }
                });
            }
        );

        // Listen for RemoveCosmeticPayload from server
        ClientPlayNetworking.registerGlobalReceiver(
            CosmeticsNetworking.RemoveCosmeticPayload.ID,
            (payload, context) -> {
                context.client().execute(() -> {
                    if (context.client().world == null) return;

                    Entity entity = context.client().world.getEntityById(payload.pokemonEntityId());
                    if (entity instanceof PokemonEntity pokemonEntity) {
                        String slotKey = "cobblemon_cosmetics_slot_" + payload.slot();
                        pokemonEntity.getPokemon().getPersistentData().remove(slotKey);
                    }
                });
            }
        );
    }
}
