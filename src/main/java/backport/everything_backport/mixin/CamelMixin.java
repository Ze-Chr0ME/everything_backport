package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsSounds;
import backport.everything_backport.mountsofmayhem.entity.CamelHusk;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.11 turned the camel's dash, sit and stand sounds into overridable getters; camel husks swap in their own. */
@Mixin(Camel.class)
public abstract class CamelMixin {
	@ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAMEL_DASH_READY:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$dashReadySound(SoundEvent original) {
		return (Object)this instanceof CamelHusk ? MountsSounds.CAMEL_HUSK_DASH_READY : original;
	}

	@ModifyExpressionValue(method = "handleStartJump", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAMEL_DASH:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$dashSound(SoundEvent original) {
		return (Object)this instanceof CamelHusk ? MountsSounds.CAMEL_HUSK_DASH : original;
	}

	@ModifyExpressionValue(method = "sitDown", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAMEL_SIT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$sitSound(SoundEvent original) {
		return (Object)this instanceof CamelHusk ? MountsSounds.CAMEL_HUSK_SIT : original;
	}

	@ModifyExpressionValue(method = "standUp", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAMEL_STAND:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$standSound(SoundEvent original) {
		return (Object)this instanceof CamelHusk ? MountsSounds.CAMEL_HUSK_STAND : original;
	}
}
