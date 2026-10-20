package backport.everything_backport.client.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.client.chaoscubed.model.SulfurCubeModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class ChaosModelLayers {
	public static final ModelLayerLocation SULFUR_CUBE = create("sulfur_cube", "main");
	public static final ModelLayerLocation SULFUR_CUBE_INNER = create("sulfur_cube", "inner");
	public static final ModelLayerLocation SULFUR_CUBE_SMALL = create("sulfur_cube_small", "main");
	public static final ModelLayerLocation SULFUR_CUBE_SMALL_INNER = create("sulfur_cube_small", "inner");

	private ChaosModelLayers() {
	}

	private static ModelLayerLocation create(String name, String layer) {
		return new ModelLayerLocation(EverythingBackport.vanilla(name), layer);
	}

	static void init() {
		EntityModelLayerRegistry.registerModelLayer(SULFUR_CUBE, SulfurCubeModel::createOuterBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(SULFUR_CUBE_INNER, SulfurCubeModel::createInnerBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(SULFUR_CUBE_SMALL, SulfurCubeModel::createSmallOuterBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(SULFUR_CUBE_SMALL_INNER, SulfurCubeModel::createSmallInnerBodyLayer);
	}
}
