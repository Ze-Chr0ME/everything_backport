# Everything Backport

Fabric mod that backports the content of every Minecraft release after 1.21.7 to **Minecraft 1.21.7**.

Built against the same toolchain as `fabric_skills_mod` so both run in one modpack:

| | |
|---|---|
| Minecraft | 1.21.7 |
| Fabric Loader | 0.18.1 |
| Fabric API | 0.129.0+1.21.7 |
| Java | 21 |

This is a **client + server** mod: every player needs it installed.

## Scope

| Update | Version | Status |
|---|---|---|
| The Copper Age | 1.21.9 | done (no mannequin yet) |
| Mounts of Mayhem | 1.21.11 | done |
| Tiny Takeover | 26.1 | done |
| Chaos Cubed | 26.2 | done (bed and sign rendering changes not ported) |
| Wilderness Bound | 26.3 | planned |

## Design notes

- **Vanilla ids.** Backported blocks, items and entities are registered under the `minecraft` namespace
  with the same ids and block state properties as the real release, so a world can later be opened in the
  newer vanilla version without losing them.
- **World generation.** The target server runs TerraTonic (Terralith + Tectonic). New biomes are added
  alongside the existing noise settings rather than replacing them, and new structures only spawn in newly
  generated chunks.
- **Newer formats.** Where a 1.21.11 data or asset format can't be read by 1.21.7, the ported file is adapted:
  equipment layers 1.21.7 doesn't know (nautilus armor and saddles, the camel husk saddle) are rendered by the
  mod from `textures/entity/equipment/<layer>/`, and the Lunge enchantment uses two small helpers
  (`everything_backport:entity_flags`, `everything_backport:play_level_sound`) in place of fields 1.21.7 lacks.
- **Sulfur caves.** 26.2 places sulfur caves in the overworld climate space. The mod keeps the world's own biome source and
  noise settings (vanilla's, or Terralith's under TerraTonic): where the climate matches 26.2's sulfur caves entry, the biome
  the source picked gives way to sulfur caves unless it is another underground biome (lush caves, dripstone caves, deep dark
  or anything tagged `c:is_underground`), and the sulfur and cinnabar surface rule runs in front of the world's own rule.
- **Physics attributes.** 26.2's `bounciness`, `friction_modifier` and `air_drag_modifier` attributes are added to every
  living entity; entities without a bounciness keep 1.21.7's collision handling.
- **Babies.** 26.1's redesigned baby models replace the scaled-down adults 1.21.7 bakes for its baby model layers.
  As in 26.1, baby zombies and piglins only show armor made of vanilla materials (it uses the `humanoid_baby`
  textures) and never show trims.
- **Assets.** Textures, models and sounds come from the vanilla release they belong to and remain the
  property of Mojang Studios.

## Building

```
./gradlew build
```

The mod jar is written to `build/libs/`.

## Testing

```
./gradlew runClientGameTest
```

Starts a client, builds scenes with the backported content, runs checks for each update (the copper golem sorting items,
the age lock, sulfur cube archetypes, geysers, sulfur caves in a generated world, and more), and saves screenshots
to `build/run/clientGameTest/screenshots`.
