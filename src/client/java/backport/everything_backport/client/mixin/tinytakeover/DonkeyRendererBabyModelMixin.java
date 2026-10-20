package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyDonkeyModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.DonkeyModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.DonkeyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the donkey or mule foal from the 26.1 model instead of the shrunken adult. */
@Mixin(DonkeyRenderer.class)
public abstract class DonkeyRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/DonkeyModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static DonkeyModel everythingBackport$babyModel(ModelPart root, Operation<DonkeyModel> original) {
		return new BabyDonkeyModel(root);
	}
}
