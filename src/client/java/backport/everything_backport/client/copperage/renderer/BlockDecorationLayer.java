package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Renders the block worn on the copper golem's antenna (1.21.9). */
public class BlockDecorationLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
	private final Function<S, Optional<BlockState>> blockState;
	private final Consumer<PoseStack> transform;

	public BlockDecorationLayer(RenderLayerParent<S, M> renderLayerParent, Function<S, Optional<BlockState>> blockState, Consumer<PoseStack> transform) {
		super(renderLayerParent);
		this.blockState = blockState;
		this.transform = transform;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, S renderState, float yRot, float xRot) {
		Optional<BlockState> optional = this.blockState.apply(renderState);
		if (optional.isPresent()) {
			BlockState state = optional.get();
			Block block = state.getBlock();
			boolean statue = block instanceof CopperGolemStatueBlock;
			poseStack.pushPose();
			this.transform.accept(poseStack);
			if (!statue) {
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}

			if (statue || block instanceof AbstractSkullBlock || block instanceof AbstractBannerBlock || block instanceof AbstractChestBlock) {
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			}

			if (block instanceof FlowerBedBlock) {
				poseStack.translate(-0.25, -1.5, -0.25);
			} else if (!statue) {
				poseStack.translate(-0.5, -1.5, -0.5);
			} else {
				poseStack.translate(-0.5, 0.0, -0.5);
			}

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
	}
}
