package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearAnimations;
import backport.everything_backport.client.mountsofmayhem.SpearRenderState;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Moves a held spear with 1.21.11's stab and charge animations. */
@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {
	@Inject(
		method = "renderArmWithItem",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"
		)
	)
	private void everythingBackport$spearItem(
		ArmedEntityRenderState state,
		ItemStackRenderState itemStackRenderState,
		HumanoidArm arm,
		PoseStack poseStack,
		MultiBufferSource multiBufferSource,
		int light,
		CallbackInfo ci
	) {
		SpearRenderState spearState = SpearRenderState.of(state);
		if (state instanceof HumanoidRenderState humanoid) {
			if (humanoid.attackTime > 0.0F && state.mainArm == arm && spearState.everythingBackport$getSwingAnimationType() == SwingAnimationType.STAB) {
				SpearAnimations.thirdPersonAttackItem(state, poseStack);
			}

			boolean usingThisArm = humanoid.isUsingItem && humanoid.useItemHand == InteractionHand.MAIN_HAND == (arm == state.mainArm);
			float ticksUsingItem = usingThisArm ? spearState.everythingBackport$getTicksUsingItem() : 0.0F;
			if (ticksUsingItem != 0.0F && spearState.everythingBackport$isSpearArm(arm)) {
				SpearAnimations.thirdPersonUseItem(state, poseStack, ticksUsingItem, arm, spearState.everythingBackport$getHandItemStack(arm));
			}
		}
	}
}
