package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyMeshes;
import backport.everything_backport.client.tinytakeover.animation.BabyArmadilloAnimation;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.ArmadilloModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** An armadillo model built on the 26.1 baby mesh plays the baby keyframes, as the 26.1 BabyArmadilloModel does. */
@Mixin(ArmadilloModel.class)
public abstract class ArmadilloModelMixin {
	@Shadow @Final @Mutable private KeyframeAnimation walkAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation rollOutAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation rollUpAnimation;
	@Shadow @Final @Mutable private KeyframeAnimation peekAnimation;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyAnimations(ModelPart root, CallbackInfo ci) {
		if (root.hasChild(BabyMeshes.BABY_MARKER)) {
			this.walkAnimation = BabyArmadilloAnimation.ARMADILLO_BABY_WALK.bake(root);
			this.rollOutAnimation = BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_OUT.bake(root);
			this.rollUpAnimation = BabyArmadilloAnimation.ARMADILLO_BABY_ROLL_UP.bake(root);
			this.peekAnimation = BabyArmadilloAnimation.ARMADILLO_BABY_PEEK.bake(root);
		}
	}
}
