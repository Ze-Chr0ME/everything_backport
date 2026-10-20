package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosCubed;
import backport.everything_backport.chaoscubed.ChaosTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.2: with nothing registered for it, a block a sulfur cube can swallow is fed to the cube in front instead of dropped. */
@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
	@Inject(method = "getDefaultDispenseMethod", at = @At("HEAD"), cancellable = true)
	private static void everythingBackport$swallowableDefault(ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {
		if (!itemStack.has(DataComponents.EQUIPPABLE) && itemStack.is(ChaosTags.SULFUR_CUBE_SWALLOWABLE)) {
			cir.setReturnValue(ChaosCubed.SWALLOW_BEHAVIOR);
		}
	}
}
