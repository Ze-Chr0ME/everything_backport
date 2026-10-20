package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.chaoscubed.worldgen.CompositeFeatureConfiguration;
import backport.everything_backport.chaoscubed.worldgen.NoiseThreshold3dConditionSource;
import backport.everything_backport.chaoscubed.worldgen.SequenceFeature;
import backport.everything_backport.chaoscubed.worldgen.SpeleothemClusterConfiguration;
import backport.everything_backport.chaoscubed.worldgen.SpeleothemClusterFeature;
import backport.everything_backport.chaoscubed.worldgen.SpeleothemConfiguration;
import backport.everything_backport.chaoscubed.worldgen.SpeleothemFeature;
import backport.everything_backport.chaoscubed.worldgen.TemplateFeature;
import backport.everything_backport.chaoscubed.worldgen.TemplateFeatureConfiguration;
import backport.everything_backport.chaoscubed.worldgen.TrapezoidInt;
import backport.everything_backport.chaoscubed.worldgen.WeightedRandomFeatureConfiguration;
import backport.everything_backport.chaoscubed.worldgen.WeightedRandomSelectorFeature;
import java.util.Optional;
import java.util.Set;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.Nullable;

/**
 * 26.2's sulfur caves and the worldgen types its features use.
 *
 * <p>The server runs TerraTonic, so the overworld's noise settings and biome source are never replaced. Instead the sulfur caves
 * biome is carved out of the climate space 26.2 gives it ({@link #isSulfurCavesClimate}) by a mixin on whatever multi-noise
 * overworld source is in use, and its surface rule is put in front of the overworld's own.
 */
public final class ChaosWorldgen {
	public static final ResourceKey<Biome> SULFUR_CAVES = ResourceKey.create(Registries.BIOME, EverythingBackport.vanilla("sulfur_caves"));
	public static final ResourceKey<NormalNoise.NoiseParameters> SULFUR_CAVE_GRADIENT = ResourceKey.create(
		Registries.NOISE, EverythingBackport.vanilla("sulfur_cave_gradient")
	);
	/** Underground biomes that keep their space when the climate also fits sulfur caves: vanilla's and those that tag themselves. */
	public static final TagKey<Biome> IS_UNDERGROUND = TagKey.create(Registries.BIOME, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "is_underground"));
	private static final Set<ResourceKey<Biome>> OVERWORLD_MARKERS = Set.of(Biomes.PLAINS, Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES, Biomes.DEEP_DARK);
	private static final Set<ResourceKey<Biome>> KEPT_CAVE_BIOMES = Set.of(Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES, Biomes.DEEP_DARK);

	// 26.2's OverworldBiomeBuilder: continentalness from coast to inland, erosion index 5 to 6, weirdness -1.1 to -0.85, depth 0.2 to 0.9.
	private static final Climate.Parameter CONTINENTALNESS = Climate.Parameter.span(-0.19F, 0.55F);
	private static final Climate.Parameter EROSION = Climate.Parameter.span(0.45F, 1.0F);
	private static final Climate.Parameter WEIRDNESS = Climate.Parameter.span(-1.1F, -0.85F);
	private static final Climate.Parameter DEPTH = Climate.Parameter.span(0.2F, 0.9F);

	public static final Feature<SpeleothemConfiguration> SPELEOTHEM = feature("speleothem", new SpeleothemFeature(SpeleothemConfiguration.CODEC));
	public static final Feature<SpeleothemClusterConfiguration> SPELEOTHEM_CLUSTER = feature(
		"speleothem_cluster", new SpeleothemClusterFeature(SpeleothemClusterConfiguration.CODEC)
	);
	public static final Feature<CompositeFeatureConfiguration> SEQUENCE = feature("sequence", new SequenceFeature(CompositeFeatureConfiguration.CODEC));
	public static final Feature<TemplateFeatureConfiguration> TEMPLATE = feature("template", new TemplateFeature(TemplateFeatureConfiguration.CODEC));
	public static final Feature<WeightedRandomFeatureConfiguration> WEIGHTED_RANDOM_SELECTOR = feature(
		"weighted_random_selector", new WeightedRandomSelectorFeature(WeightedRandomFeatureConfiguration.CODEC)
	);
	public static final IntProviderType<TrapezoidInt> TRAPEZOID = Registry.register(
		BuiltInRegistries.INT_PROVIDER_TYPE, EverythingBackport.vanilla("trapezoid"), () -> TrapezoidInt.MAP_CODEC
	);

	private static volatile @Nullable Registry<Biome> biomes;

	private ChaosWorldgen() {
	}

	private static <C extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration> Feature<C> feature(String name, Feature<C> feature) {
		return Registry.register(BuiltInRegistries.FEATURE, EverythingBackport.vanilla(name), feature);
	}

	static void init() {
		Registry.register(BuiltInRegistries.MATERIAL_CONDITION, EverythingBackport.id("noise_threshold_3d"), NoiseThreshold3dConditionSource.CODEC.codec());
		// The biome registry of the world being loaded, so biome sources can find the sulfur caves holder.
		DynamicRegistrySetupCallback.EVENT.register(view -> view.getOptional(Registries.BIOME).ifPresent(registry -> biomes = registry));
	}

	public static Optional<Holder.Reference<Biome>> sulfurCaves() {
		Registry<Biome> registry = biomes;
		return registry == null ? Optional.empty() : registry.get(SULFUR_CAVES);
	}

	/** Whether a biome source with these biomes is an overworld one (vanilla's or a datapack's like Terralith). */
	public static boolean isOverworldSource(Set<Holder<Biome>> possibleBiomes) {
		for (Holder<Biome> biome : possibleBiomes) {
			if (biome.unwrapKey().filter(OVERWORLD_MARKERS::contains).isPresent()) {
				return true;
			}
		}

		return false;
	}

	public static boolean isSulfurCavesClimate(Climate.TargetPoint point) {
		return CONTINENTALNESS.distance(point.continentalness()) == 0L
			&& EROSION.distance(point.erosion()) == 0L
			&& WEIRDNESS.distance(point.weirdness()) == 0L
			&& DEPTH.distance(point.depth()) == 0L;
	}

	/** Whether the biome the source picked may give way to sulfur caves; other underground biomes keep their space. */
	public static boolean canBecomeSulfurCaves(Holder<Biome> picked) {
		return !picked.is(SULFUR_CAVES) && !picked.is(IS_UNDERGROUND) && picked.unwrapKey().filter(KEPT_CAVE_BIOMES::contains).isEmpty();
	}

	/**
	 * 26.2's sulfur caves surface rule: every stone block in the biome becomes cinnabar or sulfur in bands of a 3D noise. Close to the
	 * surface, where the overworld rule would put dirt or grass, the gaps between the bands stay stone, as in 26.2.
	 */
	public static SurfaceRules.RuleSource sulfurCavesSurfaceRule() {
		SurfaceRules.RuleSource cinnabar = SurfaceRules.state(ChaosBlocks.CINNABAR.defaultBlockState());
		SurfaceRules.RuleSource sulfur = SurfaceRules.state(ChaosBlocks.SULFUR.defaultBlockState());
		return SurfaceRules.ifTrue(
			SurfaceRules.isBiome(SULFUR_CAVES),
			SurfaceRules.ifTrue(
				SurfaceRules.yBlockCheck(VerticalAnchor.aboveBottom(5), 0),
				SurfaceRules.sequence(
					SurfaceRules.ifTrue(new NoiseThreshold3dConditionSource(SULFUR_CAVE_GRADIENT, -0.4F, -0.1F), cinnabar),
					SurfaceRules.ifTrue(new NoiseThreshold3dConditionSource(SULFUR_CAVE_GRADIENT, 0.0, 0.4F), sulfur),
					SurfaceRules.ifTrue(new NoiseThreshold3dConditionSource(SULFUR_CAVE_GRADIENT, 0.4F, Double.MAX_VALUE), cinnabar),
					SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.state(Blocks.STONE.defaultBlockState()))
				)
			)
		);
	}
}
