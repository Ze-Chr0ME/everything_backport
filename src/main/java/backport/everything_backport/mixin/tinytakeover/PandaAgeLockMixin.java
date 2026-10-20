package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** A locked baby panda refuses bamboo instead of growing from it (26.1). */
@Mixin(Panda.class)
public abstract class PandaAgeLockMixin {
	@Inject(
		method = "mobInteract",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Panda;isBaby()Z", ordinal = 0),
		cancellable = true
	)
	private void everythingBackport$lockedBabyIgnoresFood(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		Panda self = (Panda)(Object)this;
		if (self.isBaby() && AgeLocking.isAgeLocked(self)) {
			cir.setReturnValue(InteractionResult.PASS);
		}
	}
}
