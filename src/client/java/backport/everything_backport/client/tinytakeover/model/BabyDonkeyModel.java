package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.DonkeyModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
import net.minecraft.util.Mth;

/**
 * 26.1's donkey and mule foals. Their legs, neck and tail hang off the body, so the root only holds empty
 * placeholders for the parts 1.21.7's donkey model looks up.
 */
public class BabyDonkeyModel extends DonkeyModel {
	private final ModelPart headParts;
	private final ModelPart leftChest;
	private final ModelPart rightChest;
	private final BabyEquineAnimation animation;

	public BabyDonkeyModel(ModelPart root) {
		super(root);
		ModelPart body = root.getChild("body");
		this.headParts = body.getChild("head_parts");
		this.leftChest = body.getChild("left_chest");
		this.rightChest = body.getChild("right_chest");
		this.animation = new BabyEquineAnimation(
			body,
			this.headParts,
			body.getChild("right_hind_leg"),
			body.getChild("left_hind_leg"),
			body.getChild("right_front_leg"),
			body.getChild("left_front_leg"),
			body.getChild("tail")
		) {
			@Override
			void offsetLegPositionWhenStanding(float standing) {
				this.leftHindLeg.y = Mth.lerp(standing, this.leftHindLeg.y, -0.3F);
				this.rightHindLeg.y = Mth.lerp(standing, this.leftHindLeg.y, -0.3F);
			}

			@Override
			float legStandAngle() {
				return (float) (Math.PI / 3);
			}

			@Override
			float legStandingYOffset() {
				return 1.0F;
			}

			@Override
			float legStandingZOffset() {
				return 0.5F;
			}

			@Override
			float legStandingXRotOffset() {
				return 0.0F;
			}

			@Override
			float tailXRotOffset() {
				return (float) (-Math.PI / 4);
			}

			@Override
			void animateHeadPartsPlacement(float eating, float standing) {
				this.headParts.y = Mth.lerp(eating, this.headParts.y, -1.2F);
				this.headParts.z = Mth.lerp(standing, this.headParts.z, -3.6F);
			}
		};
	}

	@Override
	public void setupAnim(DonkeyRenderState state) {
		this.resetPose();
		this.animation.setupAnim(state);
		this.leftChest.visible = state.hasChest;
		this.rightChest.visible = state.hasChest;
		// 26.1 keeps a foal's head bowed whatever it looks at.
		state.xRot = -30.0F;
		float headRotXRad = state.xRot * (float) (Math.PI / 180.0);
		float eating = state.eatAnimation;
		float standing = state.standAnimation;
		float baseHeadAngle = (1.0F - Math.max(standing, eating))
			* ((float) (Math.PI / 6) + headRotXRad + state.feedingAnimation * Mth.sin(state.ageInTicks) * 0.05F);
		this.headParts.xRot = standing * ((float) (Math.PI / 12) + headRotXRad) + eating * ((float) (Math.PI / 2) + Mth.sin(state.ageInTicks) * 0.05F) + baseHeadAngle;
	}
}
