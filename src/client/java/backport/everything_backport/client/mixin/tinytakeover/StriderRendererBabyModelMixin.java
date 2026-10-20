package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyStriderModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.StriderModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.StriderRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the baby strider from the 26.1 model instead of the shrunken adult. */
@Mixin(StriderRenderer.class)
public abstract class StriderRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/StriderModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static StriderModel everythingBackport$babyModel(ModelPart root, Operation<StriderModel> original) {
		return new BabyStriderModel(root);
	}
}
