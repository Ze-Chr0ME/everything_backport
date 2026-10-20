package backport.everything_backport.mixin.wildernessbound;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 26.3 only zooms out maps in {@code #minecraft:extendable_maps}, which holds just the plain filled map: explorer maps keep their scale. */
@Mixin(CartographyTableMenu.class)
public abstract class CartographyTableMenuMixin extends AbstractContainerMenu {
	@Shadow
	@Final
	private ResultContainer resultContainer;

	protected CartographyTableMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
		super(menuType, containerId);
	}

	@Inject(method = "setupResultSlot", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$noExplorerMapZoom(ItemStack map, ItemStack additional, ItemStack result, CallbackInfo ci) {
		if (additional.is(Items.PAPER) && !map.is(Items.FILLED_MAP)) {
			this.resultContainer.removeItemNoUpdate(2);
			this.broadcastChanges();
			ci.cancel();
		}
	}
}
