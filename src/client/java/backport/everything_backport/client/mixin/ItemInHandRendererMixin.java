package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearAnimations;
import backport.everything_backport.client.mountsofmayhem.SpearPoses;
import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** First-person spear animations: the stab swing and holding up a charge (1.21.11). */
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Shadow
	protected abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm humanoidArm, float equipProgress);

	@WrapOperation(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;swingArm(FFLcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/entity/HumanoidArm;)V"
		)
	)
	private void everythingBackport$swingAnimation(
		ItemInHandRenderer renderer,
		float swingProgress,
		float equipProgress,
		PoseStack poseStack,
		int side,
		HumanoidArm arm,
		Operation<Void> original,
		@Local(argsOnly = true) ItemStack itemStack
	) {
		SwingAnimationType type = SpearPoses.swingAnimation(itemStack).type();
		if (type == SwingAnimationType.WHACK) {
			original.call(renderer, swingProgress, equipProgress, poseStack, side, arm);
		} else {
			this.applyItemArmTransform(poseStack, arm, equipProgress);
			if (type == SwingAnimationType.STAB) {
				SpearAnimations.firstPersonAttack(swingProgress, poseStack, side, arm);
			}
		}
	}

	/**
	 * 1.21.7 renders a used spear with the plain "none" use animation, which only applies the arm transform; this
	 * swaps in 1.21.11's spear charge animation right before the item is drawn.
	 */
	@Inject(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		)
	)
	private void everythingBackport$spearCharge(
		AbstractClientPlayer player,
		float partialTick,
		float pitch,
		InteractionHand hand,
		float swingProgress,
		ItemStack itemStack,
		float equipProgress,
		PoseStack poseStack,
		MultiBufferSource multiBufferSource,
		int light,
		CallbackInfo ci
	) {
		if (player.isUsingItem()
			&& player.getUseItemRemainingTicks() > 0
			&& player.getUsedItemHand() == hand
			&& itemStack.has(MountsComponents.KINETIC_WEAPON)
			&& !player.isAutoSpinAttack()) {
			HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
			// Undo the equip offset: 1.21.11 holds a charging spear at the fully equipped position.
			poseStack.translate(0.0F, equipProgress * 0.6F, 0.0F);
			float ticksUsingItem = itemStack.getUseDuration(player) - (player.getUseItemRemainingTicks() - partialTick + 1.0F);
			float feedback = ((SpearCombat.StabTracker)player).everythingBackport$getTicksSinceLastKineticHitFeedback(partialTick);
			SpearAnimations.firstPersonUse(feedback, poseStack, ticksUsingItem, arm, itemStack);
		}
	}
}
