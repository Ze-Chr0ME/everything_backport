package backport.everything_backport.copperage.block;

import backport.everything_backport.copperage.CopperAgeSounds;
import backport.everything_backport.copperage.CopperAgeTags;
import backport.everything_backport.copperage.block.entity.ShelfBlockEntity;
import backport.everything_backport.copperage.block.state.SideChainPart;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
	public static final MapCodec<ShelfBlock> CODEC = simpleCodec(ShelfBlock::new);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = EnumProperty.create("side_chain", SideChainPart.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(
		Shapes.or(Block.box(0.0, 12.0, 11.0, 16.0, 16.0, 13.0), Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 11.0, 16.0, 4.0, 13.0))
	);

	@Override
	public MapCodec<ShelfBlock> codec() {
		return CODEC;
	}

	public ShelfBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.stateDefinition
				.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(POWERED, false)
				.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED)
				.setValue(WATERLOGGED, false)
		);
	}

	@Override
	protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPES.get(blockState.getValue(FACING));
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState blockState) {
		return true;
	}

	@Override
	protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
		return pathComputationType == PathComputationType.WATER && blockState.getFluidState().is(FluidTags.WATER);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new ShelfBlockEntity(blockPos, blockState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, SIDE_CHAIN_PART, WATERLOGGED);
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, boolean movedByPiston) {
		Containers.updateNeighboursAfterDestroy(blockState, serverLevel, blockPos);
		this.updateNeighborsAfterPoweringDown(serverLevel, blockPos, blockState);
	}

	@Override
	protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
		if (!level.isClientSide()) {
			boolean powered = level.hasNeighborSignal(blockPos);
			if (blockState.getValue(POWERED) != powered) {
				BlockState newState = blockState.setValue(POWERED, powered);
				if (!powered) {
					newState = newState.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED);
				}

				level.setBlock(blockPos, newState, 3);
				this.playSound(level, blockPos, powered ? CopperAgeSounds.SHELF_ACTIVATE : CopperAgeSounds.SHELF_DEACTIVATE);
				level.gameEvent(powered ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, blockPos, GameEvent.Context.of(newState));
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
		return this.defaultBlockState()
			.setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
			.setValue(POWERED, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos()))
			.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}

	@Override
	public int getRows() {
		return 1;
	}

	@Override
	public int getColumns() {
		return 3;
	}

	@Override
	protected InteractionResult useItemOn(
		ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
	) {
		if (level.getBlockEntity(blockPos) instanceof ShelfBlockEntity shelf && !interactionHand.equals(InteractionHand.OFF_HAND)) {
			OptionalInt slot = this.getHitSlot(blockHitResult, blockState.getValue(FACING));
			if (slot.isEmpty()) {
				return InteractionResult.PASS;
			} else if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			} else {
				Inventory inventory = player.getInventory();
				if (!blockState.getValue(POWERED)) {
					boolean tookItem = swapSingleItem(itemStack, player, shelf, slot.getAsInt(), inventory);
					if (tookItem) {
						this.playSound(level, blockPos, itemStack.isEmpty() ? CopperAgeSounds.SHELF_TAKE_ITEM : CopperAgeSounds.SHELF_SINGLE_SWAP);
					} else {
						if (itemStack.isEmpty()) {
							return InteractionResult.PASS;
						}

						this.playSound(level, blockPos, CopperAgeSounds.SHELF_PLACE_ITEM);
					}

					return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
				} else {
					ItemStack selected = inventory.getSelectedItem();
					boolean swapped = this.swapHotbar(level, blockPos, inventory);
					if (!swapped) {
						return InteractionResult.CONSUME;
					} else {
						this.playSound(level, blockPos, CopperAgeSounds.SHELF_MULTI_SWAP);
						return selected == inventory.getSelectedItem() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS.heldItemTransformedTo(inventory.getSelectedItem());
					}
				}
			}
		} else {
			return InteractionResult.PASS;
		}
	}

	private static boolean swapSingleItem(ItemStack itemStack, Player player, ShelfBlockEntity shelf, int slot, Inventory inventory) {
		ItemStack previous = shelf.swapItemNoUpdate(slot, itemStack);
		ItemStack result = player.hasInfiniteMaterials() && previous.isEmpty() ? itemStack.copy() : previous;
		inventory.setItem(inventory.getSelectedSlot(), result);
		inventory.setChanged();
		shelf.setChanged(GameEvent.ITEM_INTERACT_FINISH);
		return !previous.isEmpty();
	}

	private boolean swapHotbar(Level level, BlockPos blockPos, Inventory inventory) {
		List<BlockPos> shelves = this.getAllBlocksConnectedTo(level, blockPos);
		if (shelves.isEmpty()) {
			return false;
		} else {
			boolean swapped = false;

			for (int i = 0; i < shelves.size(); i++) {
				ShelfBlockEntity shelf = (ShelfBlockEntity)level.getBlockEntity(shelves.get(i));
				if (shelf != null) {
					for (int j = 0; j < shelf.getContainerSize(); j++) {
						int k = 9 - (shelves.size() - i) * shelf.getContainerSize() + j;
						if (k >= 0 && k <= inventory.getContainerSize()) {
							ItemStack fromHotbar = inventory.removeItemNoUpdate(k);
							ItemStack fromShelf = shelf.swapItemNoUpdate(j, fromHotbar);
							if (!fromHotbar.isEmpty() || !fromShelf.isEmpty()) {
								inventory.setItem(k, fromShelf);
								swapped = true;
							}
						}
					}

					inventory.setChanged();
					shelf.setChanged(GameEvent.ENTITY_INTERACT);
				}
			}

			return swapped;
		}
	}

	@Override
	public SideChainPart getSideChainPart(BlockState blockState) {
		return blockState.getValue(SIDE_CHAIN_PART);
	}

	@Override
	public BlockState setSideChainPart(BlockState blockState, SideChainPart sideChainPart) {
		return blockState.setValue(SIDE_CHAIN_PART, sideChainPart);
	}

	@Override
	public Direction getFacing(BlockState blockState) {
		return blockState.getValue(FACING);
	}

	@Override
	public boolean isConnectable(BlockState blockState) {
		return blockState.is(CopperAgeTags.Blocks.WOODEN_SHELVES) && blockState.hasProperty(POWERED) && blockState.getValue(POWERED);
	}

	@Override
	public int getMaxChainLength() {
		return 3;
	}

	@Override
	protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
		if (blockState.getValue(POWERED)) {
			this.updateSelfAndNeighborsOnPoweringUp(level, blockPos, blockState, oldState);
		} else {
			this.updateNeighborsAfterPoweringDown(level, blockPos, blockState);
		}
	}

	private void playSound(LevelAccessor levelAccessor, BlockPos blockPos, SoundEvent soundEvent) {
		levelAccessor.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	protected FluidState getFluidState(BlockState blockState) {
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
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

		return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, neighborPos, neighborState, randomSource);
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState blockState) {
		return true;
	}

	/**
	 * 1.21.9 only outputs a signal towards the back of the shelf. 1.21.7 has no direction here, so the
	 * comparator check is done by ComparatorBlockMixin before this is called.
	 */
	@Override
	protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
		if (level.isClientSide()) {
			return 0;
		} else if (level.getBlockEntity(blockPos) instanceof ShelfBlockEntity shelf) {
			int a = shelf.getItem(0).isEmpty() ? 0 : 1;
			int b = shelf.getItem(1).isEmpty() ? 0 : 1;
			int c = shelf.getItem(2).isEmpty() ? 0 : 1;
			return a | b << 1 | c << 2;
		} else {
			return 0;
		}
	}
}
