package backport.everything_backport.mountsofmayhem.ai;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.MountsMemories;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import backport.everything_backport.mountsofmayhem.entity.MountedCharger;
import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class SpearApproach extends Behavior<PathfinderMob> {
	double speedModifierWhenRepositioning;
	float approachDistanceSq;

	public SpearApproach(double d, float f) {
		super(Map.of(MountsMemories.SPEAR_STATUS, MemoryStatus.VALUE_ABSENT));
		this.speedModifierWhenRepositioning = d;
		this.approachDistanceSq = f * f;
	}

	private boolean ableToAttack(PathfinderMob pathfinderMob) {
		return this.getTarget(pathfinderMob) != null && pathfinderMob.getMainHandItem().has(MountsComponents.KINETIC_WEAPON);
	}

	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PathfinderMob pathfinderMob) {
		return this.ableToAttack(pathfinderMob) && !pathfinderMob.isUsingItem();
	}

	protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		pathfinderMob.setAggressive(true);
		pathfinderMob.getBrain().setMemory(MountsMemories.SPEAR_STATUS, SpearAttack.SpearStatus.APPROACH);
		super.start(serverLevel, pathfinderMob, l);
	}

	@Nullable
	private LivingEntity getTarget(PathfinderMob pathfinderMob) {
		return (LivingEntity)pathfinderMob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
	}

	protected boolean canStillUse(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		return this.ableToAttack(pathfinderMob) && this.farEnough(pathfinderMob);
	}

	private boolean farEnough(PathfinderMob pathfinderMob) {
		LivingEntity livingEntity = this.getTarget(pathfinderMob);
		double d = pathfinderMob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		return d > this.approachDistanceSq;
	}

	protected void tick(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		LivingEntity livingEntity = this.getTarget(pathfinderMob);
		Entity entity = pathfinderMob.getRootVehicle();
		float f = MountedCharger.of(entity);

		pathfinderMob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
		pathfinderMob.getNavigation().moveTo(livingEntity, f * this.speedModifierWhenRepositioning);
	}

	protected void stop(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		pathfinderMob.getNavigation().stop();
		pathfinderMob.getBrain().setMemory(MountsMemories.SPEAR_STATUS, SpearAttack.SpearStatus.CHARGING);
	}

	@Override
	protected boolean timedOut(long l) {
		return false;
	}
}
