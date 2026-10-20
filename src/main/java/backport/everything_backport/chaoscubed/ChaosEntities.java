package backport.everything_backport.chaoscubed;

import backport.everything_backport.chaoscubed.block.PotentSulfurBlockEntity;
import backport.everything_backport.chaoscubed.entity.SulfurCube;
import backport.everything_backport.registry.Reg;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ChaosEntities {
	public static final EntityType<SulfurCube> SULFUR_CUBE = Reg.entity(
		"sulfur_cube",
		EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER).sized(0.49F, 0.49F).eyeHeight(0.175F).spawnDimensionsScale(2.0F).clientTrackingRange(10)
	);
	public static final BlockEntityType<PotentSulfurBlockEntity> POTENT_SULFUR = Reg.blockEntity(
		"potent_sulfur", PotentSulfurBlockEntity::new, ChaosBlocks.POTENT_SULFUR
	);

	private ChaosEntities() {
	}

	static void init() {
		FabricDefaultAttributeRegistry.register(SULFUR_CUBE, SulfurCube.createSulfurCubeAttributes());
		SpawnPlacements.register(SULFUR_CUBE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SulfurCube::checkSulfurCubeSpawnRules);
	}
}
