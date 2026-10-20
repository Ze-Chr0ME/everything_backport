package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinySounds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Horse foals have their own voice in 26.1. */
@Mixin(Horse.class)
public abstract class HorseBabySoundMixin {
	@ModifyExpressionValue(method = "playGallopSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_BREATHE:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$breathe(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_BREATHE : original;
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_AMBIENT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$ambient(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_AMBIENT : original;
	}

	@ModifyExpressionValue(method = "getDeathSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_DEATH:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$death(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_DEATH : original;
	}

	@ModifyExpressionValue(method = "getEatingSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_EAT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$eat(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_EAT : original;
	}

	@ModifyExpressionValue(method = "getHurtSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_HURT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$hurt(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_HURT : original;
	}

	@ModifyExpressionValue(method = "getAngrySound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_ANGRY:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$angry(SoundEvent original) {
		return ((Horse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_ANGRY : original;
	}
}
