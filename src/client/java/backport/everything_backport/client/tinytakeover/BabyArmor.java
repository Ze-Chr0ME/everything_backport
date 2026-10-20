package backport.everything_backport.client.tinytakeover;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * 26.1's baby armor: babies of the zombie and piglin families wear per-slot armor shaped for the new baby meshes, with
 * the textures from {@code textures/entity/equipment/humanoid_baby/}. Only the vanilla armor materials have those
 * textures, so other armor isn't drawn on these babies, and trims are skipped, as in 26.1.
 */
public final class BabyArmor {
	private static final Set<ResourceKey<EquipmentAsset>> BABY_TEXTURED = Set.of(
		EquipmentAssets.LEATHER,
		EquipmentAssets.CHAINMAIL,
		EquipmentAssets.IRON,
		EquipmentAssets.GOLD,
		EquipmentAssets.DIAMOND,
		EquipmentAssets.NETHERITE,
		EquipmentAssets.TURTLE_SCUTE,
		ResourceKey.create(EquipmentAssets.ROOT_ID, ResourceLocation.withDefaultNamespace("copper"))
	);
	private static final String ADULT_FOLDER = "textures/entity/equipment/humanoid/";
	private static final String BABY_FOLDER = "textures/entity/equipment/humanoid_baby/";

	/** Set while a baby armor piece renders, so the equipment renderer picks baby textures and skips trims. */
	public static boolean rendering;

	private static EntityModelSet bakedFrom;
	private static Model[] humanoid;
	private static Model[] piglin;

	private BabyArmor() {
	}

	/** The baby armor model for this slot, or null when the entity keeps 1.21.7's armor. */
	public static Model model(HumanoidRenderState state, EquipmentSlot slot) {
		if (!state.isBaby || slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
			return null;
		}

		EntityType<?> type = state.entityType;
		boolean isPiglin = type == EntityType.PIGLIN || type == EntityType.ZOMBIFIED_PIGLIN;
		if (!isPiglin && type != EntityType.ZOMBIE && type != EntityType.HUSK && type != EntityType.DROWNED && type != EntityType.ZOMBIE_VILLAGER) {
			return null;
		}

		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		if (models != bakedFrom) {
			bakedFrom = models;
			humanoid = bake(models, BabyLayers.HUMANOID_BABY_ARMOR);
			piglin = bake(models, BabyLayers.PIGLIN_BABY_ARMOR);
		}

		return (isPiglin ? piglin : humanoid)[3 - slot.getIndex()];
	}

	private static Model[] bake(EntityModelSet models, ModelLayerLocation[] layers) {
		Model[] baked = new Model[layers.length];
		for (int i = 0; i < layers.length; i++) {
			baked[i] = new Model(models.bakeLayer(layers[i]));
		}

		return baked;
	}

	public static boolean hasBabyTexture(ResourceKey<EquipmentAsset> asset) {
		return BABY_TEXTURED.contains(asset);
	}

	public static ResourceLocation babyTexture(ResourceLocation texture) {
		String path = texture.getPath();
		return path.startsWith(ADULT_FOLDER) ? texture.withPath(BABY_FOLDER + path.substring(ADULT_FOLDER.length())) : texture;
	}

	/**
	 * Poses the armor like the wearer. The armor meshes have their own rest offsets, so each part moves by how far the
	 * wearer's part moved from its rest pose rather than copying the pose outright.
	 */
	public static void copyPose(HumanoidModel<?> wearer, HumanoidModel<?> armor) {
		copyPose(wearer.head, armor.head);
		copyPose(wearer.body, armor.body);
		copyPose(wearer.rightArm, armor.rightArm);
		copyPose(wearer.leftArm, armor.leftArm);
		copyPose(wearer.rightLeg, armor.rightLeg);
		copyPose(wearer.leftLeg, armor.leftLeg);
	}

	private static void copyPose(ModelPart from, ModelPart to) {
		PartPose fromRest = from.getInitialPose();
		PartPose toRest = to.getInitialPose();
		to.x = toRest.x() + from.x - fromRest.x();
		to.y = toRest.y() + from.y - fromRest.y();
		to.z = toRest.z() + from.z - fromRest.z();
		to.xRot = toRest.xRot() + from.xRot - fromRest.xRot();
		to.yRot = toRest.yRot() + from.yRot - fromRest.yRot();
		to.zRot = toRest.zRot() + from.zRot - fromRest.zRot();
		to.xScale = from.xScale;
		to.yScale = from.yScale;
		to.zScale = from.zScale;
	}

	/** One slot of baby armor. */
	public static final class Model extends HumanoidModel<HumanoidRenderState> {
		Model(ModelPart root) {
			super(root);
		}
	}
}
