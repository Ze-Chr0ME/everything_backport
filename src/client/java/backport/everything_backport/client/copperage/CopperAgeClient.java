package backport.everything_backport.client.copperage;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.client.copperage.renderer.CopperGolemRenderer;
import backport.everything_backport.client.copperage.renderer.CopperGolemStatueBlockRenderer;
import backport.everything_backport.client.copperage.renderer.CopperGolemStatueSpecialRenderer;
import backport.everything_backport.client.copperage.renderer.ShelfRenderer;
import backport.everything_backport.copperage.CopperAgeBlockEntities;
import backport.everything_backport.copperage.CopperAgeBlocks;
import backport.everything_backport.copperage.CopperAgeEntities;
import backport.everything_backport.copperage.CopperAgeParticles;
import backport.everything_backport.copperage.block.CopperChestBlock;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import backport.everything_backport.copperage.block.WeatheringCopperBlocks;
import java.util.List;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialBlockRendererRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.world.level.block.Block;

public final class CopperAgeClient {
	private CopperAgeClient() {
	}

	public static void init() {
		CopperAgeModelLayers.init();
		EntityRendererRegistry.register(CopperAgeEntities.COPPER_GOLEM, CopperGolemRenderer::new);
		BlockEntityRenderers.register(CopperAgeBlockEntities.COPPER_GOLEM_STATUE, CopperGolemStatueBlockRenderer::new);
		BlockEntityRenderers.register(CopperAgeBlockEntities.SHELF, ShelfRenderer::new);
		ParticleFactoryRegistry.getInstance().register(CopperAgeParticles.COPPER_FIRE_FLAME, FlameParticle.Provider::new);
		SpecialModelRenderers.ID_MAPPER.put(EverythingBackport.vanilla("copper_golem_statue"), CopperGolemStatueSpecialRenderer.Unbaked.MAP_CODEC);

		for (Block chest : CopperAgeBlocks.COPPER_CHESTS) {
			String texture = switch (((CopperChestBlock)chest).getState()) {
				case UNAFFECTED -> "copper";
				case EXPOSED -> "copper_exposed";
				case WEATHERED -> "copper_weathered";
				case OXIDIZED -> "copper_oxidized";
			};
			SpecialBlockRendererRegistry.register(chest, new ChestSpecialRenderer.Unbaked(EverythingBackport.vanilla(texture)));
		}

		for (Block statue : CopperAgeBlocks.COPPER_GOLEM_STATUES) {
			SpecialBlockRendererRegistry.register(
				statue,
				new CopperGolemStatueSpecialRenderer.Unbaked(((CopperGolemStatueBlock)statue).getWeatheringState(), CopperGolemStatueBlock.Pose.STANDING)
			);
		}

		for (WeatheringCopperBlocks blocks : List.of(CopperAgeBlocks.COPPER_BARS, CopperAgeBlocks.COPPER_CHAIN)) {
			blocks.forEach(block -> BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT_MIPPED));
		}

		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, CopperAgeBlocks.COPPER_TORCH, CopperAgeBlocks.COPPER_WALL_TORCH);
		CopperAgeBlocks.COPPER_LANTERN.forEach(block -> BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT));
	}
}
