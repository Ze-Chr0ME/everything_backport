package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.CatRenderState;

/** 26.1's kitten, which poses differently from a shrunken cat. */
public class BabyCatModel extends CatModel {
	public BabyCatModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(CatRenderState state) {
		this.resetPose();
		BabyFelineAnimation.setupAnim(
			state, this.head, this.body, this.tail1, this.tail2, this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg
		);
	}
}
