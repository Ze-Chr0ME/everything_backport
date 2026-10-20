package backport.everything_backport.mixin.wildernessbound;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.3: with a shield in the other hand, raising it takes priority over tilling and path making; crouching tills again. */
@Mixin({HoeItem.class, ShovelItem.class})
public abstract class ShieldBeforeTillingMixin {
	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$raiseShieldFirst(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		Player player = context.getPlayer();
		if (player != null && !player.isSecondaryUseActive()) {
			InteractionHand other = context.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
			if (player.getItemInHand(other).has(DataComponents.BLOCKS_ATTACKS)) {
				cir.setReturnValue(InteractionResult.PASS);
			}
		}
	}
}
