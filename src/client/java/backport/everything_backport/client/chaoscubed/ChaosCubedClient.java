package backport.everything_backport.client.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosBlocks;
import backport.everything_backport.chaoscubed.ChaosEntities;
import backport.everything_backport.chaoscubed.ChaosParticles;
import backport.everything_backport.client.chaoscubed.model.SulfurCubeRenderer;
import backport.everything_backport.client.chaoscubed.particle.GeyserBaseParticle;
import backport.everything_backport.client.chaoscubed.particle.GeyserEruptionParticle;
import backport.everything_backport.client.chaoscubed.particle.GeyserPlumeParticle;
import backport.everything_backport.client.chaoscubed.particle.NoxiousGasCloudParticle;
import backport.everything_backport.client.chaoscubed.particle.NoxiousGasParticle;
import backport.everything_backport.client.chaoscubed.particle.SulfurBubbleParticle;
import backport.everything_backport.client.chaoscubed.particle.SulfurCubeGooParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

/** Client side of Chaos Cubed (26.2). */
public final class ChaosCubedClient {
	private ChaosCubedClient() {
	}

	public static void init() {
		ChaosModelLayers.init();
		EntityRendererRegistry.register(ChaosEntities.SULFUR_CUBE, SulfurCubeRenderer::new);
		ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
		particles.register(ChaosParticles.SULFUR_BUBBLES, SulfurBubbleParticle.Provider::new);
		particles.register(ChaosParticles.NOXIOUS_GAS, NoxiousGasParticle.Provider::new);
		particles.register(ChaosParticles.NOXIOUS_GAS_CLOUD, new NoxiousGasCloudParticle.Provider());
		particles.register(ChaosParticles.GEYSER, new GeyserEruptionParticle.Provider());
		particles.register(ChaosParticles.GEYSER_BASE, GeyserBaseParticle.Provider::new);
		particles.register(ChaosParticles.GEYSER_POOF, GeyserBaseParticle.Provider::new);
		particles.register(ChaosParticles.GEYSER_PLUME, GeyserPlumeParticle.Provider::new);
		particles.register(ChaosParticles.SULFUR_CUBE_GOO, SulfurCubeGooParticle.Provider::new);
		BlockRenderLayerMap.putBlock(ChaosBlocks.SULFUR_SPIKE, ChunkSectionLayer.CUTOUT);
	}
}
