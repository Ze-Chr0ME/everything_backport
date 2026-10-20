package backport.everything_backport.client.wildernessbound;

import backport.everything_backport.wildernessbound.WildBlocks;
import backport.everything_backport.wildernessbound.WildEntities;
import backport.everything_backport.wildernessbound.WildParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.BoatRenderer;

/** Client side of Wilderness Bound (26.3). */
public final class WildernessBoundClient {
	private WildernessBoundClient() {
	}

	public static void init() {
		WildModelLayers.init();
		EntityRendererRegistry.register(WildEntities.CUSHION, CushionRenderer::new);
		EntityRendererRegistry.register(WildEntities.POPLAR_BOAT, context -> new BoatRenderer(context, WildModelLayers.POPLAR_BOAT));
		EntityRendererRegistry.register(WildEntities.POPLAR_CHEST_BOAT, context -> new BoatRenderer(context, WildModelLayers.POPLAR_CHEST_BOAT));
		// 26.3's poplar leaf particles fall like pale oak leaves.
		ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
		particles.register(WildParticles.RED_POPLAR_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
		particles.register(WildParticles.ORANGE_POPLAR_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
		particles.register(WildParticles.YELLOW_POPLAR_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
		BlockRenderLayerMap.putBlocks(
			ChunkSectionLayer.CUTOUT,
			WildBlocks.POPLAR_SAPLING,
			WildBlocks.POTTED_POPLAR_SAPLING,
			WildBlocks.POPLAR_DOOR,
			WildBlocks.POPLAR_TRAPDOOR,
			WildBlocks.RED_SHRUB,
			WildBlocks.SHELF_MUSHROOM,
			WildBlocks.STRAW_BED
		);
		BlockRenderLayerMap.putBlocks(
			ChunkSectionLayer.CUTOUT_MIPPED, WildBlocks.RED_POPLAR_LEAVES, WildBlocks.ORANGE_POPLAR_LEAVES, WildBlocks.YELLOW_POPLAR_LEAVES
		);
	}
}
