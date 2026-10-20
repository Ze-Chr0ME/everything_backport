package backport.everything_backport.copperage.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;

/** A non-player entity that can hold a container open (1.21.9). Players are handled by vanilla. */
public interface ContainerUser {
	boolean hasContainerOpen(ContainerOpenersCounter containerOpenersCounter, BlockPos blockPos);

	double getContainerInteractionRange();

	default LivingEntity getLivingEntity() {
		if (this instanceof LivingEntity livingEntity) {
			return livingEntity;
		} else {
			throw new IllegalStateException("A container user must be a LivingEntity");
		}
	}
}
