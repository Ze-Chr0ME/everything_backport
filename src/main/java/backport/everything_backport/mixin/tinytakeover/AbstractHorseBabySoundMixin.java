package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinySounds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Foals land and step with the 26.1 baby horse sounds. */
@Mixin(AbstractHorse.class)
public abstract class AbstractHorseBabySoundMixin {
	@ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_LAND:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$land(SoundEvent original) {
		return ((AbstractHorse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_LAND : original;
	}

	@ModifyExpressionValue(method = "playStepSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;HORSE_STEP:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$step(SoundEvent original) {
		return ((AbstractHorse)(Object)this).isBaby() ? TinySounds.BABY_HORSE_STEP : original;
	}
}
