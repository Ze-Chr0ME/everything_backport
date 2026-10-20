package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyPandaModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.PandaRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the panda cub from the 26.1 model instead of the shrunken adult. */
@Mixin(PandaRenderer.class)
public abstract class PandaRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/PandaModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static PandaModel everythingBackport$babyModel(ModelPart root, Operation<PandaModel> original) {
		return new BabyPandaModel(root);
	}
}
