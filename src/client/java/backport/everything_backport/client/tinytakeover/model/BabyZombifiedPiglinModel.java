package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.ZombifiedPiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;

/** 26.1's baby zombified piglin rests its ears at 5 degrees instead of the adult's 30. */
public class BabyZombifiedPiglinModel extends ZombifiedPiglinModel {
	public BabyZombifiedPiglinModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(ZombifiedPiglinRenderState state) {
		super.setupAnim(state);
		this.leftEar.zRot += BabyPiglinModel.EAR_ANGLE_DIFFERENCE;
		this.rightEar.zRot -= BabyPiglinModel.EAR_ANGLE_DIFFERENCE;
	}
}
