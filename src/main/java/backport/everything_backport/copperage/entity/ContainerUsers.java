package backport.everything_backport.copperage.entity;

import backport.everything_backport.mixin.ChestBlockEntityAccessor;
import backport.everything_backport.mixin.CompoundContainerAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

/** 1.21.9's {@code Container#startOpen(ContainerUser)} and friends, for chests opened by mobs. */
public final class ContainerUsers {
	private ContainerUsers() {
	}

	public static void startOpen(Container container, ContainerUser user) {
		if (container instanceof CompoundContainer compound) {
			startOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer1(), user);
			startOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer2(), user);
		} else if (container instanceof ChestBlockEntity chest && !chest.isRemoved() && !user.getLivingEntity().isSpectator()) {
			counter(chest).everythingBackport$incrementOpeners(
				user.getLivingEntity(), chest.getLevel(), chest.getBlockPos(), chest.getBlockState(), user.getContainerInteractionRange()
			);
		}
	}

	public static void stopOpen(Container container, ContainerUser user) {
		if (container instanceof CompoundContainer compound) {
			stopOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer1(), user);
			stopOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer2(), user);
		} else if (container instanceof ChestBlockEntity chest && !chest.isRemoved() && !user.getLivingEntity().isSpectator()) {
			counter(chest).everythingBackport$decrementOpeners(user.getLivingEntity(), chest.getLevel(), chest.getBlockPos(), chest.getBlockState());
		}
	}

	public static List<LivingEntity> getEntitiesWithContainerOpen(Container container) {
		if (container instanceof CompoundContainer compound) {
			List<LivingEntity> list = new ArrayList<>(getEntitiesWithContainerOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer1()));
			list.addAll(getEntitiesWithContainerOpen(((CompoundContainerAccessor)compound).everythingBackport$getContainer2()));
			return list;
		} else if (container instanceof ChestBlockEntity chest && chest.getLevel() != null) {
			return counter(chest).everythingBackport$getEntitiesWithContainerOpen(chest.getLevel(), chest.getBlockPos());
		} else {
			return List.of();
		}
	}

	private static MobOpenersCounter counter(ChestBlockEntity chest) {
		return (MobOpenersCounter)((ChestBlockEntityAccessor)chest).everythingBackport$getOpenersCounter();
	}
}
