package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.CopperAgeModelLayers;
import backport.everything_backport.client.copperage.model.CopperGolemStatueModel;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import backport.everything_backport.copperage.entity.CopperGolemOxidationLevels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.WeatheringCopper;
import org.joml.Vector3f;

public class CopperGolemStatueSpecialRenderer implements NoDataSpecialModelRenderer {
	private final CopperGolemStatueModel model;
	private final ResourceLocation texture;

	public CopperGolemStatueSpecialRenderer(CopperGolemStatueModel model, ResourceLocation texture) {
		this.model = model;
		this.texture = texture;
	}

	@Override
	public void render(ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, boolean foil) {
		this.positionModel(poseStack);
		this.model.setupAnim(Direction.SOUTH);
		this.model.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(this.texture)), light, overlay);
	}

	@Override
	public void getExtents(Set<Vector3f> set) {
		PoseStack poseStack = new PoseStack();
		this.positionModel(poseStack);
		this.model.setupAnim(Direction.SOUTH);
		this.model.root().getExtentsForGui(poseStack, set);
	}

	private void positionModel(PoseStack poseStack) {
		poseStack.translate(0.5F, 1.5F, 0.5F);
		poseStack.scale(-1.0F, -1.0F, 1.0F);
	}

	public record Unbaked(ResourceLocation texture, CopperGolemStatueBlock.Pose pose) implements SpecialModelRenderer.Unbaked {
		public static final MapCodec<CopperGolemStatueSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("texture").forGetter(CopperGolemStatueSpecialRenderer.Unbaked::texture),
					CopperGolemStatueBlock.Pose.CODEC.fieldOf("pose").forGetter(CopperGolemStatueSpecialRenderer.Unbaked::pose)
				)
				.apply(instance, CopperGolemStatueSpecialRenderer.Unbaked::new)
		);

		public Unbaked(WeatheringCopper.WeatherState weatherState, CopperGolemStatueBlock.Pose pose) {
			this(CopperGolemOxidationLevels.getOxidationLevel(weatherState).texture(), pose);
		}

		@Override
		public MapCodec<CopperGolemStatueSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
			return new CopperGolemStatueSpecialRenderer(
				new CopperGolemStatueModel(entityModelSet.bakeLayer(CopperAgeModelLayers.STATUE_POSES.get(this.pose))), this.texture
			);
		}
	}
}
