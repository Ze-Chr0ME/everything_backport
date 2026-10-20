package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearAnimations;
import backport.everything_backport.client.mountsofmayhem.SpearRenderState;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11's spear arm pose and stab swing for humanoid models. */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends HumanoidRenderState> {
	@Shadow
	@Final
	public ModelPart head;
	@Shadow
	@Final
	public ModelPart body;
	@Shadow
	@Final
	public ModelPart rightArm;
	@Shadow
	@Final
	public ModelPart leftArm;

	@Inject(method = "poseRightArm", at = @At("TAIL"))
	private void everythingBackport$spearRightArm(T state, HumanoidModel.ArmPose armPose, CallbackInfo ci) {
		SpearRenderState spearState = SpearRenderState.of(state);
		if (spearState.everythingBackport$isSpearArm(HumanoidArm.RIGHT)) {
			SpearAnimations.thirdPersonHandUse(this.rightArm, this.head, true, spearState.everythingBackport$getUseItemStackForArm(state, HumanoidArm.RIGHT), state);
		}
	}

	@Inject(method = "poseLeftArm", at = @At("TAIL"))
	private void everythingBackport$spearLeftArm(T state, HumanoidModel.ArmPose armPose, CallbackInfo ci) {
		SpearRenderState spearState = SpearRenderState.of(state);
		if (spearState.everythingBackport$isSpearArm(HumanoidArm.LEFT)) {
			SpearAnimations.thirdPersonHandUse(this.leftArm, this.head, false, spearState.everythingBackport$getUseItemStackForArm(state, HumanoidArm.LEFT), state);
		}
	}

	/** 1.21.11 swings by the held item's swing animation: a whack (the 1.21.7 swing), a stab, or nothing. */
	@Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$swingAnimation(T state, float ageInTicks, CallbackInfo ci) {
		SwingAnimationType type = SpearRenderState.of(state).everythingBackport$getSwingAnimationType();
		float attackTime = state.attackTime;
		if (type != SwingAnimationType.WHACK && !(attackTime <= 0.0F)) {
			this.body.yRot = Mth.sin(Mth.sqrt(attackTime) * (float)(Math.PI * 2)) * 0.2F;
			if (state.attackArm == HumanoidArm.LEFT) {
				this.body.yRot *= -1.0F;
			}

			float ageScale = state.ageScale;
			this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F * ageScale;
			this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F * ageScale;
			this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F * ageScale;
			this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F * ageScale;
			this.rightArm.yRot = this.rightArm.yRot + this.body.yRot;
			this.leftArm.yRot = this.leftArm.yRot + this.body.yRot;
			this.leftArm.xRot = this.leftArm.xRot + this.body.yRot;
			if (type == SwingAnimationType.STAB) {
				SpearAnimations.thirdPersonAttackHand((HumanoidModel<T>)(Object)this, state);
			}

			ci.cancel();
		}
	}
}
