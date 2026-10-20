package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.SpearCombat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Weapons with a {@code minimum_attack_charge} can't attack before they are charged (1.21.11). */
@Mixin(Player.class)
public abstract class SpearPlayerMixin {
	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$minimumAttackCharge(Entity target, CallbackInfo ci) {
		Player player = (Player)(Object)this;
		ItemStack weapon = player.getMainHandItem();
		if (SpearCombat.cannotAttackWithItem(player, weapon, player.level().isClientSide() ? 0 : 5)) {
			ci.cancel();
		}
	}
}
