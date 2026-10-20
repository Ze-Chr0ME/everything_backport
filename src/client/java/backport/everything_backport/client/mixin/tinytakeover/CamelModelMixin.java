package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyMeshes;
import backport.everything_backport.client.tinytakeover.animation.CamelBabyAnimation;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** A camel model built on the 26.1 baby mesh plays the baby keyframes, as the 26.1 BabyCamelModel does. */
@Mixin(CamelModel.class)
public abstract class CamelModelMixin {
	@Shadow @Final @Mutable private KeyframeAnimation walkAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation sitAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation sitPoseAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation standupAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation idleAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation dashAnimation;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyAnimations(ModelPart root, CallbackInfo ci) {
		if (root.hasChild(BabyMeshes.BABY_MARKER)) {
			this.walkAnimation = CamelBabyAnimation.CAMEL_BABY_WALK.bake(root);
			this.sitAnimation = CamelBabyAnimation.CAMEL_BABY_SIT.bake(root);
			this.sitPoseAnimation = CamelBabyAnimation.CAMEL_BABY_SIT_POSE.bake(root);
			this.standupAnimation = CamelBabyAnimation.CAMEL_BABY_STANDUP.bake(root);
			this.idleAnimation = CamelBabyAnimation.CAMEL_BABY_IDLE.bake(root);
			this.dashAnimation = CamelBabyAnimation.CAMEL_BABY_DASH.bake(root);
		}
	}
}
