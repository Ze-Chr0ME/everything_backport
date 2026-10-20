package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosWorldgen;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Places 26.2's sulfur caves in whatever overworld multi-noise source the world uses, vanilla's or Terralith's, without replacing
 * it: where the climate matches 26.2's sulfur caves entry, the biome the source picked gives way, unless it is another underground
 * biome. The biome is also added to the source's possible biomes so its features are decorated.
 */
@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin {
	@Unique
	private @Nullable Holder<Biome> everythingBackport$sulfurCaves;

	// Return values are modified rather than replaced with a cancelling @Inject, so the dappled forest mixin on the same methods also runs.
	@ModifyReturnValue(method = "collectPossibleBiomes", at = @At("RETURN"))
	private Stream<Holder<Biome>> everythingBackport$addSulfurCaves(Stream<Holder<Biome>> original) {
		Set<Holder<Biome>> biomes = original.collect(Collectors.toCollection(java.util.LinkedHashSet::new));
		Optional<Holder.Reference<Biome>> sulfurCaves = ChaosWorldgen.sulfurCaves();
		if (sulfurCaves.isPresent() && ChaosWorldgen.isOverworldSource(biomes)) {
			this.everythingBackport$sulfurCaves = sulfurCaves.get();
			biomes.add(sulfurCaves.get());
		}

		return biomes.stream();
	}

	@ModifyReturnValue(method = "getNoiseBiome(Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Lnet/minecraft/core/Holder;", at = @At("RETURN"))
	private Holder<Biome> everythingBackport$placeSulfurCaves(Holder<Biome> picked, Climate.TargetPoint point) {
		// Possible biomes are collected lazily; this makes sure the check above has run for this source.
		((BiomeSource)(Object)this).possibleBiomes();
		Holder<Biome> sulfurCaves = this.everythingBackport$sulfurCaves;
		return sulfurCaves != null && ChaosWorldgen.isSulfurCavesClimate(point) && ChaosWorldgen.canBecomeSulfurCaves(picked) ? sulfurCaves : picked;
	}
}
