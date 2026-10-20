package backport.everything_backport.client.mountsofmayhem.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.Nullable;

/**
 * Renders a 1.21.11 equipment layer that 1.21.7's equipment assets can't describe ({@code nautilus_body},
 * {@code nautilus_saddle}, {@code camel_husk_saddle}). Vanilla's assets use the equipment asset's name as the texture
 * for these layers, so the texture is {@code textures/entity/equipment/<layer>/<asset>.png}.
 */
public class MountEquipmentLayer<S extends LivingEntityRenderState, RM extends EntityModel<? super S>, EM extends EntityModel<? super S>>
	extends RenderLayer<S, RM> {
	private final String layer;
	private final Function<S, ItemStack> itemGetter;
	private final EM adultModel;
	@Nullable
	private final EM babyModel;

	public MountEquipmentLayer(RenderLayerParent<S, RM> parent, String layer, Function<S, ItemStack> itemGetter, EM adultModel, @Nullable EM babyModel) {
		super(parent);
		this.layer = layer;
		this.itemGetter = itemGetter;
		this.adultModel = adultModel;
		this.babyModel = babyModel;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, S state, float yRot, float xRot) {
		ItemStack itemStack = this.itemGetter.apply(state);
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		EM model = state.isBaby ? this.babyModel : this.adultModel;
		if (equippable != null && equippable.assetId().isPresent() && model != null) {
			model.setupAnim(state);
			ResourceLocation asset = equippable.assetId().get().location();
			ResourceLocation texture = asset.withPath(path -> "textures/entity/equipment/" + this.layer + "/" + path + ".png");
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(texture), itemStack.hasFoil());
			model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		}
	}
}
