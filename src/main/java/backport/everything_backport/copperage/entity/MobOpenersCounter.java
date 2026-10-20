package backport.everything_backport.copperage.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Added to {@code ContainerOpenersCounter} by ContainerOpenersCounterMixin. */
public interface MobOpenersCounter {
	void everythingBackport$incrementOpeners(LivingEntity entity, Level level, BlockPos blockPos, BlockState blockState, double range);

	void everythingBackport$decrementOpeners(LivingEntity entity, Level level, BlockPos blockPos, BlockState blockState);

	/** Players and other {@link ContainerUser}s that have this container open. */
	List<LivingEntity> everythingBackport$getEntitiesWithContainerOpen(Level level, BlockPos blockPos);
}
