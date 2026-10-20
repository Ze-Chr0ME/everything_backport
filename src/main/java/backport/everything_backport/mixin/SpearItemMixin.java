package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Items with a kinetic weapon component are charged by holding use (1.21.11). */
@Mixin(Item.class)
public abstract class SpearItemMixin {
	@Inject(method = "use", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$chargeKineticWeapon(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() == InteractionResult.PASS) {
			ItemStack itemStack = player.getItemInHand(hand);
			KineticWeapon kineticWeapon = itemStack.get(MountsComponents.KINETIC_WEAPON);
			if (kineticWeapon != null) {
				player.startUsingItem(hand);
				kineticWeapon.makeSound(player);
				cir.setReturnValue(InteractionResult.CONSUME);
			}
		}
	}

	@Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$kineticUseDuration(ItemStack itemStack, LivingEntity livingEntity, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() == 0 && itemStack.has(MountsComponents.KINETIC_WEAPON)) {
			cir.setReturnValue(72000);
		}
	}
}
