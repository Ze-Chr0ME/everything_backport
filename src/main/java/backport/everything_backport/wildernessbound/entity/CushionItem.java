package backport.everything_backport.wildernessbound.entity;

import backport.everything_backport.wildernessbound.WildEntities;
import backport.everything_backport.wildernessbound.WildSounds;
import backport.everything_backport.wildernessbound.WildTags;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** 26.3's cushion item: places a cushion of its color on the top face of a block. */
public class CushionItem extends Item {
	private static final double COLLISION_SHAPE_RAYCAST_EPSILON = 0.001;
	private final DyeColor color;

	public CushionItem(DyeColor color, Item.Properties properties) {
		super(properties);
		this.color = color;
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		UseOnContext recalculatedContext = recalculateContextForSpecialCollisionShapes(context);
		Level level = context.getLevel();
		if (recalculatedContext.getClickedFace() != Direction.UP) {
			return InteractionResult.FAIL;
		}

		BlockPlaceContext placeContext = new BlockPlaceContext(recalculatedContext);
		BlockPos blockPos = placeContext.getClickedPos();
		Vec3 entityPos = new Vec3(blockPos.getX() + 0.5, recalculatedContext.getClickLocation().y, blockPos.getZ() + 0.5);
		AABB spawnAABB = WildEntities.CUSHION.getSpawnAABB(entityPos.x, entityPos.y, entityPos.z);
		if (!Cushion.canBePlacedAt(level, spawnAABB)) {
			return InteractionResult.FAIL;
		}

		ItemStack itemStack = context.getItemInHand();
		if (level instanceof ServerLevel serverLevel) {
			if (!serverLevel.getEntitiesOfClass(Cushion.class, spawnAABB).isEmpty()) {
				return InteractionResult.FAIL;
			}

			Consumer<Cushion> entityConfig = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
			Cushion cushion = WildEntities.CUSHION.create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
			if (cushion == null) {
				return InteractionResult.FAIL;
			}

			cushion.setColor(this.color);
			cushion.snapTo(entityPos, Direction.fromYRot(placeContext.getRotation()).toYRot(), 0.0F);
			serverLevel.addFreshEntity(cushion);
			cushion.destroyIfInFire(serverLevel);
			level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), WildSounds.CUSHION_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
			cushion.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
			itemStack.consume(1, placeContext.getPlayer());
		}

		return InteractionResult.SUCCESS;
	}

	/** Hoppers, cauldrons and composters are clicked by their collision shape, so a cushion sits on their rim and not in the bowl. */
	private static UseOnContext recalculateContextForSpecialCollisionShapes(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null) {
			return context;
		}

		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		BlockState clickedState = level.getBlockState(clickedPos);
		if (!clickedState.is(WildTags.CUSHION_USES_COLLISION_SHAPE)) {
			return context;
		}

		Vec3 rayFrom = player.getEyePosition();
		Vec3 ray = context.getClickLocation().subtract(rayFrom);
		Vec3 rayTo = context.getClickLocation().add(ray.normalize().scale(COLLISION_SHAPE_RAYCAST_EPSILON));
		BlockHitResult collisionHitResult = clickedState.getCollisionShape(level, clickedPos).clip(rayFrom, rayTo, clickedPos);
		return collisionHitResult == null ? context : new UseOnContext(player, context.getHand(), collisionHitResult);
	}
}
