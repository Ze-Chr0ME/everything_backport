package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyMeshes;
import backport.everything_backport.client.tinytakeover.animation.CamelBabyAnimation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** A camel model built on the 26.1 baby mesh plays the baby keyframes, as the 26.1 BabyCamelModel does. */
@Mixin(CamelModel.class)
public abstract class CamelModelMixin {
	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_WALK:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabyWalk(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_WALK : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_SIT:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabySit(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_SIT : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_SIT_POSE:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabySitPose(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_SIT_POSE : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_STANDUP:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabyStandup(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_STANDUP : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_IDLE:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabyIdle(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_IDLE : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/CamelAnimation;CAMEL_DASH:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$camelBabyDash(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? CamelBabyAnimation.CAMEL_BABY_DASH : original;
	}
}
