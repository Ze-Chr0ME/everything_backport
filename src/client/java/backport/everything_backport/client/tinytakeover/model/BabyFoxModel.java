package backport.everything_backport.client.tinytakeover.model;

import backport.everything_backport.client.tinytakeover.animation.FoxBabyAnimation;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.util.Mth;

/** 26.1's kit: its own walk keyframes and sit, sleep and crouch poses, on 26.1's fox animation. */
public class BabyFoxModel extends FoxModel {
	private final ModelPart body;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart tail;
	private final KeyframeAnimation babyWalkAnimation;
	private float legMotionPos;

	public BabyFoxModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.tail = this.body.getChild("tail");
		this.babyWalkAnimation = FoxBabyAnimation.FOX_BABY_WALK.bake(root);
	}

	@Override
	public void setupAnim(FoxRenderState state) {
		this.resetPose();
		this.setWalkingPose(state);
		if (state.isCrouching) {
			this.setCrouchingPose(state);
		} else if (state.isSleeping) {
			this.setSleepingPose(state);
		} else if (state.isSitting) {
			this.setSittingPose(state);
		}

		if (!state.isSleeping && !state.isFaceplanted && !state.isCrouching) {
			this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
			this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
		}

		if (state.isSleeping) {
			this.head.xRot = 0.0F;
			this.head.yRot = (float) (-Math.PI * 2.0 / 3.0);
			this.head.zRot = Mth.cos(state.ageInTicks * 0.027F) / 22.0F;
		}

		if (state.isFaceplanted) {
			this.legMotionPos += 0.67F;
			this.rightHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F) * 0.1F;
			this.leftHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F + (float) Math.PI) * 0.1F;
			this.rightFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F + (float) Math.PI) * 0.1F;
			this.leftFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662F) * 0.1F;
		}
	}

	private void setWalkingPose(FoxRenderState state) {
		this.head.zRot = state.headRollAngle;
		this.rightHindLeg.visible = true;
		this.leftHindLeg.visible = true;
		this.rightFrontLeg.visible = true;
		this.leftFrontLeg.visible = true;
		this.babyWalkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 1.0F, 2.5F);
	}

	private void setCrouchingPose(FoxRenderState state) {
		this.body.xRot += 0.10471976F;
		this.head.y = this.head.y + state.crouchAmount * state.ageScale;
		float wiggleAmount = Mth.cos(state.ageInTicks) * 0.05F;
		this.body.yRot = wiggleAmount;
		this.rightHindLeg.zRot = wiggleAmount;
		this.leftHindLeg.zRot = wiggleAmount;
		this.rightFrontLeg.zRot = wiggleAmount / 2.0F;
		this.leftFrontLeg.zRot = wiggleAmount / 2.0F;
		this.body.y = this.body.y + state.crouchAmount / 6.0F;
	}

	private void setSleepingPose(FoxRenderState state) {
		this.rightHindLeg.visible = false;
		this.leftHindLeg.visible = false;
		this.rightFrontLeg.visible = false;
		this.leftFrontLeg.visible = false;
		this.body.zRot = (float) (-Math.PI / 2);
		this.body.xRot = (float) (-Math.PI / 18);
		this.body.y += 1.5F;
		this.body.z -= 1.5F;
		this.body.x -= 1.5F;
		this.tail.xRot = -2.1816616F;
		this.tail.x -= 0.7F;
		this.tail.z += 0.6F;
		this.tail.y += 0.9F;
		this.head.x -= 2.0F;
		this.head.y += 2.8F;
		this.head.z -= 4.0F;
		this.head.yRot = (float) (-Math.PI * 2.0 / 3.0);
		this.head.zRot = 0.0F;
	}

	private void setSittingPose(FoxRenderState state) {
		this.head.xRot = 0.0F;
		this.head.yRot = 0.0F;
		this.body.xRot = -0.959931F;
		this.body.z = this.body.z - 4.5F * state.ageScale;
		this.body.y = this.body.y + 3.0F * state.ageScale;
		this.tail.y -= 0.6F;
		this.tail.z = this.tail.z - 2.0F * state.ageScale;
		this.tail.xRot = 0.95993114F;
		this.head.y -= 0.75F;
		this.rightFrontLeg.xRot = (float) (-Math.PI / 12);
		this.leftFrontLeg.xRot = (float) (-Math.PI / 12);
		this.rightFrontLeg.z -= 1.5F;
		this.leftFrontLeg.z -= 1.5F;
		this.rightFrontLeg.x += 0.01F;
		this.leftFrontLeg.x -= 0.01F;
		this.rightHindLeg.z -= 3.75F;
		this.leftHindLeg.z -= 3.75F;
		this.rightHindLeg.x += 0.01F;
		this.leftHindLeg.x -= 0.01F;
	}
}
