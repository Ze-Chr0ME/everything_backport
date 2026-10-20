package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.GoatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.GoatRenderState;

/** 26.1's kid holds its head lowered unless it is ramming. */
public class BabyGoatModel extends GoatModel {
	public BabyGoatModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(GoatRenderState state) {
		super.setupAnim(state);
		if (state.rammingXHeadRot == 0.0F) {
			this.head.xRot = (float) (Math.PI / 8);
		}
	}
}
