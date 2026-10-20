package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyTextures;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Collars, wool and the drowned overlay switch to their 26.1 baby textures on babies. */
@Mixin(RenderLayer.class)
public abstract class RenderLayerBabyTextureMixin {
	@ModifyVariable(method = "renderColoredCutoutModel", at = @At("HEAD"), argsOnly = true)
	private static ResourceLocation everythingBackport$babyTexture(ResourceLocation texture, @Local(argsOnly = true) LivingEntityRenderState state) {
		return state.isBaby ? BabyTextures.forBaby(texture) : texture;
	}
}
