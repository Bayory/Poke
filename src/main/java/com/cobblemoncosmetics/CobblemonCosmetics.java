package com.cobblemoncosmetics;

import com.cobblemoncosmetics.network.CosmeticsNetworking;
import com.cobblemoncosmetics.registry.CosmeticsItems;
import com.cobblemoncosmetics.registry.CosmeticsItemGroups;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobblemonCosmetics implements ModInitializer {

    public static final String MOD_ID = "cobblemon_cosmetics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Cobblemon Cosmetics initialized!");

        // Register items (not craftable - no recipes added)
        CosmeticsItems.register();

        // Register creative tab
        CosmeticsItemGroups.register();

        // Register network packets
        CosmeticsNetworking.registerServerPackets();
    }
}
