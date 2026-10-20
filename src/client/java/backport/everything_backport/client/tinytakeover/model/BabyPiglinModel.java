package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;

/** 26.1's baby piglin rests its ears at 5 degrees instead of the adult's 30. */
public class BabyPiglinModel extends PiglinModel {
	static final float EAR_ANGLE_DIFFERENCE = (30.0F - 5.0F) * (float) (Math.PI / 180.0);

	public BabyPiglinModel(ModelPart root) {
		super(root);
	}

	@Override
	public void setupAnim(PiglinRenderState state) {
		super.setupAnim(state);
		if (state.armPose != PiglinArmPose.DANCING) {
			this.leftEar.zRot += EAR_ANGLE_DIFFERENCE;
			this.rightEar.zRot -= EAR_ANGLE_DIFFERENCE;
		}
	}
}
