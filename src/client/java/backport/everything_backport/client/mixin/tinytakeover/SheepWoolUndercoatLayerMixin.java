package backport.everything_backport.client.mixin.tinytakeover;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 26.1 lambs have no wool undercoat. */
@Mixin(SheepWoolUndercoatLayer.class)
public abstract class SheepWoolUndercoatLayerMixin {
	@Inject(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void everythingBackport$noBabyUndercoat(PoseStack poseStack, MultiBufferSource bufferSource, int light, SheepRenderState state, float yRot, float xRot, CallbackInfo ci) {
		if (state.isBaby) {
			ci.cancel();
		}
	}
}
