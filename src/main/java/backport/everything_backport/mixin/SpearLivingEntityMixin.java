package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import backport.everything_backport.mountsofmayhem.component.SwingAnimation;
import backport.everything_backport.mountsofmayhem.component.UseEffects;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Remembers what a charged spear already hit and plays the hit feedback, takes the swing duration from the held item
 * and skips sculk vibrations for items that opt out of them (1.21.11).
 */
@Mixin(LivingEntity.class)
public abstract class SpearLivingEntityMixin extends Entity implements SpearCombat.StabTracker {
	@Unique
	@Nullable
	private Object2LongMap<Entity> everythingBackport$recentKineticEnemies;
	@Unique
	private long everythingBackport$lastKineticHitFeedbackTime = Integer.MIN_VALUE;

	@Shadow
	protected ItemStack useItem;

	private SpearLivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean everythingBackport$wasRecentlyStabbed(Entity entity, int cooldownTicks) {
		if (this.everythingBackport$recentKineticEnemies == null) {
			return false;
		} else {
			return this.everythingBackport$recentKineticEnemies.containsKey(entity)
				&& this.level().getGameTime() - this.everythingBackport$recentKineticEnemies.getLong(entity) < cooldownTicks;
		}
	}

	@Override
	public void everythingBackport$rememberStabbedEntity(Entity entity) {
		if (this.everythingBackport$recentKineticEnemies != null) {
			this.everythingBackport$recentKineticEnemies.put(entity, this.level().getGameTime());
		}
	}

	@Override
	public int everythingBackport$stabbedEntities(Predicate<Entity> predicate) {
		return this.everythingBackport$recentKineticEnemies == null
			? 0
			: (int)this.everythingBackport$recentKineticEnemies.keySet().stream().filter(predicate).count();
	}

	@Override
	public float everythingBackport$getTicksSinceLastKineticHitFeedback(float partialTick) {
		return this.everythingBackport$lastKineticHitFeedbackTime < 0L
			? 0.0F
			: (float)(this.level().getGameTime() - this.everythingBackport$lastKineticHitFeedbackTime) + partialTick;
	}

	@Inject(method = "startUsingItem", at = @At("TAIL"))
	private void everythingBackport$startKineticUse(InteractionHand hand, CallbackInfo ci) {
		if (!this.level().isClientSide() && this.useItem.has(MountsComponents.KINETIC_WEAPON)) {
			this.everythingBackport$recentKineticEnemies = new Object2LongOpenHashMap<>();
		}
	}

	@Inject(method = "stopUsingItem", at = @At("HEAD"))
	private void everythingBackport$stopKineticUse(CallbackInfo ci) {
		if (!this.level().isClientSide()) {
			this.everythingBackport$recentKineticEnemies = null;
		}
	}

	@Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$kineticHit(byte event, CallbackInfo ci) {
		if (event == 2) {
			if (this.level().getGameTime() - this.everythingBackport$lastKineticHitFeedbackTime > 10L) {
				this.everythingBackport$lastKineticHitFeedbackTime = this.level().getGameTime();
				KineticWeapon kineticWeapon = this.useItem.get(MountsComponents.KINETIC_WEAPON);
				if (kineticWeapon != null) {
					kineticWeapon.makeLocalHitSound(this);
				}
			}

			ci.cancel();
		}
	}

	@ModifyConstant(method = "getCurrentSwingDuration", constant = @Constant(intValue = 6))
	private int everythingBackport$itemSwingDuration(int duration) {
		SwingAnimation swingAnimation = ((LivingEntity)(Object)this).getMainHandItem().get(MountsComponents.SWING_ANIMATION);
		return swingAnimation != null ? swingAnimation.duration() : duration;
	}

	@WrapWithCondition(
		method = {"startUsingItem", "stopUsingItem"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/core/Holder;)V")
	)
	private boolean everythingBackport$useVibrations(LivingEntity livingEntity, Holder<GameEvent> gameEvent) {
		UseEffects useEffects = this.useItem.get(MountsComponents.USE_EFFECTS);
		return useEffects == null || useEffects.interactVibrations();
	}
}
