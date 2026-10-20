package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.util.Mth;

/**
 * 26.1's {@code AbstractEquineModel.setupAnim}. 1.21.7 scales its leg and head offsets for a half-size adult mesh; 26.1
 * lets each foal model supply its own standing offsets through these hooks instead.
 */
abstract class BabyEquineAnimation {
	final ModelPart body;
	final ModelPart headParts;
	final ModelPart rightHindLeg;
	final ModelPart leftHindLeg;
	final ModelPart rightFrontLeg;
	final ModelPart leftFrontLeg;
	final ModelPart tail;

	BabyEquineAnimation(
		ModelPart body, ModelPart headParts, ModelPart rightHindLeg, ModelPart leftHindLeg, ModelPart rightFrontLeg, ModelPart leftFrontLeg, ModelPart tail
	) {
		this.body = body;
		this.headParts = headParts;
		this.rightHindLeg = rightHindLeg;
		this.leftHindLeg = leftHindLeg;
		this.rightFrontLeg = rightFrontLeg;
		this.leftFrontLeg = leftFrontLeg;
		this.tail = tail;
	}

	void setupAnim(EquineRenderState state) {
		float clampedYRot = Mth.clamp(state.yRot, -20.0F, 20.0F);
		float headRotXRad = state.xRot * (float) (Math.PI / 180.0);
		float animationSpeed = state.walkAnimationSpeed;
		float animationPos = state.walkAnimationPos;
		if (animationSpeed > 0.2F) {
			headRotXRad += Mth.cos(animationPos * 0.8F) * 0.15F * animationSpeed;
		}

		float eating = state.eatAnimation;
		float standing = state.standAnimation;
		float iStanding = 1.0F - standing;
		float feedingAnim = state.feedingAnimation;
		this.headParts.xRot = (float) (Math.PI / 6) + headRotXRad;
		this.headParts.yRot = clampedYRot * (float) (Math.PI / 180.0);
		float waterMultiplier = state.isInWater ? 0.2F : 1.0F;
		float legAnim1 = Mth.cos(waterMultiplier * animationPos * 0.6662F + (float) Math.PI);
		float legXRotAnim = legAnim1 * 0.8F * animationSpeed;
		float baseHeadAngle = (1.0F - Math.max(standing, eating)) * ((float) (Math.PI / 6) + headRotXRad + feedingAnim * Mth.sin(state.ageInTicks) * 0.05F);
		this.headParts.xRot = standing * ((float) (Math.PI / 12) + headRotXRad) + eating * (2.1816616F + Mth.sin(state.ageInTicks) * 0.05F) + baseHeadAngle;
		this.headParts.yRot = standing * clampedYRot * (float) (Math.PI / 180.0) + (1.0F - Math.max(standing, eating)) * this.headParts.yRot;
		this.animateHeadPartsPlacement(eating, standing);
		this.body.xRot = standing * (float) (-Math.PI / 4) + iStanding * this.body.xRot;
		this.leftFrontLeg.y = this.leftFrontLeg.y - this.legStandingYOffset() * standing;
		this.leftFrontLeg.z = this.leftFrontLeg.z + this.legStandingZOffset() * standing;
		this.rightFrontLeg.y = this.leftFrontLeg.y;
		this.rightFrontLeg.z = this.leftFrontLeg.z;
		float standAngle = this.legStandAngle() * standing;
		float bobValue = Mth.cos(state.ageInTicks * 0.6F + (float) Math.PI);
		float legStandingXRotOffset = this.legStandingXRotOffset();
		float rlegRot = (legStandingXRotOffset + bobValue) * standing + legXRotAnim * iStanding;
		float llegRot = (legStandingXRotOffset - bobValue) * standing - legXRotAnim * iStanding;
		this.leftHindLeg.xRot = standAngle - legAnim1 * 0.5F * animationSpeed * iStanding;
		this.rightHindLeg.xRot = standAngle + legAnim1 * 0.5F * animationSpeed * iStanding;
		this.leftFrontLeg.xRot = rlegRot;
		this.rightFrontLeg.xRot = llegRot;
		this.offsetLegPositionWhenStanding(standing);
		float ageScale = state.ageScale;
		this.tail.xRot = this.tailXRotOffset() + (float) (Math.PI / 6) + animationSpeed * 0.75F;
		this.tail.y += animationSpeed * ageScale;
		this.tail.z += animationSpeed * 2.0F * ageScale;
		if (state.animateTail) {
			this.tail.yRot = Mth.cos(state.ageInTicks * 0.7F);
		} else {
			this.tail.yRot = 0.0F;
		}
	}

	void offsetLegPositionWhenStanding(float standing) {
	}

	float legStandAngle() {
		return (float) (Math.PI / 12);
	}

	abstract float legStandingYOffset();

	abstract float legStandingZOffset();

	float legStandingXRotOffset() {
		return (float) (-Math.PI / 3);
	}

	abstract float tailXRotOffset();

	abstract void animateHeadPartsPlacement(float eating, float standing);
}
