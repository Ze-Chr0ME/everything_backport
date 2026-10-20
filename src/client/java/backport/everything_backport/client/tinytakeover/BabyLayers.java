package backport.everything_backport.client.tinytakeover;

import backport.everything_backport.EverythingBackport;
import java.util.Map;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;

/** Puts the 26.1 baby meshes in place of the shrunken adults 1.21.7 bakes for its baby model layers. */
public final class BabyLayers {
	/** Per-slot baby armor (head, chest, legs, feet) for zombies, husks, drowned and zombie villagers. */
	public static final ModelLayerLocation[] HUMANOID_BABY_ARMOR = armor("humanoid_baby");
	/** Per-slot baby armor (head, chest, legs, feet) for piglins and zombified piglins. */
	public static final ModelLayerLocation[] PIGLIN_BABY_ARMOR = armor("piglin_baby");

	private static final CubeDeformation BABY_OUTER_ARMOR_DEFORMATION = new CubeDeformation(-0.1F, 0.5F, 0.3F);
	private static final CubeDeformation BABY_INNER_ARMOR_DEFORMATION = new CubeDeformation(-0.1F, 0.3F, 0.3F);
	private static final CubeDeformation BABY_PIGLIN_ARMOR_DEFORMATION = new CubeDeformation(0.7F);
	private static final PartPose BABY_PIGLIN_ARMOR_ARM_OFFSET = PartPose.offset(0.5F, -0.5F, 0.0F);

	private BabyLayers() {
	}

	private static ModelLayerLocation layer(String name, String layer) {
		return new ModelLayerLocation(EverythingBackport.id(name), layer);
	}

	private static ModelLayerLocation[] armor(String name) {
		return new ModelLayerLocation[] {layer(name, "armor_head"), layer(name, "armor_chest"), layer(name, "armor_legs"), layer(name, "armor_feet")};
	}

	/** Replaces and adds layer definitions in the map {@code LayerDefinitions.createRoots} builds. */
	public static void apply(Map<ModelLayerLocation, LayerDefinition> roots) {
		roots.put(ModelLayers.ARMADILLO_BABY, BabyMeshes.armadillo());
		roots.put(ModelLayers.AXOLOTL_BABY, BabyMeshes.axolotl());
		roots.put(ModelLayers.BEE_BABY, BabyMeshes.bee());
		roots.put(ModelLayers.CAMEL_BABY, BabyMeshes.camel());
		LayerDefinition feline = BabyMeshes.feline();
		roots.put(ModelLayers.CAT_BABY, feline);
		roots.put(ModelLayers.CAT_BABY_COLLAR, feline.apply(MeshTransformer.scaling(1.01F)));
		roots.put(ModelLayers.OCELOT_BABY, feline);
		LayerDefinition chicken = BabyMeshes.chicken();
		roots.put(ModelLayers.CHICKEN_BABY, chicken);
		roots.put(ModelLayers.COLD_CHICKEN_BABY, chicken);
		LayerDefinition cow = BabyMeshes.cow();
		roots.put(ModelLayers.COW_BABY, cow);
		roots.put(ModelLayers.COLD_COW_BABY, cow);
		roots.put(ModelLayers.WARM_COW_BABY, cow);
		roots.put(ModelLayers.MOOSHROOM_BABY, cow);
		roots.put(ModelLayers.DOLPHIN_BABY, BabyMeshes.dolphin());
		LayerDefinition donkey = BabyMeshes.donkey();
		roots.put(ModelLayers.DONKEY_BABY, donkey);
		roots.put(ModelLayers.MULE_BABY, donkey);
		LayerDefinition zombie = BabyMeshes.zombie(CubeDeformation.NONE);
		roots.put(ModelLayers.ZOMBIE_BABY, zombie);
		roots.put(ModelLayers.HUSK_BABY, zombie);
		roots.put(ModelLayers.DROWNED_BABY, zombie);
		roots.put(ModelLayers.DROWNED_BABY_OUTER_LAYER, BabyMeshes.zombie(new CubeDeformation(0.25F)));
		roots.put(ModelLayers.FOX_BABY, BabyMeshes.fox());
		LayerDefinition squid = BabyMeshes.squid();
		roots.put(ModelLayers.SQUID_BABY, squid);
		roots.put(ModelLayers.GLOW_SQUID_BABY, squid);
		roots.put(ModelLayers.GOAT_BABY, BabyMeshes.goat());
		LayerDefinition hoglin = BabyMeshes.hoglin();
		roots.put(ModelLayers.HOGLIN_BABY, hoglin);
		roots.put(ModelLayers.ZOGLIN_BABY, hoglin);
		LayerDefinition horse = BabyMeshes.horse();
		roots.put(ModelLayers.HORSE_BABY, horse);
		roots.put(ModelLayers.SKELETON_HORSE_BABY, horse);
		roots.put(ModelLayers.ZOMBIE_HORSE_BABY, horse);
		LayerDefinition llama = BabyMeshes.llama(CubeDeformation.NONE);
		roots.put(ModelLayers.LLAMA_BABY, llama);
		roots.put(ModelLayers.TRADER_LLAMA_BABY, llama);
		roots.put(ModelLayers.LLAMA_BABY_DECOR, BabyMeshes.llama(new CubeDeformation(0.2F)));
		roots.put(ModelLayers.PANDA_BABY, BabyMeshes.panda());
		LayerDefinition pig = BabyMeshes.pig(CubeDeformation.NONE);
		roots.put(ModelLayers.PIG_BABY, pig);
		roots.put(ModelLayers.COLD_PIG_BABY, pig);
		LayerDefinition piglin = BabyMeshes.piglin();
		roots.put(ModelLayers.PIGLIN_BABY, piglin);
		roots.put(ModelLayers.ZOMBIFIED_PIGLIN_BABY, piglin);
		roots.put(ModelLayers.POLAR_BEAR_BABY, BabyMeshes.polarBear());
		roots.put(ModelLayers.RABBIT, BabyMeshes.adultRabbit());
		roots.put(ModelLayers.RABBIT_BABY, BabyMeshes.babyRabbit());
		LayerDefinition sheep = BabyMeshes.sheep();
		roots.put(ModelLayers.SHEEP_BABY, sheep);
		roots.put(ModelLayers.SHEEP_BABY_WOOL, sheep);
		roots.put(ModelLayers.SHEEP_BABY_WOOL_UNDERCOAT, sheep);
		roots.put(ModelLayers.SNIFFER_BABY, BabyMeshes.snifflet());
		roots.put(ModelLayers.STRIDER_BABY, BabyMeshes.strider());
		roots.put(ModelLayers.TURTLE_BABY, BabyMeshes.turtle());
		roots.put(ModelLayers.VILLAGER_BABY, BabyMeshes.villager());
		roots.put(ModelLayers.WOLF_BABY, BabyMeshes.wolf());
		roots.put(ModelLayers.ZOMBIE_VILLAGER_BABY, BabyMeshes.zombieVillager());
		putAll(roots, HUMANOID_BABY_ARMOR, BabyMeshes.armorSet(BABY_INNER_ARMOR_DEFORMATION, BABY_OUTER_ARMOR_DEFORMATION, PartPose.ZERO, false));
		putAll(roots, PIGLIN_BABY_ARMOR, BabyMeshes.armorSet(BABY_PIGLIN_ARMOR_DEFORMATION, BABY_PIGLIN_ARMOR_DEFORMATION, BABY_PIGLIN_ARMOR_ARM_OFFSET, true));
	}

	private static void putAll(Map<ModelLayerLocation, LayerDefinition> roots, ModelLayerLocation[] locations, LayerDefinition[] layers) {
		for (int i = 0; i < locations.length; i++) {
			roots.put(locations[i], layers[i]);
		}
	}
}
