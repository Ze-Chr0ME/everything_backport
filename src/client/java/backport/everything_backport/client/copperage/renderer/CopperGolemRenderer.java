package backport.everything_backport.client.copperage.renderer;

import backport.everything_backport.client.copperage.CopperAgeModelLayers;
import backport.everything_backport.client.copperage.model.CopperGolemModel;
import backport.everything_backport.copperage.entity.CopperGolem;
import backport.everything_backport.copperage.entity.CopperGolemOxidationLevels;
import java.util.Optional;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.BlockItemStateProperties;

public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemRenderState, CopperGolemModel> {
	public CopperGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new CopperGolemModel(context.bakeLayer(CopperAgeModelLayers.COPPER_GOLEM)), 0.5F);
		this.addLayer(new CopperGolemEyesLayer(this));
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new BlockDecorationLayer<>(this, state -> state.blockOnAntenna, this.model::applyBlockOnAntennaTransform));
		this.addLayer(new CopperGolemHeadLayer(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(CopperGolemRenderState renderState) {
		return CopperGolemOxidationLevels.getOxidationLevel(renderState.weathering).texture();
	}

	@Override
	public CopperGolemRenderState createRenderState() {
		return new CopperGolemRenderState();
	}

	@Override
	public void extractRenderState(CopperGolem copperGolem, CopperGolemRenderState renderState, float partialTick) {
		super.extractRenderState(copperGolem, renderState, partialTick);
		ArmedEntityRenderState.extractArmedEntityRenderState(copperGolem, renderState, this.itemModelResolver);
		renderState.weathering = copperGolem.getWeatherState();
		renderState.copperGolemState = copperGolem.getState();
		renderState.idleAnimationState.copyFrom(copperGolem.getIdleAnimationState());
		renderState.interactionGetItem.copyFrom(copperGolem.getInteractionGetItemAnimationState());
		renderState.interactionGetNoItem.copyFrom(copperGolem.getInteractionGetNoItemAnimationState());
		renderState.interactionDropItem.copyFrom(copperGolem.getInteractionDropItemAnimationState());
		renderState.interactionDropNoItem.copyFrom(copperGolem.getInteractionDropNoItemAnimationState());
		renderState.blockOnAntenna = Optional.of(copperGolem.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA)).flatMap(itemStack -> {
			if (itemStack.getItem() instanceof BlockItem blockItem) {
				BlockItemStateProperties properties = itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
				return Optional.of(properties.apply(blockItem.getBlock().defaultBlockState()));
			} else {
				return Optional.empty();
			}
		});
	}
}
