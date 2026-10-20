package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PolarBearRenderState;

/**
 * 26.1's cub stands up like an adult: 1.21.7 shrinks the standing head offset to 4/9 for its half-size baby, which the
 * new full-detail cub doesn't need.
 */
public class BabyPolarBearModel extends PolarBearModel {
	private static final float OLD_BABY_HEAD_FACTOR = 0.44444445F;

	public BabyPolarBearModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(PolarBearRenderState state) {
		super.setupAnim(state);
		float standScale = state.standScale * state.standScale;
		float missing = 1.0F - (state.isBaby ? OLD_BABY_HEAD_FACTOR : 1.0F);
		this.head.y -= standScale * missing * 24.0F;
		this.head.z += standScale * missing * 13.0F;
	}
}
