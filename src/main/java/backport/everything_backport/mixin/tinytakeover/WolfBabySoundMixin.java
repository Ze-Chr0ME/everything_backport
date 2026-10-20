package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinySounds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Puppies have their own sounds in 26.1, whatever the wolf sound variant. */
@Mixin(Wolf.class)
public abstract class WolfBabySoundMixin {
	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;growlSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$growlSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_GROWL : original;
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;whineSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$whineSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_WHINE : original;
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;pantSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$pantSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_PANT : original;
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;ambientSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$ambientSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_AMBIENT : original;
	}

	@ModifyExpressionValue(method = "getHurtSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;hurtSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$hurtSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_HURT : original;
	}

	@ModifyExpressionValue(method = "getDeathSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/WolfSoundVariant;deathSound()Lnet/minecraft/core/Holder;"))
	private Holder<SoundEvent> everythingBackport$deathSound(Holder<SoundEvent> original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_DEATH : original;
	}

	@ModifyExpressionValue(method = "playStepSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;WOLF_STEP:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$step(SoundEvent original) {
		return ((Wolf)(Object)this).isBaby() ? TinySounds.BABY_WOLF_STEP.value() : original;
	}
}
