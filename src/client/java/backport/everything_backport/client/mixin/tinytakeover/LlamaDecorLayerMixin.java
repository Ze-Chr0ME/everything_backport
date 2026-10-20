package backport.everything_backport.client.mixin.tinytakeover;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Baby llamas show no carpet, and baby trader llamas wear 26.1's smaller trader decor. */
@Mixin(LlamaDecorLayer.class)
public abstract class LlamaDecorLayerMixin {
	@Unique
	private static final ResourceKey<EquipmentAsset> TRADER_LLAMA_BABY = ResourceKey.create(
		EquipmentAssets.ROOT_ID, ResourceLocation.withDefaultNamespace("trader_llama_baby")
	);

	@Shadow
	private void renderEquipment(
		PoseStack poseStack, MultiBufferSource bufferSource, LlamaRenderState state, ItemStack itemStack, ResourceKey<EquipmentAsset> asset, int light
	) {
	}

	@Inject(
		method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/LlamaRenderState;FF)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void everythingBackport$babyDecor(PoseStack poseStack, MultiBufferSource bufferSource, int light, LlamaRenderState state, float yRot, float xRot, CallbackInfo ci) {
		if (state.isBaby) {
			if (state.isTraderLlama) {
				this.renderEquipment(poseStack, bufferSource, state, ItemStack.EMPTY, TRADER_LLAMA_BABY, light);
			}

			ci.cancel();
		}
	}
}
