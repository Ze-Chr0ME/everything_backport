package backport.everything_backport.client.tinytakeover;

import backport.everything_backport.tinytakeover.TinyBlocks;
import backport.everything_backport.tinytakeover.TinyParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

/** Client side of Tiny Takeover (26.1). Baby models, textures and layers are swapped in by mixins. */
public final class TinyTakeoverClient {
	private TinyTakeoverClient() {
	}

	public static void init() {
		ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
		particles.register(TinyParticles.PAUSE_MOB_GROWTH, SimpleVerticalParticle.PauseMobGrowthProvider::new);
		particles.register(TinyParticles.RESET_MOB_GROWTH, SimpleVerticalParticle.ResetMobGrowthProvider::new);
		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, TinyBlocks.GOLDEN_DANDELION, TinyBlocks.POTTED_GOLDEN_DANDELION);
	}
}
