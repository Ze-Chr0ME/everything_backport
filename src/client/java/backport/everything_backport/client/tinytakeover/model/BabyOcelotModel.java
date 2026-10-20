package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FelineRenderState;

/** 26.1's baby ocelot, which poses differently from a shrunken ocelot. */
public class BabyOcelotModel extends OcelotModel {
	public BabyOcelotModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(FelineRenderState state) {
		this.resetPose();
		BabyFelineAnimation.setupAnim(
			state, this.head, this.body, this.tail1, this.tail2, this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg
		);
	}
}
