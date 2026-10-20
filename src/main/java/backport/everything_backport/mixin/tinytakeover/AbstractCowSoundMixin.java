package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.sound.CowSoundHolder;
import backport.everything_backport.tinytakeover.sound.CowSoundVariant;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.AbstractCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/** Cows use their 26.1 voice; mooshrooms, which have none, keep the classic sounds. */
@Mixin(AbstractCow.class)
public abstract class AbstractCowSoundMixin {
	@Unique
	private SoundEvent everythingBackport$sound(SoundEvent original, Function<CowSoundVariant, Holder<SoundEvent>> getter) {
		return (Object)this instanceof CowSoundHolder holder ? getter.apply(holder.everythingBackport$cowSounds()).value() : original;
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;COW_AMBIENT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$ambient(SoundEvent original) {
		return this.everythingBackport$sound(original, CowSoundVariant::ambientSound);
	}

	@ModifyExpressionValue(method = "getHurtSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;COW_HURT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$hurt(SoundEvent original) {
		return this.everythingBackport$sound(original, CowSoundVariant::hurtSound);
	}

	@ModifyExpressionValue(method = "getDeathSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;COW_DEATH:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$death(SoundEvent original) {
		return this.everythingBackport$sound(original, CowSoundVariant::deathSound);
	}

	@ModifyExpressionValue(method = "playStepSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;COW_STEP:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$step(SoundEvent original) {
		return this.everythingBackport$sound(original, CowSoundVariant::stepSound);
	}
}
