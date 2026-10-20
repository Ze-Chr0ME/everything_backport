package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyTextures;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Foals draw their markings from 26.1's baby marking textures. */
@Mixin(HorseMarkingLayer.class)
public abstract class HorseMarkingLayerMixin {
	@ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object everythingBackport$babyMarkings(Object texture, @Local(argsOnly = true) HorseRenderState state) {
		return state.isBaby ? BabyTextures.forBaby((ResourceLocation)texture) : texture;
	}
}
