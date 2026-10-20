package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.EquineBodyArmorState;
import backport.everything_backport.client.mountsofmayhem.MountsModelLayers;
import net.minecraft.client.model.AbstractEquineModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11 undead horses render horse armor, under their saddle. */
@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, EquineRenderState, AbstractEquineModel<EquineRenderState>> {
	private UndeadHorseRendererMixin(EntityRendererProvider.Context context, AbstractEquineModel<EquineRenderState> model, AbstractEquineModel<EquineRenderState> babyModel) {
		super(context, model, babyModel);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$armorLayer(EntityRendererProvider.Context context, UndeadHorseRenderer.Type type, CallbackInfo ci) {
		this.layers.add(
			0,
			new SimpleEquipmentLayer<>(
				this,
				context.getEquipmentRenderer(),
				EquipmentClientInfo.LayerType.HORSE_BODY,
				state -> ((EquineBodyArmorState)state).everythingBackport$getBodyArmorItem(),
				new HorseModel(context.bakeLayer(MountsModelLayers.UNDEAD_HORSE_ARMOR)),
				new HorseModel(context.bakeLayer(MountsModelLayers.UNDEAD_HORSE_BABY_ARMOR))
			)
		);
	}
}
