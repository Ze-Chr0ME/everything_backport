package backport.everything_backport.copperage.block;

import backport.everything_backport.copperage.CopperAgeBlocks;
import backport.everything_backport.copperage.CopperAgeTags;
import com.google.common.collect.BiMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

/**
 * Copper chest. It uses the vanilla chest block entity; open and close sounds are picked by ChestBlockEntityMixin,
 * and chests of different oxidation connect to each other.
 */
public class CopperChestBlock extends ChestBlock implements KeepsBlockEntity {
	private static final Map<Block, Supplier<Block>> COPPER_TO_COPPER_CHEST_MAPPING = Map.of(
		Blocks.COPPER_BLOCK,
		() -> CopperAgeBlocks.COPPER_CHEST,
		Blocks.EXPOSED_COPPER,
		() -> CopperAgeBlocks.EXPOSED_COPPER_CHEST,
		Blocks.WEATHERED_COPPER,
		() -> CopperAgeBlocks.WEATHERED_COPPER_CHEST,
		Blocks.OXIDIZED_COPPER,
		() -> CopperAgeBlocks.OXIDIZED_COPPER_CHEST,
		Blocks.WAXED_COPPER_BLOCK,
		() -> CopperAgeBlocks.COPPER_CHEST,
		Blocks.WAXED_EXPOSED_COPPER,
		() -> CopperAgeBlocks.EXPOSED_COPPER_CHEST,
		Blocks.WAXED_WEATHERED_COPPER,
		() -> CopperAgeBlocks.WEATHERED_COPPER_CHEST,
		Blocks.WAXED_OXIDIZED_COPPER,
		() -> CopperAgeBlocks.OXIDIZED_COPPER_CHEST
	);
	private final WeatheringCopper.WeatherState weatherState;
	private final SoundEvent openSound;
	private final SoundEvent closeSound;

	public CopperChestBlock(WeatheringCopper.WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
		super(() -> BlockEntityType.CHEST, properties);
		this.weatherState = weatherState;
		this.openSound = openSound;
		this.closeSound = closeSound;
	}

	public SoundEvent getOpenChestSound() {
		return this.openSound;
	}

	public SoundEvent getCloseChestSound() {
		return this.closeSound;
	}

	public boolean chestCanConnectTo(BlockState blockState) {
		return blockState.is(CopperAgeTags.Blocks.COPPER_CHESTS) && blockState.hasProperty(ChestBlock.TYPE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		Level level = blockPlaceContext.getLevel();
		BlockPos blockPos = blockPlaceContext.getClickedPos();
		ChestType chestType = ChestType.SINGLE;
		Direction direction = blockPlaceContext.getHorizontalDirection().getOpposite();
		FluidState fluidState = level.getFluidState(blockPos);
		boolean secondaryUse = blockPlaceContext.isSecondaryUseActive();
		Direction clickedFace = blockPlaceContext.getClickedFace();
		if (clickedFace.getAxis().isHorizontal() && secondaryUse) {
			Direction partnerFacing = this.candidatePartnerFacing(level, blockPos, clickedFace.getOpposite());
			if (partnerFacing != null && partnerFacing.getAxis() != clickedFace.getAxis()) {
				direction = partnerFacing;
				chestType = partnerFacing.getCounterClockWise() == clickedFace.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
			}
		}

		if (chestType == ChestType.SINGLE && !secondaryUse) {
			chestType = this.getChestType(level, blockPos, direction);
		}

		BlockState blockState = this.defaultBlockState()
			.setValue(FACING, direction)
			.setValue(TYPE, chestType)
			.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
		return getLeastOxidizedChestOfConnectedBlocks(blockState, level, blockPos);
	}

	protected ChestType getChestType(Level level, BlockPos blockPos, Direction direction) {
		if (direction == this.candidatePartnerFacing(level, blockPos, direction.getClockWise())) {
			return ChestType.LEFT;
		} else {
			return direction == this.candidatePartnerFacing(level, blockPos, direction.getCounterClockWise()) ? ChestType.RIGHT : ChestType.SINGLE;
		}
	}

	@Nullable
	private Direction candidatePartnerFacing(Level level, BlockPos blockPos, Direction direction) {
		BlockState blockState = level.getBlockState(blockPos.relative(direction));
		return this.chestCanConnectTo(blockState) && blockState.getValue(TYPE) == ChestType.SINGLE ? blockState.getValue(FACING) : null;
	}

	private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState blockState, Level level, BlockPos blockPos) {
		BlockState other = level.getBlockState(blockPos.relative(getConnectedDirection(blockState)));
		if (blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE
			&& blockState.getBlock() instanceof CopperChestBlock chest
			&& other.getBlock() instanceof CopperChestBlock otherChest) {
			BlockState self = blockState;
			BlockState partner = other;
			if (chest.isWaxed() != otherChest.isWaxed()) {
				self = unwaxBlock(chest, blockState).orElse(blockState);
				partner = unwaxBlock(otherChest, other).orElse(other);
			}

			Block block = chest.weatherState.ordinal() <= otherChest.weatherState.ordinal() ? self.getBlock() : partner.getBlock();
			return block.withPropertiesOf(self);
		} else {
			return blockState;
		}
	}

	@Override
	protected BlockState updateShape(
		BlockState blockState,
		LevelReader levelReader,
		ScheduledTickAccess scheduledTickAccess,
		BlockPos blockPos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource randomSource
	) {
		if (blockState.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
		}

		BlockState result = blockState;
		if (this.chestCanConnectTo(neighborState) && direction.getAxis().isHorizontal()) {
			ChestType chestType = neighborState.getValue(TYPE);
			if (blockState.getValue(TYPE) == ChestType.SINGLE
				&& chestType != ChestType.SINGLE
				&& blockState.getValue(FACING) == neighborState.getValue(FACING)
				&& getConnectedDirection(neighborState) == direction.getOpposite()) {
				result = blockState.setValue(TYPE, chestType.getOpposite());
			}
		} else if (getConnectedDirection(blockState) == direction) {
			result = blockState.setValue(TYPE, ChestType.SINGLE);
		}

		if (this.chestCanConnectTo(neighborState)) {
			ChestType chestType = result.getValue(ChestBlock.TYPE);
			if (chestType != ChestType.SINGLE && getConnectedDirection(result) == direction) {
				return neighborState.getBlock().withPropertiesOf(result);
			}
		}

		return result;
	}

	private static Optional<BlockState> unwaxBlock(CopperChestBlock chest, BlockState blockState) {
		return !chest.isWaxed()
			? Optional.of(blockState)
			: Optional.ofNullable((Block)((BiMap<Block, Block>)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(blockState.getBlock()))
				.map(block -> block.withPropertiesOf(blockState));
	}

	public WeatheringCopper.WeatherState getState() {
		return this.weatherState;
	}

	public static BlockState getFromCopperBlock(Block block, Direction direction, Level level, BlockPos blockPos) {
		CopperChestBlock chest = (CopperChestBlock)COPPER_TO_COPPER_CHEST_MAPPING.getOrDefault(block, () -> CopperAgeBlocks.COPPER_CHEST).get();
		ChestType chestType = chest.getChestType(level, blockPos, direction);
		BlockState blockState = chest.defaultBlockState().setValue(FACING, direction).setValue(TYPE, chestType);
		return getLeastOxidizedChestOfConnectedBlocks(blockState, level, blockPos);
	}

	public boolean isWaxed() {
		return true;
	}

	@Override
	public boolean shouldChangedStateKeepBlockEntity(BlockState newState) {
		return newState.is(CopperAgeTags.Blocks.COPPER_CHESTS);
	}
}
