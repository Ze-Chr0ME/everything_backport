package backport.everything_backport.client.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.component.UseEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** 1.21.11's {@code use_effects} component: how much using an item slows the player, and whether they can sprint. */
@Mixin(LocalPlayer.class)
public abstract class SpearLocalPlayerMixin {
	@ModifyConstant(method = "modifyInput", constant = @Constant(floatValue = 0.2F))
	private float everythingBackport$useSpeedMultiplier(float multiplier) {
		UseEffects useEffects = this.everythingBackport$useEffects();
		return useEffects != null ? useEffects.speedMultiplier() : multiplier;
	}

	@WrapOperation(
		method = {"aiStep", "canStartSprinting"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z")
	)
	private boolean everythingBackport$slowDueToUsingItem(LocalPlayer player, Operation<Boolean> original) {
		UseEffects useEffects = this.everythingBackport$useEffects();
		return original.call(player) && (useEffects == null || !useEffects.canSprint());
	}

	@Unique
	private UseEffects everythingBackport$useEffects() {
		return ((LocalPlayer)(Object)this).getUseItem().get(MountsComponents.USE_EFFECTS);
	}
}
