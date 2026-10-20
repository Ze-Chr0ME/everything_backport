package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mobs holding an {@code attack_range} item use its reach for melee attacks (1.21.11). */
@Mixin(Mob.class)
public abstract class SpearMobMixin {
	@Inject(method = "isWithinMeleeAttackRange", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$attackRange(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		Mob mob = (Mob)(Object)this;
		AttackRange attackRange = SpearCombat.getActiveItem(mob).get(MountsComponents.ATTACK_RANGE);
		if (attackRange != null) {
			double max = attackRange.effectiveMaxRange(mob);
			double min = attackRange.effectiveMinRange(mob);
			AABB hitbox = target.getHitbox();
			cir.setReturnValue(everythingBackport$attackBox(mob, max).intersects(hitbox) && (min <= 0.0 || !everythingBackport$attackBox(mob, min).intersects(hitbox)));
		}
	}

	@Unique
	private static AABB everythingBackport$attackBox(Mob mob, double reach) {
		Entity vehicle = mob.getVehicle();
		AABB box;
		if (vehicle != null) {
			AABB vehicleBox = vehicle.getBoundingBox();
			AABB ownBox = mob.getBoundingBox();
			box = new AABB(
				Math.min(ownBox.minX, vehicleBox.minX), ownBox.minY, Math.min(ownBox.minZ, vehicleBox.minZ), Math.max(ownBox.maxX, vehicleBox.maxX), ownBox.maxY,
				Math.max(ownBox.maxZ, vehicleBox.maxZ)
			);
		} else {
			box = mob.getBoundingBox();
		}

		return box.inflate(reach, 0.0, reach);
	}
}
