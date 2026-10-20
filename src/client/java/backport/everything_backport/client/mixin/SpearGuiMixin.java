package backport.everything_backport.client.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** The crosshair attack indicator only lights up when the target is within the held item's attack range (1.21.11). */
@Mixin(Gui.class)
public abstract class SpearGuiMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@ModifyExpressionValue(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isAlive()Z"))
	private boolean everythingBackport$inAttackRange(boolean alive) {
		AttackRange attackRange = SpearCombat.getActiveItem(this.minecraft.player).get(MountsComponents.ATTACK_RANGE);
		return alive && (attackRange == null || attackRange.isInRange(this.minecraft.player, this.minecraft.hitResult.getLocation()));
	}
}
