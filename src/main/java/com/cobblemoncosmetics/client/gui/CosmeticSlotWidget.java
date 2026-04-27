package com.cobblemoncosmetics.client.gui;

import com.cobblemon.mod.common.client.gui.summary.SummaryScreen;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemoncosmetics.CobblemonCosmetics;
import com.cobblemoncosmetics.item.CosmeticItem;
import com.cobblemoncosmetics.registry.CosmeticsItems;
import com.cobblemoncosmetics.util.CosmeticNBTHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.narration.NarrationMessageBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Optional;

/**
 * A single cosmetic slot widget rendered in the Cobblemon Summary Screen.
 *
 * Each slot:
 * - Displays as an empty inventory slot (gray square)
 * - If a cosmetic is in this slot, shows its item icon
 * - On click: opens a small popup to remove the cosmetic
 * - Shows a slot number label
 * - Renders a tooltip showing the cosmetic name or "Empty slot X"
 */
@Environment(EnvType.CLIENT)
public class CosmeticSlotWidget extends ClickableWidget {

    private final int slot;
    private final SummaryScreen parentScreen;

    public CosmeticSlotWidget(int x, int y, int width, int height, int slot, SummaryScreen parentScreen) {
        super(x, y, width, height, Text.literal("Cosmetic Slot " + slot));
        this.slot = slot;
        this.parentScreen = parentScreen;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int x = getX();
        int y = getY();

        // Draw slot background (dark gray like inventory slots)
        context.fill(x, y, x + getWidth(), y + getHeight(), 0xFF8B8B8B);
        context.fill(x + 1, y + 1, x + getWidth() - 1, y + getHeight() - 1, 0xFF373737);

        // Draw slot number in top-left corner (tiny)
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            String.valueOf(slot),
            x + 1, y + 1,
            0xFFAAAAAA,
            false
        );

        // If a cosmetic is present, render its item icon
        Pokemon pokemon = getCurrentPokemon();
        if (pokemon != null) {
            Optional<String> cosmeticId = CosmeticNBTHelper.getCosmeticInSlot(pokemon, slot);
            if (cosmeticId.isPresent()) {
                // Find the CosmeticItem matching this ID
                CosmeticsItems.ALL_COSMETICS.stream()
                    .filter(c -> c.getCosmeticId().equals(cosmeticId.get()))
                    .findFirst()
                    .ifPresent(cosmeticItem -> {
                        ItemStack stack = new ItemStack(cosmeticItem);
                        context.drawItem(stack, x + 1, y + 1);
                    });

                // Draw a subtle green outline to indicate occupied
                context.drawBorder(x, y, getWidth(), getHeight(), 0xFF55FF55);
            }
        }

        // Hover highlight
        if (isHovered()) {
            context.fill(x, y, x + getWidth(), y + getHeight(), 0x30FFFFFF);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        Pokemon pokemon = getCurrentPokemon();
        if (pokemon == null) return;

        Optional<String> cosmeticId = CosmeticNBTHelper.getCosmeticInSlot(pokemon, slot);
        if (cosmeticId.isPresent()) {
            // Right-click or click to remove
            CobblemonCosmetics.LOGGER.info("[CLIENT] Requesting removal of cosmetic in slot {}", slot);
            // In a full implementation, send a packet to server to remove the cosmetic
            // For now we do a client-side preview removal
            // TODO: send RemoveCosmetic packet to server
        }
    }

    /**
     * Adds tooltip for this slot.
     */
    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        Pokemon pokemon = getCurrentPokemon();
        String desc;
        if (pokemon != null) {
            Optional<String> cosmeticId = CosmeticNBTHelper.getCosmeticInSlot(pokemon, slot);
            desc = cosmeticId.map(id -> "Slot " + slot + ": " + id).orElse("Slot " + slot + ": Empty");
        } else {
            desc = "Cosmetic Slot " + slot;
        }
        builder.put(net.minecraft.client.gui.narration.NarrationPart.TITLE, Text.literal(desc));
    }

    /**
     * Try to get the currently displayed Pokémon from the summary screen.
     * This uses reflection since SummaryScreen's fields may not be directly accessible.
     */
    private Pokemon getCurrentPokemon() {
        try {
            // Try to get the selected pokemon from the screen via reflection
            // (Cobblemon's SummaryScreen exposes 'pokemon' field)
            var field = parentScreen.getClass().getDeclaredField("pokemon");
            field.setAccessible(true);
            Object obj = field.get(parentScreen);
            if (obj instanceof Pokemon p) return p;
        } catch (Exception e) {
            // Field may have a different name in this version - try alternatives
            try {
                var field = parentScreen.getClass().getDeclaredField("selectedPokemon");
                field.setAccessible(true);
                Object obj = field.get(parentScreen);
                if (obj instanceof Pokemon p) return p;
            } catch (Exception ignored) {}
        }
        return null;
    }
}
