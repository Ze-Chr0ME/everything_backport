package backport.everything_backport.client.wildernessbound;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.wildernessbound.entity.Cushion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

/** 26.3's cushion renderer, drawn the 1.21.7 way. */
public class CushionRenderer extends EntityRenderer<Cushion, CushionRenderState> {
	private static final Map<DyeColor, ResourceLocation> TEXTURES_BY_COLOR = new EnumMap<>(DyeColor.class);
	private final CushionModel model;

	static {
		for (DyeColor color : DyeColor.values()) {
			TEXTURES_BY_COLOR.put(color, EverythingBackport.vanilla("textures/entity/cushion/" + color.getName() + "_cushion.png"));
		}
	}

	public CushionRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CushionModel(context.bakeLayer(WildModelLayers.CUSHION));
	}

	public static ResourceLocation texture(DyeColor color) {
		return TEXTURES_BY_COLOR.get(color);
	}

	@Override
	public void extractRenderState(Cushion cushion, CushionRenderState state, float partialTicks) {
		super.extractRenderState(cushion, state, partialTicks);
		state.direction = Direction.fromYRot(cushion.getYRot());
		state.texture = texture(cushion.getColor());
	}

	@Override
	public void render(CushionRenderState state, PoseStack poseStack, MultiBufferSource buffers, int light) {
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.direction.toYRot()));
		poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
		poseStack.translate(0.0, -0.25, 0.0);
		this.model.renderToBuffer(poseStack, buffers.getBuffer(this.model.renderType(state.texture)), light, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
		super.render(state, poseStack, buffers, light);
	}

	@Override
	public CushionRenderState createRenderState() {
		return new CushionRenderState();
	}
}
