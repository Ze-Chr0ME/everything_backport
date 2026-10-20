package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyWolfModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.WolfRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the puppy from the 26.1 model instead of the shrunken adult. */
@Mixin(WolfRenderer.class)
public abstract class WolfRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/WolfModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static WolfModel everythingBackport$babyModel(ModelPart root, Operation<WolfModel> original) {
		return new BabyWolfModel(root);
	}
}
