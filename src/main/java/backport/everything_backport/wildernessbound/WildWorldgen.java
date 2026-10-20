package backport.everything_backport.wildernessbound;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.wildernessbound.worldgen.PoplarFoliagePlacer;
import backport.everything_backport.wildernessbound.worldgen.PoplarTrunkPlacer;
import backport.everything_backport.wildernessbound.worldgen.ShelfMushroomDecorator;
import java.util.Optional;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.Nullable;

/**
 * 26.3's dappled forest, poplar trees and the worldgen types they use.
 *
 * <p>The server runs TerraTonic, so the overworld's biome source is never replaced. 26.3 puts the dappled forest in one cell of its
 * overworld biome table: cold, dry and in the variant half of weirdness, where 1.21.7 places plains. A mixin on whatever multi-noise
 * overworld source is in use turns plains picked in that climate into dappled forest ({@link #isDappledForest}); biomes a datapack
 * like Terralith picks there are left alone.
 */
public final class WildWorldgen {
	public static final ResourceKey<Biome> DAPPLED_FOREST = ResourceKey.create(Registries.BIOME, EverythingBackport.vanilla("dappled_forest"));
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_POPLAR = feature("red_poplar");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_POPLAR = feature("orange_poplar");
	public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_POPLAR = feature("yellow_poplar");

	// 26.3's OverworldBiomeBuilder: MIDDLE_BIOMES_VARIANT[1][0], temperature index 1, humidity index 0, non-negative weirdness.
	private static final Climate.Parameter TEMPERATURE = Climate.Parameter.span(-0.45F, -0.15F);
	private static final Climate.Parameter HUMIDITY = Climate.Parameter.span(-1.0F, -0.35F);
	private static final Climate.Parameter WEIRDNESS = Climate.Parameter.span(0.0F, 1.0F);

	public static final TrunkPlacerType<PoplarTrunkPlacer> POPLAR_TRUNK_PLACER = Registry.register(
		BuiltInRegistries.TRUNK_PLACER_TYPE, EverythingBackport.vanilla("poplar_trunk_placer"), new TrunkPlacerType<>(PoplarTrunkPlacer.CODEC)
	);
	public static final FoliagePlacerType<PoplarFoliagePlacer> POPLAR_FOLIAGE_PLACER = Registry.register(
		BuiltInRegistries.FOLIAGE_PLACER_TYPE, EverythingBackport.vanilla("poplar_foliage_placer"), new FoliagePlacerType<>(PoplarFoliagePlacer.CODEC)
	);
	public static final TreeDecoratorType<ShelfMushroomDecorator> SHELF_MUSHROOM_DECORATOR = Registry.register(
		BuiltInRegistries.TREE_DECORATOR_TYPE, EverythingBackport.vanilla("shelf_mushroom"), new TreeDecoratorType<>(ShelfMushroomDecorator.CODEC)
	);

	private static volatile @Nullable Registry<Biome> biomes;

	private WildWorldgen() {
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> feature(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, EverythingBackport.vanilla(name));
	}

	static void init() {
		// The biome registry of the world being loaded, so biome sources can find the dappled forest holder.
		DynamicRegistrySetupCallback.EVENT.register(view -> view.getOptional(Registries.BIOME).ifPresent(registry -> biomes = registry));
	}

	public static Optional<Holder.Reference<Biome>> dappledForest() {
		Registry<Biome> registry = biomes;
		return registry == null ? Optional.empty() : registry.get(DAPPLED_FOREST);
	}

	/** Whether 26.3 would place dappled forest where the source picked this biome. */
	public static boolean isDappledForest(Climate.TargetPoint point, Holder<Biome> picked) {
		return picked.is(Biomes.PLAINS)
			&& TEMPERATURE.distance(point.temperature()) == 0L
			&& HUMIDITY.distance(point.humidity()) == 0L
			&& WEIRDNESS.distance(point.weirdness()) == 0L;
	}
}
