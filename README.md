# Cobblemon Cosmetics Mod

A Fabric mod for **Cobblemon 1.7.3** on **Minecraft 1.21.1** that adds a cosmetic system for your Pokémon.

## Features

- 🎨 **6 cosmetic slots** per Pokémon (Slot 1–6), visible in the Summary Screen beside the Pokémon model
- 🪄 **Cosmetic items** — right-click any Pokémon to apply a cosmetic skin
- 🚫 **Not craftable** — items can only be obtained via `/give` or Creative Mode
- 📦 **Custom Creative Tab** — all cosmetic items are organized in their own tab
- 🔄 **Multiplayer sync** — cosmetics are saved and synced to all nearby players
- 💾 **Persistent** — cosmetics survive server restarts (stored in Pokémon NBT)

## Installation

1. Install [Fabric Loader 0.16+](https://fabricmc.net/use/installer/)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for 1.21.1
3. Install [Cobblemon 1.7.3](https://cobblemon.com) for 1.21.1 (Fabric)
4. Drop `cobblemon-cosmetics-1.0.0.jar` into your `mods/` folder

## How to Use

### Apply a cosmetic
1. Give yourself a cosmetic item (Creative Mode tab → "Cobblemon Cosmetics", or `/give @s cobblemon_cosmetics:fire_crown`)
2. Right-click on any of your Pokémon while holding the item
3. The cosmetic is applied and saved permanently

### View cosmetics in the Summary Screen
- Open the Pokémon Summary Screen (default: `R` key in Cobblemon)
- Six cosmetic slots appear below the Pokémon model
- Each slot shows which cosmetic is applied

### Remove a cosmetic
- Click the occupied slot in the Summary Screen to remove it

## Adding Your Own Cosmetics

### 1. Register the item
In `CosmeticsItems.java`, add:
```java
public static final CosmeticItem MY_COSMETIC = registerCosmetic(
    "my_cosmetic_id",    // internal ID (lowercase, underscores)
    "My Cosmetic Name",  // display name shown to players
    1                    // slot number (1–6)
);
```

### 2. Add texture
Place your 16×16 (or 32×32) PNG texture at:
```
src/main/resources/assets/cobblemon_cosmetics/textures/item/my_cosmetic_id.png
```

### 3. Add item model
Create `src/main/resources/assets/cobblemon_cosmetics/models/item/my_cosmetic_id.json`:
```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "cobblemon_cosmetics:item/my_cosmetic_id"
  }
}
```

### 4. Add translation
In `src/main/resources/assets/cobblemon_cosmetics/lang/en_us.json`:
```json
"item.cobblemon_cosmetics.my_cosmetic_id": "My Cosmetic Name"
```

### 5. Implement the visual
The cosmetic ID is stored in the Pokémon's NBT as `cobblemon_cosmetics_slot_X`.
In your renderer (or Gecko/custom model), read:
```java
String cosmeticId = pokemon.getPersistentData().getString("cobblemon_cosmetics_slot_1");
```
Then switch the model/texture based on the ID.

## Project Structure

```
src/main/java/com/cobblemoncosmetics/
├── CobblemonCosmetics.java          # Main mod class
├── client/
│   ├── CobblemonCosmeticsClient.java # Client entrypoint
│   └── gui/
│       └── CosmeticSlotWidget.java   # GUI slot in Summary Screen
├── item/
│   └── CosmeticItem.java             # The cosmetic item class
├── mixin/
│   ├── PokemonEntityMixin.java        # Syncs cosmetics when tracked
│   └── PokemonSummaryScreenMixin.java # Injects slots into Summary Screen
├── network/
│   └── CosmeticsNetworking.java       # Server ↔ Client packets
├── registry/
│   ├── CosmeticsItems.java            # All cosmetic items registered here
│   └── CosmeticsItemGroups.java       # Creative tab
└── util/
    └── CosmeticNBTHelper.java         # NBT read/write helpers
```

## Building

```bash
./gradlew build
```

Output jar will be in `build/libs/`.

## License

MIT License
