package backport.everything_backport.client.chaoscubed.model;

import backport.everything_backport.chaoscubed.entity.SulfurCube;
import backport.everything_backport.client.chaoscubed.ChaosModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 26.2's sulfur cube renderer. 26.2 draws the shell as the main model and the jelly (or the swallowed block) first through a layer;
 * 1.21.7 draws the main model before its layers, so here the jelly is the main model, then the block and the shell follow as layers.
 */
public class SulfurCubeRenderer extends MobRenderer<SulfurCube, SulfurCubeRenderer.State, SulfurCubeModel> {
	private static final ResourceLocation OUTER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer.png");
	private static final ResourceLocation OUTER_SMALL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer_small.png");
	private static final ResourceLocation INNER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner.png");
	private static final ResourceLocation INNER_SMALL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner_small.png");
	private final SulfurCubeModel innerModel;
	private final SulfurCubeModel smallInnerModel;

	public SulfurCubeRenderer(EntityRendererProvider.Context context) {
		super(context, new SulfurCubeModel(context.bakeLayer(ChaosModelLayers.SULFUR_CUBE_INNER)), 0.25F);
		this.innerModel = this.model;
		this.smallInnerModel = new SulfurCubeModel(context.bakeLayer(ChaosModelLayers.SULFUR_CUBE_SMALL_INNER));
		this.addLayer(new ContainedBlockLayer(this, context.getBlockRenderDispatcher()));
		this.addLayer(
			new OuterLayer(
				this,
				new SulfurCubeModel(context.bakeLayer(ChaosModelLayers.SULFUR_CUBE)),
				new SulfurCubeModel(context.bakeLayer(ChaosModelLayers.SULFUR_CUBE_SMALL))
			)
		);
	}

	@Override
	public void render(State state, PoseStack poseStack, MultiBufferSource buffer, int light) {
		this.model = state.isBaby ? this.smallInnerModel : this.innerModel;
		super.render(state, poseStack, buffer, light);
	}

	/** The jelly hides while the cube holds a block, and when the cube is invisible. */
	@Override
	protected @Nullable RenderType getRenderType(State state, boolean visible, boolean translucent, boolean glowing) {
		return state.containedBlock != null || state.isInvisible ? null : super.getRenderType(state, visible, translucent, glowing);
	}

	@Override
	protected float getWhiteOverlayProgress(State state) {
		return isLit(state.fuseRemainingTicks) ? 1.0F : 0.0F;
	}

	private static boolean isLit(float fuse) {
		return fuse > 0.0F && (int)fuse / 5 % 2 == 0;
	}

	@Override
	protected float getShadowRadius(State state) {
		return state.size * 0.25F;
	}

	@Override
	protected void scale(State state, PoseStack poseStack) {
		poseStack.scale(0.999F, 0.999F, 0.999F);
		poseStack.translate(0.0F, 0.001F, 0.0F);
		float size = state.size;
		float squish = state.containedBlock == null ? state.squish / (size * 0.5F + 1.0F) : 0.0F;
		float w = 1.0F / (squish + 1.0F);
		poseStack.scale(w * size, 1.0F / w * size, w * size);
		float fuse = state.fuseRemainingTicks;
		if (fuse < 10.0F && fuse > 0.0F) {
			float swell = Mth.clamp(1.0F - fuse / 10.0F, 0.0F, 1.0F);
			swell *= swell;
			swell *= swell;
			float s = 1.0F + swell * 0.3F;
			poseStack.scale(s, s, s);
		}

		float vOffset = state.isBaby ? 1.24F : 0.98F;
		float extraDownscale = state.isBaby ? 1.0F : 0.5F;
		float onePixelUpIfVisible = (state.isInvisible ? 0.0F : 1.0F) / 16.0F;
		poseStack.scale(extraDownscale, extraDownscale, extraDownscale);
		poseStack.translate(-0.0F, vOffset - onePixelUpIfVisible, -0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(State state) {
		return state.isBaby ? INNER_SMALL_LOCATION : INNER_LOCATION;
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(SulfurCube entity, State state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish);
		state.size = entity.getSize();
		state.fuseRemainingTicks = entity.isPrimed() ? entity.getFuse() - partialTicks + 1.0F : 0.0F;
		ItemStack containedBlock = entity.getItemBySlot(EquipmentSlot.BODY);
		state.containedBlock = null;
		if (!containedBlock.isEmpty()) {
			BlockItemStateProperties blockItemState = containedBlock.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
			BlockState blockState = blockItemState.apply(Block.byItem(containedBlock.getItem()).defaultBlockState());
			if (!blockState.isAir()) {
				state.containedBlock = blockState;
			}
		}
	}

	public static class State extends SlimeRenderState {
		public float fuseRemainingTicks;
		public @Nullable BlockState containedBlock;
	}

	/** The swallowed block, upside down in the middle of the cube, flashing white while the fuse burns. */
	private static class ContainedBlockLayer extends RenderLayer<State, SulfurCubeModel> {
		private final BlockRenderDispatcher blockRenderer;

		ContainedBlockLayer(RenderLayerParent<State, SulfurCubeModel> parent, BlockRenderDispatcher blockRenderer) {
			super(parent);
			this.blockRenderer = blockRenderer;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource buffer, int light, State state, float yRot, float xRot) {
			if (state.containedBlock == null) {
				return;
			}

			int overlay = isLit(state.fuseRemainingTicks) ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10) : LivingEntityRenderer.getOverlayCoords(state, 0.0F);
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
			if (state.isBaby) {
				poseStack.scale(0.5F, 0.5F, 0.5F);
			}

			poseStack.translate(-0.5F, -0.518F, -0.5F);
			this.blockRenderer.renderSingleBlock(state.containedBlock, poseStack, buffer, light, overlay);
			poseStack.popPose();
		}
	}

	/** The translucent shell, drawn last so the jelly and the block show through it. */
	private static class OuterLayer extends RenderLayer<State, SulfurCubeModel> {
		private final SulfurCubeModel model;
		private final SulfurCubeModel smallModel;

		OuterLayer(RenderLayerParent<State, SulfurCubeModel> parent, SulfurCubeModel model, SulfurCubeModel smallModel) {
			super(parent);
			this.model = model;
			this.smallModel = smallModel;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource buffer, int light, State state, float yRot, float xRot) {
			boolean outlineOnly = state.appearsGlowing && state.isInvisible;
			if (!state.isInvisible || outlineOnly) {
				ResourceLocation texture = state.isBaby ? OUTER_SMALL_LOCATION : OUTER_LOCATION;
				VertexConsumer vertexConsumer = buffer.getBuffer(outlineOnly ? RenderType.outline(texture) : RenderType.entityTranslucent(texture));
				SulfurCubeModel shell = state.isBaby ? this.smallModel : this.model;
				shell.setupAnim(state);
				shell.renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(state, 0.0F));
			}
		}
	}
}
