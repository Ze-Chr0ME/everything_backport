package backport.everything_backport.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** 1.21.9 lets the original lightning rod oxidize into the new exposed, weathered and oxidized rods. */
@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockMixin extends RodBlock {
	@Unique
	private static final WeatheringCopper UNAFFECTED = () -> WeatheringCopper.WeatherState.UNAFFECTED;

	private LightningRodBlockMixin(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState blockState) {
		return blockState.is(Blocks.LIGHTNING_ROD) || super.isRandomlyTicking(blockState);
	}

	@Override
	protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (blockState.is(Blocks.LIGHTNING_ROD)) {
			UNAFFECTED.changeOverTime(blockState, serverLevel, blockPos, randomSource);
		} else {
			super.randomTick(blockState, serverLevel, blockPos, randomSource);
		}
	}
}
