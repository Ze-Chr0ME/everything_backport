package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.util.Mth;

/** 26.1's {@code BabyFelineModel.setupAnim}, shared by kittens and baby ocelots. */
final class BabyFelineAnimation {
	private BabyFelineAnimation() {
	}

	static void setupAnim(
		FelineRenderState state, ModelPart head, ModelPart body, ModelPart tail1, ModelPart tail2, ModelPart leftHindLeg, ModelPart rightHindLeg,
		ModelPart leftFrontLeg, ModelPart rightFrontLeg
	) {
		float ageScale = state.ageScale;
		if (state.isCrouching) {
			body.y += 1.0F * ageScale;
			head.y += 2.0F * ageScale;
			tail1.y += 1.0F * ageScale;
			tail2.y += -4.0F * ageScale;
			tail2.z += 2.0F * ageScale;
			tail1.xRot = (float) (Math.PI / 2);
			tail2.xRot = (float) (Math.PI / 2);
		} else if (state.isSprinting) {
			tail2.y = tail1.y;
			tail2.z += 2.0F * ageScale;
			tail1.xRot = (float) (Math.PI / 2);
			tail2.xRot = (float) (Math.PI / 2);
		}

		head.xRot = state.xRot * (float) (Math.PI / 180.0);
		head.yRot = state.yRot * (float) (Math.PI / 180.0);
		if (!state.isSitting) {
			float animationSpeed = state.walkAnimationSpeed;
			float animationPos = state.walkAnimationPos;
			if (state.isSprinting) {
				leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
				rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F + 0.3F) * animationSpeed;
				leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI + 0.3F) * animationSpeed;
				rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI) * animationSpeed;
				tail2.xRot = 1.7278761F + (float) (Math.PI / 10) * Mth.cos(animationPos) * animationSpeed;
			} else {
				leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
				rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI) * animationSpeed;
				leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI) * animationSpeed;
				rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F) * animationSpeed;
				if (!state.isCrouching) {
					tail2.xRot = 1.7278761F + (float) (Math.PI / 4) * Mth.cos(animationPos) * animationSpeed;
				} else {
					tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(animationPos) * animationSpeed;
				}
			}
		} else {
			body.xRot += -0.43633232F;
			body.y += 1.25F;
			head.z += 0.75F;
			tail1.xRot += 0.5454154F;
			tail1.y += 4.0F;
			tail1.z -= 0.9F;
			leftHindLeg.z -= 0.9F;
			rightHindLeg.z -= 0.9F;
		}

		if (state.lieDownAmount > 0.0F) {
			body.x++;
			head.xRot = Mth.rotLerp(state.lieDownAmount, head.xRot, (float) (Math.PI / 18));
			head.zRot = Mth.rotLerp(state.lieDownAmount, head.zRot, (float) (-Math.PI * 5.0 / 12.0));
			head.x += 1.5F;
			head.y += 0.75F;
			head.z -= 0.5F;
			rightFrontLeg.xRot = (float) (-Math.PI / 4);
			rightFrontLeg.x += 3.5F;
			rightFrontLeg.y -= 0.5F;
			leftFrontLeg.xRot = (float) (-Math.PI / 2);
			leftFrontLeg.x += 1.5F;
			leftFrontLeg.y--;
			leftFrontLeg.z -= 2.0F;
			rightHindLeg.xRot = (float) (Math.PI * 2.0 / 9.0);
			rightHindLeg.yRot = (float) (Math.PI / 9);
			rightHindLeg.zRot = (float) (-Math.PI / 9);
			rightHindLeg.x += 2.5F;
			rightHindLeg.y -= 0.25F;
			rightHindLeg.z += 0.5F;
			leftHindLeg.x += 1.5F;
			leftHindLeg.z--;
			tail1.xRot = tail1.xRot + Mth.rotLerp(state.lieDownAmountTail, tail1.xRot, (float) (-Math.PI / 6));
			tail1.yRot = tail1.yRot + Mth.rotLerp(state.lieDownAmountTail, tail1.yRot, 0.0F);
			tail1.zRot = tail1.zRot + Mth.rotLerp(state.lieDownAmountTail, tail1.zRot, (float) (-Math.PI / 18));
			tail1.x++;
			tail1.y += 0.5F;
			tail1.z -= 0.25F;
		}

		if (state.relaxStateOneAmount > 0.0F) {
			head.xRot = Mth.rotLerp(state.relaxStateOneAmount, head.xRot, -0.58177644F);
		}
	}
}
