package backport.everything_backport.chaoscubed.worldgen;

import backport.everything_backport.chaoscubed.ChaosWorldgen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;

/** 26.2's {@code minecraft:trapezoid} int provider, used by the sulfur spring features. */
public class TrapezoidInt extends IntProvider {
	public static final MapCodec<TrapezoidInt> MAP_CODEC = RecordCodecBuilder.<TrapezoidInt>mapCodec(
			i -> i.group(
					Codec.INT.fieldOf("min").forGetter(c -> c.minInclusive),
					Codec.INT.fieldOf("max").forGetter(c -> c.maxInclusive),
					Codec.INT.fieldOf("plateau").forGetter(c -> c.plateau)
				)
				.apply(i, TrapezoidInt::new)
		)
		.validate(
			c -> {
				if (c.maxInclusive < c.minInclusive) {
					return DataResult.error(() -> "Max must be larger than min: [" + c.minInclusive + ", " + c.maxInclusive + "]");
				} else {
					return c.plateau > c.maxInclusive - c.minInclusive
						? DataResult.error(() -> "Plateau can at most be the full span: [" + c.minInclusive + ", " + c.maxInclusive + "]")
						: DataResult.success(c);
				}
			}
		);
	private final int minInclusive;
	private final int maxInclusive;
	private final int plateau;

	public TrapezoidInt(int minInclusive, int maxInclusive, int plateau) {
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
		this.plateau = plateau;
	}

	@Override
	public int sample(RandomSource random) {
		if (this.plateau == 0 && this.maxInclusive == -this.minInclusive) {
			return random.nextInt(this.maxInclusive + 1) - random.nextInt(this.maxInclusive + 1);
		}

		int range = this.maxInclusive - this.minInclusive;
		if (this.plateau == range) {
			return Mth.randomBetweenInclusive(random, this.minInclusive, this.maxInclusive);
		}

		int plateauStart = (range - this.plateau) / 2;
		int plateauEnd = range - plateauStart;
		return this.minInclusive + Mth.randomBetweenInclusive(random, 0, plateauEnd) + Mth.randomBetweenInclusive(random, 0, plateauStart);
	}

	@Override
	public int getMinValue() {
		return this.minInclusive;
	}

	@Override
	public int getMaxValue() {
		return this.maxInclusive;
	}

	@Override
	public IntProviderType<?> getType() {
		return ChaosWorldgen.TRAPEZOID;
	}

	@Override
	public String toString() {
		return "trapezoid(" + this.plateau + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
	}
}
