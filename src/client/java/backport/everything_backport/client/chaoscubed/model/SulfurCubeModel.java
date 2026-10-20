package backport.everything_backport.client.chaoscubed.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/** 26.2's sulfur cube: an outer translucent shell around an inner jelly cube, or around the block it swallowed. */
public class SulfurCubeModel extends EntityModel<EntityRenderState> {
	public SulfurCubeModel(ModelPart root) {
		super(root, RenderType::entityTranslucent);
	}

	public static LayerDefinition createOuterBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 18.0F, 18.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 128, 128);
	}

	public static LayerDefinition createInnerBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 36).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 128, 128);
	}

	/** The baby sulfur cube's shell. */
	public static LayerDefinition createSmallOuterBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 64);
	}

	/** The baby sulfur cube's jelly. */
	public static LayerDefinition createSmallInnerBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		return LayerDefinition.create(mesh, 64, 64);
	}
}
