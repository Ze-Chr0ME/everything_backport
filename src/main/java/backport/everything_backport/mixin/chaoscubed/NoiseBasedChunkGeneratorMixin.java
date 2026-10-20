package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosWorldgen;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Puts 26.2's sulfur caves surface rule in front of the world's own overworld rule, whichever noise settings provide it, so
 * TerraTonic's settings stay untouched. Only generators whose biome source holds sulfur caves get it.
 */
@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {
	@Unique
	private static SurfaceRules.RuleSource everythingBackport$sulfurCavesRule;

	@ModifyExpressionValue(
		method = {"buildSurface(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/biome/BiomeManager;Lnet/minecraft/core/Registry;Lnet/minecraft/world/level/levelgen/blending/Blender;)V", "applyCarvers"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;surfaceRule()Lnet/minecraft/world/level/levelgen/SurfaceRules$RuleSource;")
	)
	private SurfaceRules.RuleSource everythingBackport$addSulfurCavesRule(SurfaceRules.RuleSource rule) {
		Holder<Biome> sulfurCaves = ChaosWorldgen.sulfurCaves().orElse(null);
		if (sulfurCaves == null || !((ChunkGenerator)(Object)this).getBiomeSource().possibleBiomes().contains(sulfurCaves)) {
			return rule;
		}

		if (everythingBackport$sulfurCavesRule == null) {
			everythingBackport$sulfurCavesRule = ChaosWorldgen.sulfurCavesSurfaceRule();
		}

		return SurfaceRules.sequence(everythingBackport$sulfurCavesRule, rule);
	}
}
