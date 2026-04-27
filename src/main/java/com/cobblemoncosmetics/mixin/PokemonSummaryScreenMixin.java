package com.cobblemoncosmetics.mixin;

import com.cobblemon.mod.common.client.gui.summary.SummaryScreen;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemoncosmetics.CobblemonCosmetics;
import com.cobblemoncosmetics.client.gui.CosmeticSlotWidget;
import com.cobblemoncosmetics.util.CosmeticNBTHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Mixin into the Cobblemon Summary Screen to add cosmetic slots.
 *
 * Layout:
 * - The Pokémon model rotates in the center-bottom area of the summary screen.
 * - Below the model (where the Pokémon stands) is Slot 0 (cosmetic display slot).
 * - To the left of the riding/mount slot area, we add Slot 1 through Slot 6.
 *
 * Note: This mixin uses the @Environment(CLIENT) because SummaryScreen is client-only.
 */
@Environment(EnvType.CLIENT)
@Mixin(value = SummaryScreen.class, remap = false)
public class PokemonSummaryScreenMixin {

    private final List<CosmeticSlotWidget> cosmeticSlots = new ArrayList<>();

    @Inject(method = "init", at = @At("TAIL"))
    private void cobblemon_cosmetics_addSlots(CallbackInfo ci) {
        SummaryScreen self = (SummaryScreen) (Object) this;

        // Clear existing slots
        cosmeticSlots.clear();

        // Get the screen dimensions
        int screenWidth = self.width;
        int screenHeight = self.height;

        // Position the 6 cosmetic slots on the left side of the screen,
        // near where the mount/riding slot is displayed in Cobblemon's UI.
        //
        // Default Cobblemon summary screen layout:
        //   - Model viewer: roughly centered, lower half
        //   - Stats panel: right side
        //   - Info panel: left side
        //
        // We place our slots just to the left of the model viewer.
        // Slots are 18x18 pixels, arranged in a 2x3 grid.

        int slotSize = 18;
        int padding = 4;

        // Start position: left of model area
        // The model is roughly at x = screenWidth/2 - 60, y = screenHeight - 120
        int startX = screenWidth / 2 - 90;
        int startY = screenHeight - 110;

        for (int i = 0; i < 6; i++) {
            int col = i % 2;     // 2 columns
            int row = i / 2;     // 3 rows
            int x = startX + col * (slotSize + padding);
            int y = startY + row * (slotSize + padding);

            int slot = i + 1;    // slots 1-6

            CosmeticSlotWidget widget = new CosmeticSlotWidget(x, y, slotSize, slotSize, slot, self);
            cosmeticSlots.add(widget);
            self.addDrawableChild(widget);
        }

        CobblemonCosmetics.LOGGER.debug("Added {} cosmetic slots to summary screen.", cosmeticSlots.size());
    }
}
