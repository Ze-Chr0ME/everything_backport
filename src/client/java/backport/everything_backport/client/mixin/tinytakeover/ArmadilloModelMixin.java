package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyMeshes;
import backport.everything_backport.client.tinytakeover.animation.BabyArmadilloAnimation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.ArmadilloModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** An armadillo model built on the 26.1 baby mesh plays the baby keyframes, as the 26.1 BabyArmadilloModel does. */
@Mixin(ArmadilloModel.class)
public abstract class ArmadilloModelMixin {
	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/ArmadilloAnimation;ARMADILLO_WALK:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$armadilloBabyWalk(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? BabyArmadilloAnimation.ARMADILLO_BABY_WALK : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/ArmadilloAnimation;ARMADILLO_ROLL_OUT:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$armadilloBabyRollOut(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_OUT : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/ArmadilloAnimation;ARMADILLO_ROLL_UP:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$armadilloBabyRollUp(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_UP : original;
	}

	@ModifyExpressionValue(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/animation/definitions/ArmadilloAnimation;ARMADILLO_PEEK:Lnet/minecraft/client/animation/AnimationDefinition;"))
	private AnimationDefinition everythingBackport$armadilloBabyPeek(AnimationDefinition original, @Local(argsOnly = true) ModelPart root) {
		return root.hasChild(BabyMeshes.BABY_MARKER) ? BabyArmadilloAnimation.ARMADILLO_BABY_PEEK : original;
	}
}
