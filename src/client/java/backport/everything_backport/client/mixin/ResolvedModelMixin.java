package backport.everything_backport.client.mixin;

import backport.everything_backport.client.copperage.OnShelfTransform;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ResolvedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Inherits the {@code on_shelf} transform from parent models, like the other display transforms. */
@Mixin(ResolvedModel.class)
public interface ResolvedModelMixin {
	@Inject(method = "findTopTransforms", at = @At("RETURN"))
	private static void everythingBackport$findOnShelf(ResolvedModel resolvedModel, CallbackInfoReturnable<ItemTransforms> cir) {
		for (ResolvedModel model = resolvedModel; model != null; model = model.parent()) {
			ItemTransforms transforms = model.wrapped().transforms();
			if (transforms != null) {
				ItemTransform onShelf = ((OnShelfTransform)(Object)transforms).everythingBackport$getOnShelf();
				if (onShelf != ItemTransform.NO_TRANSFORM) {
					((OnShelfTransform)(Object)cir.getReturnValue()).everythingBackport$setOnShelf(onShelf);
					return;
				}
			}
		}
	}
}
