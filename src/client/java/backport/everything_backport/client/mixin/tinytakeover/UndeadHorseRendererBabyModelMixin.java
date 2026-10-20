package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyHorseModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the undead foal from the 26.1 model instead of the shrunken adult. */
@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/HorseModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static HorseModel everythingBackport$babyModel(ModelPart root, Operation<HorseModel> original) {
		return new BabyHorseModel(root);
	}
}
