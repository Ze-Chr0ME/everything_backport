package backport.everything_backport.client.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.client.mountsofmayhem.model.NautilusArmorModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusSaddleModel;
import backport.everything_backport.client.mountsofmayhem.model.ZombieNautilusCoralModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.AbstractEquineModel;
import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.CamelSaddleModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class MountsModelLayers {
	public static final ModelLayerLocation NAUTILUS = create("nautilus", "main");
	public static final ModelLayerLocation NAUTILUS_BABY = create("nautilus_baby", "main");
	public static final ModelLayerLocation NAUTILUS_SADDLE = create("nautilus", "saddle");
	public static final ModelLayerLocation NAUTILUS_ARMOR = create("nautilus_armor", "main");
	public static final ModelLayerLocation ZOMBIE_NAUTILUS = create("zombie_nautilus", "main");
	public static final ModelLayerLocation ZOMBIE_NAUTILUS_CORAL = create("zombie_nautilus_coral", "main");
	public static final ModelLayerLocation CAMEL_HUSK_SADDLE = create("camel_husk", "saddle");
	public static final ModelLayerLocation CAMEL_HUSK_BABY_SADDLE = create("camel_husk_baby", "saddle");
	public static final ModelLayerLocation PARCHED = create("parched", "main");
	public static final ModelLayerLocation UNDEAD_HORSE_ARMOR = create("undead_horse_armor", "main");
	public static final ModelLayerLocation UNDEAD_HORSE_BABY_ARMOR = create("undead_horse_baby_armor", "main");

	private MountsModelLayers() {
	}

	private static ModelLayerLocation create(String name, String layer) {
		return new ModelLayerLocation(EverythingBackport.vanilla(name), layer);
	}

	static void init() {
		EntityModelLayerRegistry.registerModelLayer(NAUTILUS, NautilusModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(NAUTILUS_BABY, NautilusModel::createBabyBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(NAUTILUS_SADDLE, NautilusSaddleModel::createSaddleLayer);
		EntityModelLayerRegistry.registerModelLayer(NAUTILUS_ARMOR, NautilusArmorModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ZOMBIE_NAUTILUS, NautilusModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ZOMBIE_NAUTILUS_CORAL, ZombieNautilusCoralModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(CAMEL_HUSK_SADDLE, CamelSaddleModel::createSaddleLayer);
		EntityModelLayerRegistry.registerModelLayer(CAMEL_HUSK_BABY_SADDLE, () -> CamelSaddleModel.createSaddleLayer().apply(CamelModel.BABY_TRANSFORMER));
		EntityModelLayerRegistry.registerModelLayer(PARCHED, MountsModelLayers::createParchedLayer);
		EntityModelLayerRegistry.registerModelLayer(
			UNDEAD_HORSE_ARMOR, () -> LayerDefinition.create(AbstractEquineModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64)
		);
		EntityModelLayerRegistry.registerModelLayer(
			UNDEAD_HORSE_BABY_ARMOR, () -> LayerDefinition.create(AbstractEquineModel.createBabyMesh(new CubeDeformation(0.1F)), 64, 64)
		);
	}

	/** 1.21.11's {@code SkeletonModel#createSingleModelDualBodyLayer}: a skeleton with its outer layer on the same texture. */
	private static LayerDefinition createParchedLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition root = meshDefinition.getRoot();
		root.addOrReplaceChild(
			"body",
			CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
				.texOffs(28, 0)
				.addBox(-4.0F, 10.0F, -2.0F, 8.0F, 1.0F, 4.0F)
				.texOffs(16, 48)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.025F)),
			PartPose.offset(0.0F, 0.0F, 0.0F)
		);
		root.addOrReplaceChild(
				"head",
				CubeListBuilder.create()
					.texOffs(0, 0)
					.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
					.texOffs(0, 32)
					.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)),
				PartPose.offset(0.0F, 0.0F, 0.0F)
			)
			.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild(
			"right_arm",
			CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(42, 33).addBox(-1.55F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(-5.5F, 2.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"left_arm",
			CubeListBuilder.create().texOffs(56, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(40, 48).addBox(-1.45F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(5.5F, 2.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"right_leg",
			CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(0, 49).addBox(-1.5F, -0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(-2.0F, 12.0F, 0.0F)
		);
		root.addOrReplaceChild(
			"left_leg",
			CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(4, 49).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(2.0F, 12.0F, 0.0F)
		);
		return LayerDefinition.create(meshDefinition, 64, 64);
	}
}
