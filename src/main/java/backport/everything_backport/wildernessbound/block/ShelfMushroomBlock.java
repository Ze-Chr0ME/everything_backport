package backport.everything_backport.wildernessbound.block;

import backport.everything_backport.wildernessbound.WildSounds;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/** 26.3's shelf mushroom: grows on the side of a block, bones meal to its large form, and bounces like a bed. */
public class ShelfMushroomBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
	public static final MapCodec<ShelfMushroomBlock> CODEC = simpleCodec(ShelfMushroomBlock::new);
	public static final int MAX_AGE = 1;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
	/** 26.3's {@code bounceRestitution} and {@code fallDistanceReduction} block properties. */
	public static final float BOUNCE_RESTITUTION = 0.75F;
	private static final float FALL_DISTANCE_REDUCTION = 0.5F;
	private static final List<Map<Direction, VoxelShape>> SHAPES = IntStream.rangeClosed(0, 1)
		.mapToObj(
			i -> Shapes.rotateHorizontal(
				Shapes.or(
					Block.column(10 + i * 4, 7 + i * 3, 1 + i, 3 + i * 2).move(0.0, (8 - i * 2) / 16.0, -(i * 1.5 - 4.5) / 16.0).optimize(),
					Block.column(6 + i * 2, 4 + i * 2, 0.0, 1 + i).move(0.0, (8 - i * 2) / 16.0, -(i - 6) / 16.0).optimize()
				)
			)
		)
		.toList();

	public ShelfMushroomBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction facing = state.getValue(FACING);
		BlockPos supportPos = pos.relative(facing.getOpposite());
		return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(AGE)).get(state.getValue(FACING));
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		LevelReader level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				state = state.setValue(FACING, direction.getOpposite());
				if (state.canSurvive(level, pos)) {
					return state;
				}
			}
		}

		return null;
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		entity.causeFallDamage(fallDistance * (1.0F - FALL_DISTANCE_REDUCTION), 1.0F, entity.damageSources().fall());
	}

	/** Bounces like 26.3's {@code Entity.restituteMovementAfterCollisions} does for a block with a restitution. */
	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
		Vec3 movement = entity.getDeltaMovement();
		if (entity.isSuppressingBounce() || movement.y >= 0.0 || -movement.y < entity.getGravity()) {
			super.updateEntityMovementAfterFallOn(level, entity);
			return;
		}

		double restitution = entity instanceof LivingEntity ? BOUNCE_RESTITUTION : BOUNCE_RESTITUTION * 0.8F;
		entity.setDeltaMovement(movement.x, -movement.y * restitution, movement.z);
		if (!(entity instanceof ItemEntity) && !(entity instanceof PrimedTnt) && !entity.level().isClientSide()) {
			entity.level().playSound(null, entity.getOnPosLegacy(), WildSounds.SHELF_MUSHROOM_BOUNCE, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	protected BlockState updateShape(
		BlockState state,
		LevelReader level,
		ScheduledTickAccess ticks,
		BlockPos pos,
		Direction directionToNeighbour,
		BlockPos neighbourPos,
		BlockState neighbourState,
		RandomSource random
	) {
		return directionToNeighbour == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, AGE);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
