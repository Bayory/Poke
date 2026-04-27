package com.cobblemoncosmetics.network;

import com.cobblemoncosmetics.CobblemonCosmetics;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Handles networking between server and clients for cosmetic sync.
 *
 * Packet: ApplyCosmeticPayload
 * Direction: Server -> Client (to sync cosmetic visual on all clients)
 */
public class CosmeticsNetworking {

    /**
     * Packet sent from server to all clients when a cosmetic is applied.
     * Contains: pokemonEntityId, slot, cosmeticId
     */
    public record ApplyCosmeticPayload(
        int pokemonEntityId,
        int slot,
        String cosmeticId
    ) implements CustomPayload {

        public static final Id<ApplyCosmeticPayload> ID = new Id<>(
            Identifier.of(CobblemonCosmetics.MOD_ID, "apply_cosmetic")
        );

        public static final PacketCodec<PacketByteBuf, ApplyCosmeticPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeInt(value.pokemonEntityId());
                buf.writeInt(value.slot());
                buf.writeString(value.cosmeticId());
            },
            buf -> new ApplyCosmeticPayload(
                buf.readInt(),
                buf.readInt(),
                buf.readString()
            )
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Packet sent from server to all clients when a cosmetic is removed.
     */
    public record RemoveCosmeticPayload(
        int pokemonEntityId,
        int slot
    ) implements CustomPayload {

        public static final Id<RemoveCosmeticPayload> ID = new Id<>(
            Identifier.of(CobblemonCosmetics.MOD_ID, "remove_cosmetic")
        );

        public static final PacketCodec<PacketByteBuf, RemoveCosmeticPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeInt(value.pokemonEntityId());
                buf.writeInt(value.slot());
            },
            buf -> new RemoveCosmeticPayload(
                buf.readInt(),
                buf.readInt()
            )
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void registerServerPackets() {
        // Register payload types for sending server -> client
        PayloadTypeRegistry.playS2C().register(ApplyCosmeticPayload.ID, ApplyCosmeticPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RemoveCosmeticPayload.ID, RemoveCosmeticPayload.CODEC);

        CobblemonCosmetics.LOGGER.info("Registered cosmetics networking packets.");
    }
}
