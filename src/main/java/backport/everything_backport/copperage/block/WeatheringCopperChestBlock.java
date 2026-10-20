package backport.everything_backport.copperage.block;

import backport.everything_backport.copperage.entity.ContainerUsers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
	public WeatheringCopperChestBlock(
		WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties
	) {
		super(weatherState, openSound, closeSound, properties);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState blockState) {
		return WeatheringCopper.getNext(blockState.getBlock()).isPresent();
	}

	@Override
	protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (blockState.getValue(ChestBlock.TYPE) != ChestType.RIGHT
			&& serverLevel.getBlockEntity(blockPos) instanceof ChestBlockEntity chestBlockEntity
			&& ContainerUsers.getEntitiesWithContainerOpen(chestBlockEntity).isEmpty()) {
			this.changeOverTime(blockState, serverLevel, blockPos, randomSource);
		}
	}

	@Override
	public WeatheringCopper.WeatherState getAge() {
		return this.getState();
	}

	@Override
	public boolean isWaxed() {
		return false;
	}
}
