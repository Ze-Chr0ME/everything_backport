package backport.everything_backport.client.wildernessbound;

import backport.everything_backport.EverythingBackport;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class WildModelLayers {
	public static final ModelLayerLocation CUSHION = create("cushion");
	public static final ModelLayerLocation POPLAR_BOAT = create("boat/poplar");
	public static final ModelLayerLocation POPLAR_CHEST_BOAT = create("chest_boat/poplar");

	private WildModelLayers() {
	}

	private static ModelLayerLocation create(String name) {
		return new ModelLayerLocation(EverythingBackport.vanilla(name), "main");
	}

	static void init() {
		EntityModelLayerRegistry.registerModelLayer(CUSHION, CushionModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(POPLAR_BOAT, BoatModel::createBoatModel);
		EntityModelLayerRegistry.registerModelLayer(POPLAR_CHEST_BOAT, BoatModel::createChestBoatModel);
	}
}
