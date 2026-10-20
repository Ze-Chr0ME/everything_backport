package backport.everything_backport.client.mixin.tinytakeover;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Babies hold items closer to their smaller 26.1 hands. */
@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerBabyMixin {
	@WrapOperation(
		method = "renderArmWithItem",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 0)
	)
	private void everythingBackport$babyOffset(
		PoseStack poseStack, float x, float y, float z, Operation<Void> original, @Local(argsOnly = true) ArmedEntityRenderState state
	) {
		if (state.isBaby && state.entityType != EntityType.ARMOR_STAND) {
			original.call(poseStack, 0.0F, 1.0F / 16.0F, -4.5F / 16.0F);
		} else {
			original.call(poseStack, x, y, z);
		}
	}
}
