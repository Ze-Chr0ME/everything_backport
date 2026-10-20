package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.EquineBodyArmorState;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** 1.21.11 moved the body armor item from horse render states to every equine render state. */
@Mixin(EquineRenderState.class)
public abstract class EquineRenderStateMixin implements EquineBodyArmorState {
	@Unique
	private ItemStack everythingBackport$bodyArmorItem = ItemStack.EMPTY;

	@Override
	public ItemStack everythingBackport$getBodyArmorItem() {
		return this.everythingBackport$bodyArmorItem;
	}

	@Override
	public void everythingBackport$setBodyArmorItem(ItemStack itemStack) {
		this.everythingBackport$bodyArmorItem = itemStack;
	}
}
