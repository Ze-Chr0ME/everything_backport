package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyGoatModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.GoatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.GoatRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the kid from the 26.1 model instead of the shrunken adult. */
@Mixin(GoatRenderer.class)
public abstract class GoatRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/GoatModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static GoatModel everythingBackport$babyModel(ModelPart root, Operation<GoatModel> original) {
		return new BabyGoatModel(root);
	}
}
