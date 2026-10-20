package backport.everything_backport.client.tinytakeover.model;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.WolfRenderState;

/** 26.1's puppy: the adult wolf animation, with the head and tail rolling when shaking and a flatter sit. */
public class BabyWolfModel extends WolfModel {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart tail;

	public BabyWolfModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.tail = root.getChild("tail");
	}

	@Override
	public void setupAnim(WolfRenderState state) {
		super.setupAnim(state);
		this.head.zRot = state.headRollAngle + state.getBodyRollAngle(0.0F);
		this.tail.zRot = state.getBodyRollAngle(-0.2F);
		if (state.isSitting) {
			this.body.xRot -= (float) (Math.PI / 2);
		}
	}
}
