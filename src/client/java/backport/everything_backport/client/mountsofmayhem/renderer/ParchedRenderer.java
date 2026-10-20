package backport.everything_backport.client.mountsofmayhem.renderer;

import backport.everything_backport.client.mountsofmayhem.MountsModelLayers;
import backport.everything_backport.mountsofmayhem.entity.Parched;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;

public class ParchedRenderer extends AbstractSkeletonRenderer<Parched, SkeletonRenderState> {
	private static final ResourceLocation PARCHED_SKELETON_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/parched.png");

	public ParchedRenderer(EntityRendererProvider.Context context) {
		super(context, MountsModelLayers.PARCHED, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
	}

	@Override
	public ResourceLocation getTextureLocation(SkeletonRenderState state) {
		return PARCHED_SKELETON_LOCATION;
	}

	@Override
	public SkeletonRenderState createRenderState() {
		return new SkeletonRenderState();
	}
}
