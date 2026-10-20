package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyPolarBearModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the polar bear cub from the 26.1 model instead of the shrunken adult. */
@Mixin(PolarBearRenderer.class)
public abstract class PolarBearRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/PolarBearModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static PolarBearModel everythingBackport$babyModel(ModelPart root, Operation<PolarBearModel> original) {
		return new BabyPolarBearModel(root);
	}
}
