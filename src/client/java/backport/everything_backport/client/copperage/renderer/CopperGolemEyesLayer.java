package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.model.CopperGolemModel;
import backport.everything_backport.copperage.entity.CopperGolemOxidationLevels;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Glowing eyes that follow the golem's oxidation level. */
public class CopperGolemEyesLayer extends RenderLayer<CopperGolemRenderState, CopperGolemModel> {
	public CopperGolemEyesLayer(RenderLayerParent<CopperGolemRenderState, CopperGolemModel> renderLayerParent) {
		super(renderLayerParent);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CopperGolemRenderState renderState, float yRot, float xRot) {
		if (!renderState.isInvisible) {
			var texture = CopperGolemOxidationLevels.getOxidationLevel(renderState.weathering).eyeTexture();
			this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.eyes(texture)), light, OverlayTexture.NO_OVERLAY);
		}
	}
}
