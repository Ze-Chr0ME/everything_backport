package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyAxolotlModel;
import backport.everything_backport.tinytakeover.TinyAnimationStates;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Baby axolotls use the keyframe-animated 26.1 model, which is not an {@link AxolotlModel}. */
@Mixin(AxolotlRenderer.class)
public abstract class AxolotlRendererMixin {
	/** The 1.21.7 axolotl model cannot be built on the 26.1 baby mesh, so none is made; the real one is set below. */
	@WrapOperation(
		method = "<init>",
		at = @At(value = "NEW", target = "Lnet/minecraft/client/model/AxolotlModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1)
	)
	private static AxolotlModel everythingBackport$skipOldBaby(ModelPart root, Operation<AxolotlModel> original) {
		return null;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyModel(EntityRendererProvider.Context context, CallbackInfo ci) {
		((AgeableMobRendererAccessor)this).everythingBackport$setBabyModel(new BabyAxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL_BABY)));
	}

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/animal/axolotl/Axolotl;Lnet/minecraft/client/renderer/entity/state/AxolotlRenderState;F)V",
		at = @At("TAIL")
	)
	private void everythingBackport$copyAnimations(Axolotl axolotl, AxolotlRenderState state, float partialTick, CallbackInfo ci) {
		TinyAnimationStates.copy(axolotl, state);
	}
}
