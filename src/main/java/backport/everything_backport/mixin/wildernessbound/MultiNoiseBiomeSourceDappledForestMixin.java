package backport.everything_backport.mixin.wildernessbound;

import backport.everything_backport.chaoscubed.ChaosWorldgen;
import backport.everything_backport.wildernessbound.WildWorldgen;
import java.util.LinkedHashSet;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Places 26.3's dappled forest in whatever overworld multi-noise source the world uses, without replacing it: plains picked where
 * 26.3's climate table has dappled forest become dappled forest. The biome is also added to the source's possible biomes so its
 * features are decorated.
 */
@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceDappledForestMixin {
	@Unique
	private @Nullable Holder<Biome> everythingBackport$dappledForest;

	@Inject(method = "collectPossibleBiomes", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$addDappledForest(CallbackInfoReturnable<Stream<Holder<Biome>>> cir) {
		Set<Holder<Biome>> biomes = cir.getReturnValue().collect(Collectors.toCollection(LinkedHashSet::new));
		Optional<Holder.Reference<Biome>> dappledForest = WildWorldgen.dappledForest();
		if (dappledForest.isPresent() && ChaosWorldgen.isOverworldSource(biomes)) {
			this.everythingBackport$dappledForest = dappledForest.get();
			biomes.add(dappledForest.get());
		}

		cir.setReturnValue(biomes.stream());
	}

	@Inject(method = "getNoiseBiome(Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Lnet/minecraft/core/Holder;", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$placeDappledForest(Climate.TargetPoint point, CallbackInfoReturnable<Holder<Biome>> cir) {
		// Possible biomes are collected lazily; this makes sure the check above has run for this source.
		((BiomeSource)(Object)this).possibleBiomes();
		Holder<Biome> dappledForest = this.everythingBackport$dappledForest;
		if (dappledForest != null && WildWorldgen.isDappledForest(point, cir.getReturnValue())) {
			cir.setReturnValue(dappledForest);
		}
	}
}
