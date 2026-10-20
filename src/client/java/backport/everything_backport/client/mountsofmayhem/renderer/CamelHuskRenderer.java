package backport.everything_backport.client.mountsofmayhem.renderer;

import backport.everything_backport.client.mountsofmayhem.MountsModelLayers;
import net.minecraft.client.model.CamelSaddleModel;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.resources.ResourceLocation;

/** The camel husk: a camel with its own texture and saddle texture (1.21.11). */
public class CamelHuskRenderer extends CamelRenderer {
	private static final ResourceLocation CAMEL_HUSK_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/camel/camel_husk.png");

	public CamelHuskRenderer(EntityRendererProvider.Context context) {
		super(context);
		// 1.21.11 swaps the camel saddle layer for one with the camel_husk_saddle textures.
		this.layers.removeIf(layer -> layer instanceof SimpleEquipmentLayer<?, ?, ?>);
		this.addLayer(
			new MountEquipmentLayer<>(
				this,
				"camel_husk_saddle",
				state -> state.saddle,
				new CamelSaddleModel(context.bakeLayer(MountsModelLayers.CAMEL_HUSK_SADDLE)),
				new CamelSaddleModel(context.bakeLayer(MountsModelLayers.CAMEL_HUSK_BABY_SADDLE))
			)
		);
	}

	@Override
	public ResourceLocation getTextureLocation(CamelRenderState state) {
		return CAMEL_HUSK_LOCATION;
	}
}
