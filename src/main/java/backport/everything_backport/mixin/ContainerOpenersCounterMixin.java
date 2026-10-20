package backport.everything_backport.mixin;

import backport.everything_backport.copperage.entity.ContainerUser;
import backport.everything_backport.copperage.entity.MobOpenersCounter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Lets mobs such as the copper golem hold chests open, as {@code ContainerUser} does in 1.21.9. */
@Mixin(ContainerOpenersCounter.class)
public abstract class ContainerOpenersCounterMixin implements MobOpenersCounter {
	@Shadow
	private int openCount;
	@Shadow
	private double maxInteractionRange;

	@Shadow
	protected abstract void onOpen(Level level, BlockPos blockPos, BlockState blockState);

	@Shadow
	protected abstract void onClose(Level level, BlockPos blockPos, BlockState blockState);

	@Shadow
	protected abstract void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int previous, int current);

	@Shadow
	protected abstract boolean isOwnContainer(Player player);

	@Override
	public void everythingBackport$incrementOpeners(LivingEntity entity, Level level, BlockPos blockPos, BlockState blockState, double range) {
		int previous = this.openCount++;
		if (previous == 0) {
			this.onOpen(level, blockPos, blockState);
			level.gameEvent(entity, GameEvent.CONTAINER_OPEN, blockPos);
			level.scheduleTick(blockPos, blockState.getBlock(), 5);
		}

		this.openerCountChanged(level, blockPos, blockState, previous, this.openCount);
		this.maxInteractionRange = Math.max(range, this.maxInteractionRange);
	}

	@Override
	public void everythingBackport$decrementOpeners(LivingEntity entity, Level level, BlockPos blockPos, BlockState blockState) {
		int previous = this.openCount--;
		if (this.openCount == 0) {
			this.onClose(level, blockPos, blockState);
			level.gameEvent(entity, GameEvent.CONTAINER_CLOSE, blockPos);
			this.maxInteractionRange = 0.0;
		}

		this.openerCountChanged(level, blockPos, blockState, previous, this.openCount);
	}

	@Override
	public List<LivingEntity> everythingBackport$getEntitiesWithContainerOpen(Level level, BlockPos blockPos) {
		AABB area = new AABB(blockPos).inflate(Math.max(this.maxInteractionRange, 3.0) + 4.0);
		List<LivingEntity> list = new ArrayList<>();
		for (Entity entity : level.getEntities((Entity)null, area, entity -> this.everythingBackport$hasContainerOpen(entity, blockPos))) {
			list.add((LivingEntity)entity);
		}

		return list;
	}

	@Unique
	private boolean everythingBackport$hasContainerOpen(Entity entity, BlockPos blockPos) {
		if (entity instanceof Player player) {
			return !player.isSpectator() && this.isOwnContainer(player);
		} else {
			return entity instanceof ContainerUser user && !user.getLivingEntity().isSpectator() && user.hasContainerOpen((ContainerOpenersCounter)(Object)this, blockPos);
		}
	}

	/** Keep mobs counted when vanilla rechecks which players still have the container open. */
	@ModifyVariable(method = "recheckOpeners", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private int everythingBackport$countMobOpeners(int playerCount, Level level, BlockPos blockPos, BlockState blockState) {
		AABB area = new AABB(blockPos).inflate(7.0);
		return playerCount
			+ level.getEntities((Entity)null, area, entity -> !(entity instanceof Player) && this.everythingBackport$hasContainerOpen(entity, blockPos)).size();
	}
}
