package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mixin.EntityTypeAccessor;
import backport.everything_backport.mixin.SpawnPlacementsAccessor;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import backport.everything_backport.mountsofmayhem.entity.CamelHusk;
import backport.everything_backport.mountsofmayhem.entity.Nautilus;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilus;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilusVariant;
import backport.everything_backport.registry.Reg;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Monster;
import backport.everything_backport.mountsofmayhem.entity.Parched;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

public final class MountsEntities {
	public static final EntityDataSerializer<Holder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT_SERIALIZER = EntityDataSerializer.forValueType(
		ByteBufCodecs.holderRegistry(MountsRegistries.ZOMBIE_NAUTILUS_VARIANT)
	);

	public static final EntityType<Nautilus> NAUTILUS = Reg.entity(
		"nautilus",
		EntityType.Builder.of(Nautilus::new, MobCategory.WATER_CREATURE).sized(0.875F, 0.95F).passengerAttachments(1.1375F).eyeHeight(0.2751F).clientTrackingRange(10)
	);
	public static final EntityType<ZombieNautilus> ZOMBIE_NAUTILUS = Reg.entity(
		"zombie_nautilus",
		EntityType.Builder.of(ZombieNautilus::new, MobCategory.MONSTER).sized(0.875F, 0.95F).passengerAttachments(1.1375F).eyeHeight(0.2751F).clientTrackingRange(10)
	);
	public static final EntityType<CamelHusk> CAMEL_HUSK = Reg.entity(
		"camel_husk", EntityType.Builder.of(CamelHusk::new, MobCategory.MONSTER).sized(1.7F, 2.375F).eyeHeight(2.275F).clientTrackingRange(10)
	);
	public static final EntityType<Parched> PARCHED = Reg.entity(
		"parched",
		EntityType.Builder.of(Parched::new, MobCategory.MONSTER).sized(0.6F, 1.99F).eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8).notInPeaceful()
	);

	private MountsEntities() {
	}

	static void init() {
		FabricTrackedDataRegistry.register(EverythingBackport.id("zombie_nautilus_variant"), ZOMBIE_NAUTILUS_VARIANT_SERIALIZER);
		FabricDefaultAttributeRegistry.register(NAUTILUS, AbstractNautilus.createAttributes());
		FabricDefaultAttributeRegistry.register(ZOMBIE_NAUTILUS, ZombieNautilus.createAttributes());
		FabricDefaultAttributeRegistry.register(CAMEL_HUSK, Camel.createAttributes());
		FabricDefaultAttributeRegistry.register(PARCHED, Parched.createAttributes());

		SpawnPlacements.register(NAUTILUS, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractNautilus::checkNautilusSpawnRules);
		SpawnPlacements.register(ZOMBIE_NAUTILUS, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractNautilus::checkNautilusSpawnRules);
		SpawnPlacements.register(CAMEL_HUSK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
		SpawnPlacements.register(PARCHED, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
		// 1.21.11: husks no longer need the sky to spawn, and zombie horses spawn like monsters.
		SpawnPlacementsAccessor.everythingBackport$getDataByType().remove(EntityType.HUSK);
		SpawnPlacements.register(EntityType.HUSK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
		SpawnPlacementsAccessor.everythingBackport$getDataByType().remove(EntityType.ZOMBIE_HORSE);
		SpawnPlacements.register(EntityType.ZOMBIE_HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
		((EntityTypeAccessor)EntityType.ZOMBIE_HORSE).everythingBackport$setCategory(MobCategory.MONSTER);

		addSpawns();
	}

	/** 1.21.11's spawn changes, matching the vanilla biome files. Zombie nautiluses and camel husks only spawn as mounts. */
	private static void addSpawns() {
		BiomeModifications.addSpawn(
			BiomeSelectors.includeByKey(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.WARM_OCEAN),
			MobCategory.WATER_CREATURE,
			NAUTILUS,
			5,
			1,
			1
		);
		BiomeModifications.addSpawn(
			BiomeSelectors.includeByKey(Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN), MobCategory.WATER_CREATURE, NAUTILUS, 2, 1, 1
		);
		BiomeModifications.create(EverythingBackport.id("mounts_of_mayhem_spawns"))
			.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.DESERT), context -> {
				context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.SKELETON);
				context.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4), 50);
				context.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(PARCHED, 4, 4), 50);
			})
			.add(
				ModificationPhase.REPLACEMENTS,
				BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA),
				context -> {
					context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.ZOMBIE);
					context.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4), 90);
					context.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_HORSE, 1, 1), 5);
				}
			);
	}
}
