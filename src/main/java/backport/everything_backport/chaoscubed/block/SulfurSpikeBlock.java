package backport.everything_backport.chaoscubed.block;

import backport.everything_backport.chaoscubed.ChaosSounds;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SulfurSpikeBlock extends SpeleothemBlock {
	private static final int MAX_GROWING_LENGTH = 2;
	public static final MapCodec<SulfurSpikeBlock> CODEC = RecordCodecBuilder.mapCodec(
		i -> i.group(BlockState.CODEC.fieldOf("block_to_grow_on").forGetter(b -> b.blockToGrowOn), propertiesCodec()).apply(i, SulfurSpikeBlock::new)
	);

	@Override
	public MapCodec<SulfurSpikeBlock> codec() {
		return CODEC;
	}

	public SulfurSpikeBlock(BlockState blockToGrowOn, BlockBehaviour.Properties properties) {
		super(blockToGrowOn, properties);
	}

	@Override
	protected SoundEvent getStalactiteLandingSound() {
		return ChaosSounds.SULFUR_SPIKE_LAND;
	}

	@Override
	protected int getMaxGrowthLength() {
		return MAX_GROWING_LENGTH;
	}
}
