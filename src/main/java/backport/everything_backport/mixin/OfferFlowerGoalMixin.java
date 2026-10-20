package backport.everything_backport.mixin;

import backport.everything_backport.copperage.CopperAgeTags;
import backport.everything_backport.copperage.entity.CopperGolem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.21.9: iron golems offer poppies to any mob in #candidate_for_iron_golem_gift, and copper golems wear them. */
@Mixin(OfferFlowerGoal.class)
public abstract class OfferFlowerGoalMixin extends Goal {
	@Unique
	private static final TargetingConditions OFFER_TARGET_CONTEXT = TargetingConditions.forNonCombat().range(6.0);
	@Shadow
	@Final
	private IronGolem golem;
	@Shadow
	private int tick;
	@Unique
	@Nullable
	private LivingEntity everythingBackport$entity;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$canUse(CallbackInfoReturnable<Boolean> cir) {
		if (!this.golem.level().isBrightOutside()) {
			cir.setReturnValue(false);
		} else if (this.golem.getRandom().nextInt(8000) != 0) {
			cir.setReturnValue(false);
		} else {
			var level = getServerLevel(this.golem);
			var candidates = level.getEntitiesOfClass(
				LivingEntity.class, this.everythingBackport$getGolemBoundingBox(), entity -> entity.getType().is(CopperAgeTags.EntityTypes.CANDIDATE_FOR_IRON_GOLEM_GIFT)
			);
			this.everythingBackport$entity = level.getNearestEntity(
				candidates, OFFER_TARGET_CONTEXT, this.golem, this.golem.getX(), this.golem.getY(), this.golem.getZ()
			);
			cir.setReturnValue(this.everythingBackport$entity != null);
		}
	}

	@Inject(method = "stop", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$stop(CallbackInfo ci) {
		this.golem.offerFlower(false);
		if (this.tick == 0
			&& this.everythingBackport$entity instanceof Mob mob
			&& mob.getType().is(CopperAgeTags.EntityTypes.ACCEPTS_IRON_GOLEM_GIFT)
			&& mob.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA).isEmpty()
			&& this.everythingBackport$getGolemBoundingBox().intersects(mob.getBoundingBox())) {
			mob.setItemSlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA, Items.POPPY.getDefaultInstance());
			mob.setGuaranteedDrop(CopperGolem.EQUIPMENT_SLOT_ANTENNA);
		}

		this.everythingBackport$entity = null;
		ci.cancel();
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$tick(CallbackInfo ci) {
		if (this.everythingBackport$entity != null) {
			this.golem.getLookControl().setLookAt(this.everythingBackport$entity, 30.0F, 30.0F);
		}

		this.tick--;
		ci.cancel();
	}

	@Unique
	private AABB everythingBackport$getGolemBoundingBox() {
		return this.golem.getBoundingBox().inflate(6.0, 2.0, 6.0);
	}
}
