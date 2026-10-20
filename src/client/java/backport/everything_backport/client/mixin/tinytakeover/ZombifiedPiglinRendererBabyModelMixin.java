package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyZombifiedPiglinModel;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.ZombifiedPiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.ZombifiedPiglinRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Builds the baby zombified piglin from the 26.1 model instead of the shrunken adult. */
@Mixin(ZombifiedPiglinRenderer.class)
public abstract class ZombifiedPiglinRendererBabyModelMixin {
	@WrapOperation(method = "<init>", at = @At(value = "NEW", target = "Lnet/minecraft/client/model/ZombifiedPiglinModel;<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 1))
	private static ZombifiedPiglinModel everythingBackport$babyModel(ModelPart root, Operation<ZombifiedPiglinModel> original) {
		return new BabyZombifiedPiglinModel(root);
	}
}
