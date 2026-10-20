package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.CopperAgeModelLayers;
import backport.everything_backport.client.copperage.model.CopperGolemStatueModel;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import backport.everything_backport.copperage.block.entity.CopperGolemStatueBlockEntity;
import backport.everything_backport.copperage.entity.CopperGolemOxidationLevels;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CopperGolemStatueBlockRenderer implements BlockEntityRenderer<CopperGolemStatueBlockEntity> {
	private final Map<CopperGolemStatueBlock.Pose, CopperGolemStatueModel> models = new EnumMap<>(CopperGolemStatueBlock.Pose.class);

	public CopperGolemStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
		CopperAgeModelLayers.STATUE_POSES.forEach((pose, layer) -> this.models.put(pose, new CopperGolemStatueModel(context.bakeLayer(layer))));
	}

	@Override
	public void render(
		CopperGolemStatueBlockEntity statue, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, Vec3 cameraPos
	) {
		BlockState blockState = statue.getBlockState();
		if (blockState.getBlock() instanceof CopperGolemStatueBlock statueBlock) {
			poseStack.pushPose();
			poseStack.translate(0.5F, 0.0F, 0.5F);
			CopperGolemStatueModel model = this.models.get(blockState.getValue(CopperGolemStatueBlock.POSE));
			Direction direction = blockState.getValue(CopperGolemStatueBlock.FACING);
			model.setupAnim(direction);
			RenderType renderType = RenderType.entityCutoutNoCull(CopperGolemOxidationLevels.getOxidationLevel(statueBlock.getWeatheringState()).texture());
			model.renderToBuffer(poseStack, multiBufferSource.getBuffer(renderType), light, overlay);
			poseStack.popPose();
		}
	}
}
