package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyArmor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Babies of the zombie and piglin families wear 26.1's per-slot baby armor. */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "getArmorModel", at = @At("RETURN"))
	private A everythingBackport$babyArmorModel(A model, @Local(argsOnly = true) HumanoidRenderState state, @Local(argsOnly = true) EquipmentSlot slot) {
		BabyArmor.Model baby = BabyArmor.model(state, slot);
		return baby != null ? (A)(Object)baby : model;
	}

	@WrapOperation(
		method = "renderArmorPiece",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;copyPropertiesTo(Lnet/minecraft/client/model/HumanoidModel;)V")
	)
	private void everythingBackport$poseBabyArmor(HumanoidModel<S> wearer, HumanoidModel<S> armor, Operation<Void> original) {
		if (armor instanceof BabyArmor.Model) {
			BabyArmor.copyPose(wearer, armor);
		} else {
			original.call(wearer, armor);
		}
	}

	@WrapOperation(
		method = "renderArmorPiece",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
		)
	)
	private void everythingBackport$renderBabyArmor(
		EquipmentLayerRenderer renderer,
		EquipmentClientInfo.LayerType layerType,
		ResourceKey<EquipmentAsset> asset,
		Model model,
		ItemStack itemStack,
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int light,
		Operation<Void> original
	) {
		if (!(model instanceof BabyArmor.Model)) {
			original.call(renderer, layerType, asset, model, itemStack, poseStack, bufferSource, light);
		} else if (BabyArmor.hasBabyTexture(asset)) {
			BabyArmor.rendering = true;
			try {
				original.call(renderer, EquipmentClientInfo.LayerType.HUMANOID, asset, model, itemStack, poseStack, bufferSource, light);
			} finally {
				BabyArmor.rendering = false;
			}
		}
	}
}
