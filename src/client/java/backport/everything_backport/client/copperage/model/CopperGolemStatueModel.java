package backport.everything_backport.client.copperage.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

public class CopperGolemStatueModel extends Model {
	public CopperGolemStatueModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityCutoutNoCull);
	}

	public void setupAnim(Direction direction) {
		this.root.y = 0.0F;
		this.root.yRot = direction.getOpposite().toYRot() * (float) (Math.PI / 180.0);
		this.root.zRot = (float) Math.PI;
	}
}
