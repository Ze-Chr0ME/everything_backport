package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyCatModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** The kitten collar follows the 26.1 kitten pose. */
@Mixin(CatCollarLayer.class)
public abstract class CatCollarLayerMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/CatModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private CatModel everythingBackport$babyCollar(ModelPart root, Operation<CatModel> original) {
		return new BabyCatModel(root);
	}
}
