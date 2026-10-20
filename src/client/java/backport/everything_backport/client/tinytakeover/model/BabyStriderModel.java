package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.StriderRenderState;
import net.minecraft.util.Mth;

/** 26.1's baby strider: a short body bobbing on its legs with three bristles along its back. */
public class BabyStriderModel extends StriderModel {
	private final ModelPart body;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart frontBristle;
	private final ModelPart middleBristle;
	private final ModelPart bottomBristle;

	public BabyStriderModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
		this.frontBristle = this.body.getChild("bristle2");
		this.middleBristle = this.body.getChild("bristle1");
		this.bottomBristle = this.body.getChild("bristle0");
	}

	@Override
	public void setupAnim(StriderRenderState state) {
		super.setupAnim(state);
		float animationPos = state.walkAnimationPos;
		float animationSpeed = Math.min(state.walkAnimationSpeed, 0.25F);
		float ageInTicks = state.ageInTicks;
		this.body.y = 17.25F - 1.0F * Mth.cos(animationPos * 1.5F) * 2.0F * animationSpeed;
		this.leftLeg.y = 20.0F + 2.0F * Mth.sin(animationPos * 1.5F * 0.5F + (float) Math.PI) * 2.0F * animationSpeed;
		this.rightLeg.y = 20.0F + 2.0F * Mth.sin(animationPos * 1.5F * 0.5F) * 2.0F * animationSpeed;
		float bristleFlow = Mth.cos(animationPos * 1.5F + (float) Math.PI) * animationSpeed;
		this.frontBristle.xRot += bristleFlow * 0.6F + 0.1F * Mth.sin(ageInTicks * 0.4F);
		this.middleBristle.xRot += bristleFlow * 1.2F + 0.1F * Mth.sin(ageInTicks * 0.2F);
		this.bottomBristle.xRot += bristleFlow * 1.3F + 0.05F * Mth.sin(ageInTicks * -0.4F);
	}
}
