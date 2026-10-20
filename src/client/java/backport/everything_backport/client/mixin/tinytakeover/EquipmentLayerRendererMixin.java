package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyArmor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.function.Function;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Baby armor uses the {@code humanoid_baby} textures and draws no trims. */
@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin {
	@WrapOperation(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0)
	)
	private Object everythingBackport$babyTexture(Function<Object, Object> lookup, Object key, Operation<Object> original) {
		Object texture = original.call(lookup, key);
		return BabyArmor.rendering ? BabyArmor.babyTexture((ResourceLocation)texture) : texture;
	}

	@WrapOperation(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;")
	)
	private Object everythingBackport$noBabyTrims(ItemStack itemStack, DataComponentType<?> type, Operation<Object> original) {
		return BabyArmor.rendering && type == DataComponents.TRIM ? null : original.call(itemStack, type);
	}
}
