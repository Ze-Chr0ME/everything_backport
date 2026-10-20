package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyTextures;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Babies with a 26.1 model wear the 26.1 baby texture, looked up from the adult texture the renderer picks. */
@Mixin(LivingEntityRenderer.class)
public abstract class BabyTextureMixin {
	@WrapOperation(
		method = "getRenderType",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/ResourceLocation;"
		)
	)
	private ResourceLocation everythingBackport$babyTexture(LivingEntityRenderer<?, ?, ?> renderer, LivingEntityRenderState state, Operation<ResourceLocation> original) {
		ResourceLocation texture = original.call(renderer, state);
		return state.isBaby ? BabyTextures.forBaby(texture) : texture;
	}
}
