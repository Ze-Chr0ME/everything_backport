package backport.everything_backport.client.mountsofmayhem.renderer;

import backport.everything_backport.client.mountsofmayhem.MountsModelLayers;
import backport.everything_backport.client.mountsofmayhem.model.NautilusArmorModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusSaddleModel;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class NautilusRenderer<T extends AbstractNautilus> extends AgeableMobRenderer<T, NautilusRenderState, NautilusModel> {
	private static final ResourceLocation NAUTILUS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/nautilus/nautilus.png");
	private static final ResourceLocation NAUTILUS_BABY_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/nautilus/nautilus_baby.png");

	public NautilusRenderer(EntityRendererProvider.Context context) {
		super(context, new NautilusModel(context.bakeLayer(MountsModelLayers.NAUTILUS)), new NautilusModel(context.bakeLayer(MountsModelLayers.NAUTILUS_BABY)), 0.7F);
		this.addLayer(
			new MountEquipmentLayer<>(
				this, "nautilus_body", state -> state.bodyArmorItem, new NautilusArmorModel(context.bakeLayer(MountsModelLayers.NAUTILUS_ARMOR)), null
			)
		);
		this.addLayer(
			new MountEquipmentLayer<>(
				this, "nautilus_saddle", state -> state.saddle, new NautilusSaddleModel(context.bakeLayer(MountsModelLayers.NAUTILUS_SADDLE)), null
			)
		);
	}

	@Override
	public ResourceLocation getTextureLocation(NautilusRenderState state) {
		return state.isBaby ? NAUTILUS_BABY_LOCATION : NAUTILUS_LOCATION;
	}

	@Override
	public NautilusRenderState createRenderState() {
		return new NautilusRenderState();
	}

	@Override
	public void extractRenderState(T nautilus, NautilusRenderState state, float partialTick) {
		super.extractRenderState(nautilus, state, partialTick);
		state.saddle = nautilus.getItemBySlot(EquipmentSlot.SADDLE).copy();
		state.bodyArmorItem = nautilus.getBodyArmorItem().copy();
	}
}
