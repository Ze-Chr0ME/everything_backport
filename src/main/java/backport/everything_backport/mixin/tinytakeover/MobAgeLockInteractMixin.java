package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLockable;
import backport.everything_backport.tinytakeover.AgeLocking;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * In 26.1 {@code AgeableMob.mobInteract} (and the tadpole's) handles the golden dandelion. 1.21.7's ageable mobs don't
 * share one, and none of them treat the dandelion as anything else, so it is checked right before the mob's own
 * interaction runs.
 */
@Mixin(Mob.class)
public abstract class MobAgeLockInteractMixin {
	@Inject(
		method = "interact",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"),
		cancellable = true
	)
	private void everythingBackport$goldenDandelion(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		Mob self = (Mob)(Object)this;
		if (self instanceof AgeLockable lockable) {
			ItemStack itemInHand = player.getItemInHand(hand);
			if (AgeLocking.canUseGoldenDandelion(
				itemInHand, lockable.everythingBackport$canBeAgeLocked(), lockable.everythingBackport$getAgeLockParticleTimer(), self
			)) {
				AgeLocking.setAgeLocked(self, lockable, player, itemInHand);
				self.gameEvent(GameEvent.ENTITY_INTERACT, player);
				cir.setReturnValue(InteractionResult.SUCCESS);
			}
		}
	}
}
