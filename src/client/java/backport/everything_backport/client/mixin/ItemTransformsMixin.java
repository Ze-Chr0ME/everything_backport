package backport.everything_backport.client.mixin;

import backport.everything_backport.client.copperage.OnShelfTransform;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemTransforms.class)
public abstract class ItemTransformsMixin implements OnShelfTransform {
	@Unique
	private ItemTransform everythingBackport$onShelf = ItemTransform.NO_TRANSFORM;

	@Override
	public ItemTransform everythingBackport$getOnShelf() {
		return this.everythingBackport$onShelf;
	}

	@Override
	public void everythingBackport$setOnShelf(ItemTransform transform) {
		this.everythingBackport$onShelf = transform;
	}

	@Inject(method = "getTransform", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$onShelf(ItemDisplayContext itemDisplayContext, CallbackInfoReturnable<ItemTransform> cir) {
		if (itemDisplayContext == ItemDisplayContext.FIXED && OnShelfTransform.Context.isRenderingShelf()) {
			cir.setReturnValue(this.everythingBackport$onShelf);
		}
	}
}
