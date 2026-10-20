package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyOcelotModel;
import net.minecraft.client.renderer.entity.OcelotRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OcelotRenderer.class)
public abstract class OcelotRendererBabyModelMixin {
	/** Rebuilds the baby model on the baked 26.1 baby mesh. The constructor call is an argument to super(), which can't be wrapped. */
	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyModel(CallbackInfo ci) {
		AgeableMobRendererAccessor renderer = (AgeableMobRendererAccessor)this;
		renderer.everythingBackport$setBabyModel(new BabyOcelotModel(renderer.everythingBackport$getBabyModel().root()));
	}
}
