package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PandaRenderState;
import net.minecraft.util.Mth;

/** 26.1's panda cub, which sits upright on its haunches instead of rolling its body back like an adult. */
public class BabyPandaModel extends PandaModel {
	public BabyPandaModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(PandaRenderState state) {
		this.resetPose();
		this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
		this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
		float animationPos = state.walkAnimationPos;
		float animationSpeed = state.walkAnimationSpeed;
		this.rightHindLeg.xRot = Mth.cos(animationPos * 0.6662F) * 1.4F * animationSpeed;
		this.leftHindLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI) * 1.4F * animationSpeed;
		this.rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662F + (float) Math.PI) * 1.4F * animationSpeed;
		this.leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662F) * 1.4F * animationSpeed;
		if (state.isUnhappy) {
			this.head.yRot = 0.35F * Mth.sin(0.6F * state.ageInTicks);
			this.head.zRot = 0.35F * Mth.sin(0.6F * state.ageInTicks);
			this.rightFrontLeg.xRot = -0.75F * Mth.sin(0.3F * state.ageInTicks);
			this.leftFrontLeg.xRot = 0.75F * Mth.sin(0.3F * state.ageInTicks);
		} else {
			this.head.zRot = 0.0F;
		}

		if (state.isSneezing) {
			if (state.sneezeTime < 15) {
				this.head.xRot = (float) (-Math.PI / 4) * state.sneezeTime / 14.0F;
			} else if (state.sneezeTime < 20) {
				float internalSneezePos = (state.sneezeTime - 15) / 5;
				this.head.xRot = (float) (-Math.PI / 4) + (float) (Math.PI / 4) * internalSneezePos;
			}
		}

		if (state.sitAmount > 0.0F) {
			this.animateSitting(state);
			if (state.isEating) {
				this.head.xRot = (float) (Math.PI / 2) + 0.2F * Mth.sin(state.ageInTicks * 0.6F);
				this.rightFrontLeg.xRot = -0.4F - 0.2F * Mth.sin(state.ageInTicks * 0.6F);
				this.leftFrontLeg.xRot = -0.4F - 0.2F * Mth.sin(state.ageInTicks * 0.6F);
			}

			if (state.isScared) {
				this.head.xRot = 2.1707964F;
				this.rightFrontLeg.xRot = -0.9F;
				this.leftFrontLeg.xRot = -0.9F;
			}
		} else {
			this.rightHindLeg.zRot = 0.0F;
			this.leftHindLeg.zRot = 0.0F;
			this.rightFrontLeg.zRot = 0.0F;
			this.leftFrontLeg.zRot = 0.0F;
		}

		if (state.lieOnBackAmount > 0.0F) {
			this.rightHindLeg.xRot = -0.6F * Mth.sin(state.ageInTicks * 0.15F);
			this.leftHindLeg.xRot = 0.6F * Mth.sin(state.ageInTicks * 0.15F);
			this.rightFrontLeg.xRot = 0.3F * Mth.sin(state.ageInTicks * 0.25F);
			this.leftFrontLeg.xRot = -0.3F * Mth.sin(state.ageInTicks * 0.25F);
			this.head.xRot = Mth.rotLerpRad(state.lieOnBackAmount, this.head.xRot, (float) (Math.PI / 2));
		}

		if (state.rollAmount > 0.0F) {
			this.head.xRot = Mth.rotLerpRad(state.rollAmount, this.head.xRot, 2.0561945F);
			this.rightHindLeg.xRot = -0.5F * Mth.sin(state.ageInTicks * 0.5F);
			this.leftHindLeg.xRot = 0.5F * Mth.sin(state.ageInTicks * 0.5F);
			this.rightFrontLeg.xRot = 0.5F * Mth.sin(state.ageInTicks * 0.5F);
			this.leftFrontLeg.xRot = -0.5F * Mth.sin(state.ageInTicks * 0.5F);
		}
	}

	private void animateSitting(PandaRenderState state) {
		this.body.xRot = Mth.rotLerpRad(state.sitAmount, this.body.xRot, (float) (Math.PI / 18));
		this.body.z = Mth.lerp(state.sitAmount, this.body.z, -1.5F);
		this.head.z = Mth.lerp(state.sitAmount, this.head.z, -11.5F);
		this.head.y = Mth.lerp(state.sitAmount, this.head.y, 17.5F);
		this.rightFrontLeg.z = Mth.lerp(state.sitAmount, this.rightFrontLeg.z, -5.0F);
		this.leftFrontLeg.z = Mth.lerp(state.sitAmount, this.leftFrontLeg.z, -5.0F);
		this.rightHindLeg.z = Mth.lerp(state.sitAmount, this.rightHindLeg.z, 3.0F);
		this.leftHindLeg.z = Mth.lerp(state.sitAmount, this.leftHindLeg.z, 3.0F);
		this.rightFrontLeg.zRot = -0.27079642F;
		this.leftFrontLeg.zRot = 0.27079642F;
		this.rightHindLeg.zRot = 0.5707964F;
		this.leftHindLeg.zRot = -0.5707964F;
	}
}
