package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.model.CopperGolemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.SkullBlock;

/** {@link CustomHeadLayer} for the copper golem, whose head hangs off its body (1.21.9's {@code translateToHead}). */
public class CopperGolemHeadLayer extends RenderLayer<CopperGolemRenderState, CopperGolemModel> {
	private final Function<SkullBlock.Type, SkullModelBase> skullModels;

	public CopperGolemHeadLayer(RenderLayerParent<CopperGolemRenderState, CopperGolemModel> renderLayerParent, EntityModelSet entityModelSet) {
		super(renderLayerParent);
		this.skullModels = Util.memoize(type -> SkullBlockRenderer.createModel(entityModelSet, type));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CopperGolemRenderState renderState, float yRot, float xRot) {
		if (!renderState.headItem.isEmpty() || renderState.wornHeadType != null) {
			poseStack.pushPose();
			CopperGolemModel model = this.getParentModel();
			model.root().translateAndRotate(poseStack);
			model.translateToHead(poseStack);
			if (renderState.wornHeadType != null) {
				poseStack.scale(1.1875F, -1.1875F, -1.1875F);
				poseStack.translate(-0.5, 0.0, -0.5);
				SkullBlock.Type type = renderState.wornHeadType;
				SkullModelBase skullModel = this.skullModels.apply(type);
				RenderType renderType = SkullBlockRenderer.getRenderType(type, renderState.wornHeadProfile);
				SkullBlockRenderer.renderSkull(null, 180.0F, renderState.wornHeadAnimationPos, poseStack, multiBufferSource, light, skullModel, renderType);
			} else {
				CustomHeadLayer.translateToHead(poseStack, CustomHeadLayer.Transforms.DEFAULT);
				renderState.headItem.render(poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
			}

			poseStack.popPose();
		}
	}
}
