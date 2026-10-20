package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.OnShelfTransform;
import backport.everything_backport.copperage.block.ShelfBlock;
import backport.everything_backport.copperage.block.entity.ShelfBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
	private static final float ITEM_SIZE = 0.25F;
	private static final float ALIGN_ITEMS_TO_BOTTOM = -0.25F;
	private final ItemModelResolver itemModelResolver;
	private final ItemStackRenderState itemRenderState = new ItemStackRenderState();

	public ShelfRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.getItemModelResolver();
	}

	@Override
	public void render(ShelfBlockEntity shelf, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, Vec3 cameraPos) {
		Direction direction = shelf.getBlockState().getValue(ShelfBlock.FACING);
		float rotation = direction.getAxis().isHorizontal() ? -direction.toYRot() : 180.0F;
		NonNullList<ItemStack> items = shelf.getItems();
		int seed = HashCommon.long2int(shelf.getBlockPos().asLong());

		for (int i = 0; i < items.size(); i++) {
			ItemStack itemStack = items.get(i);
			if (!itemStack.isEmpty()) {
				OnShelfTransform.Context.setRenderingShelf(true);
				try {
					this.itemModelResolver.updateForTopItem(this.itemRenderState, itemStack, ItemDisplayContext.FIXED, shelf.getLevel(), null, seed + i);
				} finally {
					OnShelfTransform.Context.setRenderingShelf(false);
				}

				this.renderItem(shelf.getAlignItemsToBottom(), poseStack, multiBufferSource, light, i, rotation);
			}
		}
	}

	private void renderItem(boolean alignToBottom, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int slot, float rotation) {
		float x = (slot - 1) * 0.3125F;
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
		poseStack.translate(x, alignToBottom ? ALIGN_ITEMS_TO_BOTTOM : 0.0, -0.25);
		poseStack.scale(ITEM_SIZE, ITEM_SIZE, ITEM_SIZE);
		float[] bounds = {Float.MAX_VALUE, -Float.MAX_VALUE};
		this.itemRenderState.visitExtents(point -> {
			bounds[0] = Math.min(bounds[0], point.y());
			bounds[1] = Math.max(bounds[1], point.y());
		});
		if (bounds[0] <= bounds[1]) {
			double offset = -bounds[0];
			if (!alignToBottom) {
				offset += -(bounds[1] - bounds[0]) / 2.0;
			}

			poseStack.translate(0.0, offset, 0.0);
		}

		this.itemRenderState.render(poseStack, multiBufferSource, light, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
	}
}
