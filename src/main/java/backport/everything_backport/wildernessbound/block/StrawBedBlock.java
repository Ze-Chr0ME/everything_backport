package backport.everything_backport.wildernessbound.block;

import backport.everything_backport.wildernessbound.WildSounds;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.Util;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * 26.3's straw bed. It skips the night like a bed but never sets the respawn point, and it is used up: it breaks when its sleeper gets
 * up, and in dimensions where beds don't work it breaks as soon as it is used instead of exploding. It is a {@link BedBlock} so that
 * 1.21.7's sleeping code accepts it, but it has no block entity and renders from its block model. The rules 26.3 keeps in the
 * {@code gameplay/straw_bed_rule} environment attribute are applied by {@code StrawBedSleepMixin} and {@code StrawBedLeaveMixin}.
 */
public class StrawBedBlock extends BedBlock {
	/** How high a sleeper lies: the top of the foot part, which 26.3 sleeps on even from the head. */
	public static final double SLEEP_HEIGHT = 0.25;
	private static final VoxelShape BASE_SHAPE = Block.column(16.0, 0.0, 4.0);
	private static final VoxelShape PILLOW_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 8.0);
	private static final Map<Direction, VoxelShape> FOOT_SHAPES = Util.make(() -> Shapes.rotateHorizontal(BASE_SHAPE));
	private static final Map<Direction, VoxelShape> HEAD_SHAPES = Util.make(() -> Shapes.rotateHorizontal(Shapes.or(BASE_SHAPE, PILLOW_SHAPE)));

	public StrawBedBlock(BlockBehaviour.Properties properties) {
		super(DyeColor.YELLOW, properties);
	}

	/** 26.3's {@code destroyBed}: the bed crumbles without dropping anything. */
	public static void destroyBed(Level level, BlockPos pos) {
		level.playSound(null, pos, WildSounds.STRAW_BED_BREAK_LEAVE, SoundSource.BLOCKS, 1.0F, 1.0F);
		level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS_SERVER;
		}

		if (state.getValue(PART) != BedPart.HEAD) {
			pos = pos.relative(state.getValue(FACING));
			state = level.getBlockState(pos);
			if (!state.is(this)) {
				return InteractionResult.CONSUME;
			}
		}

		if (!canSetSpawn(level)) {
			destroyBed(level, pos);
			return InteractionResult.SUCCESS_SERVER;
		}

		if (state.getValue(OCCUPIED)) {
			if (!kickVillagerOutOfBed(level, pos)) {
				player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
			}

			return InteractionResult.SUCCESS_SERVER;
		}

		player.startSleepInBed(pos).ifLeft(problem -> {
			if (problem.getMessage() != null) {
				player.displayClientMessage(problem.getMessage(), true);
			}
		});
		return InteractionResult.SUCCESS_SERVER;
	}

	private static boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
		List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
		if (villagers.isEmpty()) {
			return false;
		}

		villagers.getFirst().stopSleeping();
		return true;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Map<Direction, VoxelShape> shapes = state.getValue(PART) == BedPart.HEAD ? HEAD_SHAPES : FOOT_SHAPES;
		return shapes.get(getConnectedDirection(state).getOpposite());
	}

	/** Unlike beds, straw beds neither soften falls nor bounce. */
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
	}

	@Override
	public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
		entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return null;
	}
}
