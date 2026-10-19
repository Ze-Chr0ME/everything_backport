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
| The Copper Age | 1.21.9 | planned |
| Mounts of Mayhem | 1.21.11 | planned |
| Tiny Takeover | 26.1 | planned |
| Chaos Cubed | 26.2 | planned |
| Wilderness Bound | 26.3 | planned |

## Design notes

- **Vanilla ids.** Backported blocks, items and entities are registered under the `minecraft` namespace
  with the same ids and block state properties as the real release, so a world can later be opened in the
  newer vanilla version without losing them.
- **World generation.** The target server runs TerraTonic (Terralith + Tectonic). New biomes are added
  alongside the existing noise settings rather than replacing them, and new structures only spawn in newly
  generated chunks.
- **Assets.** Textures, models and sounds come from the vanilla release they belong to and remain the
  property of Mojang Studios.

## Building

```
./gradlew build
```

The mod jar is written to `build/libs/`.
