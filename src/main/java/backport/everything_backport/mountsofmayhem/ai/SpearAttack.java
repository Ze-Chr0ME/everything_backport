package backport.everything_backport.mountsofmayhem.ai;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.MountsMemories;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import backport.everything_backport.mountsofmayhem.entity.MountedCharger;
import java.util.Map;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class SpearAttack extends Behavior<PathfinderMob> {
	public static final int MIN_REPOSITION_DISTANCE = 6;
	public static final int MAX_REPOSITION_DISTANCE = 7;
	double speedModifierWhenCharging;
	double speedModifierWhenRepositioning;
	float approachDistanceSq;
	float targetInRangeRadiusSq;

	public SpearAttack(double d, double e, float f, float g) {
		super(Map.of(MountsMemories.SPEAR_STATUS, MemoryStatus.VALUE_PRESENT));
		this.speedModifierWhenCharging = d;
		this.speedModifierWhenRepositioning = e;
		this.approachDistanceSq = f * f;
		this.targetInRangeRadiusSq = g * g;
	}

	@Nullable
	private LivingEntity getTarget(PathfinderMob pathfinderMob) {
		return (LivingEntity)pathfinderMob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
	}

	private boolean ableToAttack(PathfinderMob pathfinderMob) {
		return this.getTarget(pathfinderMob) != null && pathfinderMob.getMainHandItem().has(MountsComponents.KINETIC_WEAPON);
	}

	private int getKineticWeaponUseDuration(PathfinderMob pathfinderMob) {
		return (Integer)Optional.ofNullable(pathfinderMob.getMainHandItem().get(MountsComponents.KINETIC_WEAPON))
			.map(KineticWeapon::computeDamageUseDuration)
			.orElse(0);
	}

	protected boolean checkExtraStartConditions(ServerLevel serverLevel, PathfinderMob pathfinderMob) {
		return pathfinderMob.getBrain().getMemory(MountsMemories.SPEAR_STATUS).orElse(SpearAttack.SpearStatus.APPROACH) == SpearAttack.SpearStatus.CHARGING
			&& this.ableToAttack(pathfinderMob)
			&& !pathfinderMob.isUsingItem();
	}

	protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		pathfinderMob.setAggressive(true);
		pathfinderMob.getBrain().setMemory(MountsMemories.SPEAR_ENGAGE_TIME, this.getKineticWeaponUseDuration(pathfinderMob));
		pathfinderMob.getBrain().eraseMemory(MountsMemories.SPEAR_CHARGE_POSITION);
		pathfinderMob.startUsingItem(InteractionHand.MAIN_HAND);
		super.start(serverLevel, pathfinderMob, l);
	}

	protected boolean canStillUse(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		return (Integer)pathfinderMob.getBrain().getMemory(MountsMemories.SPEAR_ENGAGE_TIME).orElse(0) > 0 && this.ableToAttack(pathfinderMob);
	}

	protected void tick(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		LivingEntity livingEntity = this.getTarget(pathfinderMob);
		double d = pathfinderMob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
		Entity entity = pathfinderMob.getRootVehicle();
		float f = MountedCharger.of(entity);

		int i = pathfinderMob.isPassenger() ? 2 : 0;
		pathfinderMob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity, true));
		pathfinderMob.getBrain()
			.setMemory(MountsMemories.SPEAR_ENGAGE_TIME, (Integer)pathfinderMob.getBrain().getMemory(MountsMemories.SPEAR_ENGAGE_TIME).orElse(0) - 1);
		Vec3 vec3 = (Vec3)pathfinderMob.getBrain().getMemory(MountsMemories.SPEAR_CHARGE_POSITION).orElse(null);
		if (vec3 != null) {
			pathfinderMob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, f * this.speedModifierWhenRepositioning);
			if (pathfinderMob.getNavigation().isDone()) {
				pathfinderMob.getBrain().eraseMemory(MountsMemories.SPEAR_CHARGE_POSITION);
			}
		} else {
			pathfinderMob.getNavigation().moveTo(livingEntity, f * this.speedModifierWhenCharging);
			if (d < this.targetInRangeRadiusSq || pathfinderMob.getNavigation().isDone()) {
				double e = Math.sqrt(d);
				Vec3 vec32 = LandRandomPos.getPosAway(pathfinderMob, 6 + i - e, 7 + i - e, 7, livingEntity.position());
				pathfinderMob.getBrain().setMemory(MountsMemories.SPEAR_CHARGE_POSITION, vec32);
			}
		}
	}

	protected void stop(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l) {
		pathfinderMob.getNavigation().stop();
		pathfinderMob.stopUsingItem();
		pathfinderMob.getBrain().eraseMemory(MountsMemories.SPEAR_CHARGE_POSITION);
		pathfinderMob.getBrain().eraseMemory(MountsMemories.SPEAR_ENGAGE_TIME);
		pathfinderMob.getBrain().setMemory(MountsMemories.SPEAR_STATUS, SpearAttack.SpearStatus.RETREAT);
	}

	@Override
	protected boolean timedOut(long l) {
		return false;
	}

	public static enum SpearStatus {
		APPROACH,
		CHARGING,
		RETREAT;
	}
}
