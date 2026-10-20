package backport.everything_backport.mixin;

import backport.everything_backport.copperage.CopperAgeEntities;
import backport.everything_backport.copperage.CopperAgeTags;
import backport.everything_backport.copperage.block.CopperChestBlock;
import backport.everything_backport.copperage.entity.CopperGolem;
import com.google.common.collect.BiMap;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Builds copper golems from a carved pumpkin on a copper block, which turns into a copper chest (1.21.9). */
@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
	@Shadow
	@Final
	private static Predicate<BlockState> PUMPKINS_PREDICATE;
	@Unique
	@Nullable
	private BlockPattern everythingBackport$copperGolemBase;
	@Unique
	@Nullable
	private BlockPattern everythingBackport$copperGolemFull;

	@Shadow
	private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch blockPatternMatch, Entity entity, BlockPos blockPos) {
		throw new AssertionError();
	}

	@Inject(method = "canSpawnGolem", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$canSpawnCopperGolem(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && this.everythingBackport$getOrCreateCopperGolemBase().find(levelReader, blockPos) != null) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "trySpawnGolem", at = @At("TAIL"))
	private void everythingBackport$trySpawnCopperGolem(Level level, BlockPos blockPos, CallbackInfo ci) {
		BlockPattern.BlockPatternMatch match = this.everythingBackport$getOrCreateCopperGolemFull().find(level, blockPos);
		if (match != null) {
			CopperGolem copperGolem = CopperAgeEntities.COPPER_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
			if (copperGolem != null) {
				WeatheringCopper.WeatherState weatherState = everythingBackport$getWeatherStateFromPattern(match);
				BlockInWorld copperBlock = match.getBlock(0, 1, 0);
				Block copper = copperBlock.getState().getBlock();
				Direction facing = match.getBlock(0, 0, 0).getState().getValue(CarvedPumpkinBlock.FACING);
				spawnGolemInWorld(level, match, copperGolem, match.getBlock(0, 0, 0).getPos());
				level.setBlock(copperBlock.getPos(), CopperChestBlock.getFromCopperBlock(copper, facing, level, copperBlock.getPos()), 2);
				copperGolem.spawn(weatherState);
			}
		}
	}

	@Unique
	private static WeatheringCopper.WeatherState everythingBackport$getWeatherStateFromPattern(BlockPattern.BlockPatternMatch match) {
		BlockState blockState = match.getBlock(0, 1, 0).getState();
		return blockState.getBlock() instanceof WeatheringCopper weatheringCopper
			? weatheringCopper.getAge()
			: Optional.ofNullable((Block)((BiMap<Block, Block>)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(blockState.getBlock()))
				.filter(block -> block instanceof WeatheringCopper)
				.map(block -> (WeatheringCopper)block)
				.orElse((WeatheringCopper)Blocks.COPPER_BLOCK)
				.getAge();
	}

	@Unique
	private BlockPattern everythingBackport$getOrCreateCopperGolemBase() {
		if (this.everythingBackport$copperGolemBase == null) {
			this.everythingBackport$copperGolemBase = BlockPatternBuilder.start()
				.aisle(" ", "#")
				.where('#', BlockInWorld.hasState(blockState -> blockState.is(CopperAgeTags.Blocks.COPPER)))
				.build();
		}

		return this.everythingBackport$copperGolemBase;
	}

	@Unique
	private BlockPattern everythingBackport$getOrCreateCopperGolemFull() {
		if (this.everythingBackport$copperGolemFull == null) {
			this.everythingBackport$copperGolemFull = BlockPatternBuilder.start()
				.aisle("^", "#")
				.where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
				.where('#', BlockInWorld.hasState(blockState -> blockState.is(CopperAgeTags.Blocks.COPPER)))
				.build();
		}

		return this.everythingBackport$copperGolemFull;
	}
}
