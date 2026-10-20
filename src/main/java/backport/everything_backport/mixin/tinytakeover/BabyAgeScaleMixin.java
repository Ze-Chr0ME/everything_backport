package backport.everything_backport.mixin.tinytakeover;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.1's bigger baby camels (0.6, was 0.45) and goats (0.55, was 0.5). */
@Mixin({LivingEntity.class, Camel.class})
public abstract class BabyAgeScaleMixin {
	@Inject(method = "getAgeScale", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$babyScale(CallbackInfoReturnable<Float> cir) {
		LivingEntity self = (LivingEntity)(Object)this;
		if (self.isBaby()) {
			if (self instanceof Camel) {
				cir.setReturnValue(0.6F);
			} else if (self instanceof Goat) {
				cir.setReturnValue(0.55F);
			}
		}
	}
}
