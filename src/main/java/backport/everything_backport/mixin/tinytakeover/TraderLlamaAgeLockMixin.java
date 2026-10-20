package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Locked or persistent trader llamas stay after their wandering trader leaves (26.1). */
@Mixin(TraderLlama.class)
public abstract class TraderLlamaAgeLockMixin {
	@Inject(method = "canDespawn", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$keepLocked(CallbackInfoReturnable<Boolean> cir) {
		TraderLlama self = (TraderLlama)(Object)this;
		if (cir.getReturnValueZ() && (AgeLocking.isAgeLocked(self) || self.isPersistenceRequired())) {
			cir.setReturnValue(false);
		}
	}
}
