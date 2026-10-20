package backport.everything_backport.chaoscubed.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

/** 26.2's {@code noise_threshold} surface condition with {@code is_3d}: the noise is sampled at the block's height too. */
public record NoiseThreshold3dConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold)
	implements SurfaceRules.ConditionSource {
	public static final KeyDispatchDataCodec<NoiseThreshold3dConditionSource> CODEC = KeyDispatchDataCodec.of(
		RecordCodecBuilder.<NoiseThreshold3dConditionSource>mapCodec(
			i -> i.group(
					ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThreshold3dConditionSource::noise),
					Codec.DOUBLE.fieldOf("min_threshold").forGetter(NoiseThreshold3dConditionSource::minThreshold),
					Codec.DOUBLE.fieldOf("max_threshold").forGetter(NoiseThreshold3dConditionSource::maxThreshold)
				)
				.apply(i, NoiseThreshold3dConditionSource::new)
		)
	);

	@Override
	public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
		return CODEC;
	}

	@Override
	public SurfaceRules.Condition apply(SurfaceRules.Context context) {
		NormalNoise normalNoise = context.randomState.getOrCreateNoise(this.noise);
		return new SurfaceRules.Condition() {
			private long lastUpdate = context.lastUpdateY - 1L;
			private boolean result;

			@Override
			public boolean test() {
				if (this.lastUpdate != context.lastUpdateY) {
					this.lastUpdate = context.lastUpdateY;
					double value = normalNoise.getValue(context.blockX, context.blockY, context.blockZ);
					this.result = value >= NoiseThreshold3dConditionSource.this.minThreshold && value <= NoiseThreshold3dConditionSource.this.maxThreshold;
				}

				return this.result;
			}
		};
	}
}
