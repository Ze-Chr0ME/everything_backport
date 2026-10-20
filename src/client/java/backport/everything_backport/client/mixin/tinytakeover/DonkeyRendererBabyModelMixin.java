package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyDonkeyModel;
import net.minecraft.client.renderer.entity.DonkeyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DonkeyRenderer.class)
public abstract class DonkeyRendererBabyModelMixin {
	/** Rebuilds the baby model on the baked 26.1 baby mesh. The constructor call is an argument to super(), which can't be wrapped. */
	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyModel(CallbackInfo ci) {
		AgeableMobRendererAccessor renderer = (AgeableMobRendererAccessor)this;
		renderer.everythingBackport$setBabyModel(new BabyDonkeyModel(renderer.everythingBackport$getBabyModel().root()));
	}
}
