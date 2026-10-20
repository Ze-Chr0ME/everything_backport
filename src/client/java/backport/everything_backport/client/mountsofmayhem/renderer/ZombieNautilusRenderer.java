package backport.everything_backport.client.mountsofmayhem.renderer;

import backport.everything_backport.client.mountsofmayhem.MountsModelLayers;
import backport.everything_backport.client.mountsofmayhem.model.NautilusArmorModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusModel;
import backport.everything_backport.client.mountsofmayhem.model.NautilusSaddleModel;
import backport.everything_backport.client.mountsofmayhem.model.ZombieNautilusCoralModel;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilus;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilusVariant;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class ZombieNautilusRenderer extends MobRenderer<ZombieNautilus, NautilusRenderState, NautilusModel> {
	private final Map<ZombieNautilusVariant.ModelType, NautilusModel> models;

	public ZombieNautilusRenderer(EntityRendererProvider.Context context) {
		super(context, new NautilusModel(context.bakeLayer(MountsModelLayers.ZOMBIE_NAUTILUS)), 0.7F);
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
		this.models = Maps.newEnumMap(
			Map.of(
				ZombieNautilusVariant.ModelType.NORMAL,
				new NautilusModel(context.bakeLayer(MountsModelLayers.ZOMBIE_NAUTILUS)),
				ZombieNautilusVariant.ModelType.WARM,
				new ZombieNautilusCoralModel(context.bakeLayer(MountsModelLayers.ZOMBIE_NAUTILUS_CORAL))
			)
		);
	}

	@Override
	public void render(NautilusRenderState state, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
		if (state.variant != null) {
			this.model = this.models.get(state.variant.modelAndTexture().model());
			super.render(state, poseStack, multiBufferSource, light);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(NautilusRenderState state) {
		return state.variant == null ? MissingTextureAtlasSprite.getLocation() : state.variant.modelAndTexture().asset().texturePath();
	}

	@Override
	public NautilusRenderState createRenderState() {
		return new NautilusRenderState();
	}

	@Override
	public void extractRenderState(ZombieNautilus zombieNautilus, NautilusRenderState state, float partialTick) {
		super.extractRenderState(zombieNautilus, state, partialTick);
		state.saddle = zombieNautilus.getItemBySlot(EquipmentSlot.SADDLE).copy();
		state.bodyArmorItem = zombieNautilus.getBodyArmorItem().copy();
		state.variant = zombieNautilus.getVariant().value();
	}
}
