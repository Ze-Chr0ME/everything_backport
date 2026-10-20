package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.util.Mth;

/** 26.1's foal, used for horses, skeleton horses and zombie horses. */
public class BabyHorseModel extends HorseModel {
	private final BabyEquineAnimation animation;

	public BabyHorseModel(ModelPart root) {
		super(root);
		ModelPart body = root.getChild("body");
		this.animation = new BabyEquineAnimation(
			body,
			root.getChild("head_parts"),
			root.getChild("right_hind_leg"),
			root.getChild("left_hind_leg"),
			root.getChild("right_front_leg"),
			root.getChild("left_front_leg"),
			body.getChild("tail")
		) {
			@Override
			float legStandingYOffset() {
				return 4.0F;
			}

			@Override
			float legStandingZOffset() {
				return 0.0F;
			}

			@Override
			float tailXRotOffset() {
				return (float) (-Math.PI / 2);
			}

			@Override
			void animateHeadPartsPlacement(float eating, float standing) {
				this.headParts.y = this.headParts.y + Mth.lerp(eating, Mth.lerp(standing, 0.0F, -2.0F), 2.0F);
				this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0F);
			}
		};
	}

	@Override
	public void setupAnim(EquineRenderState state) {
		this.resetPose();
		this.animation.setupAnim(state);
	}
}
