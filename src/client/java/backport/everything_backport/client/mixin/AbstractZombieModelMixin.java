package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearRenderState;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.11 zombies holding a spear keep the spear pose instead of reaching out with both arms. */
@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin {
	@WrapOperation(
		method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;animateZombieArms(Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;ZFF)V")
	)
	private void everythingBackport$spearArms(
		ModelPart leftArm, ModelPart rightArm, boolean aggressive, float attackTime, float ageInTicks, Operation<Void> original, ZombieRenderState state
	) {
		if (SpearRenderState.of(state).everythingBackport$getSwingAnimationType() == SwingAnimationType.STAB) {
			AnimationUtils.bobArms(rightArm, leftArm, ageInTicks);
		} else {
			original.call(leftArm, rightArm, aggressive, attackTime, ageInTicks);
		}
	}
}
