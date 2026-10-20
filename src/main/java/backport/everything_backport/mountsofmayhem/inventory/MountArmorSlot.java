package backport.everything_backport.mountsofmayhem.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

/** The package-private vanilla {@code ArmorSlot}, for mount menus. */
public class MountArmorSlot extends Slot {
	private final LivingEntity owner;
	private final EquipmentSlot slot;
	@Nullable
	private final ResourceLocation emptyIcon;

	public MountArmorSlot(Container container, LivingEntity owner, EquipmentSlot slot, int index, int x, int y, @Nullable ResourceLocation emptyIcon) {
		super(container, index, x, y);
		this.owner = owner;
		this.slot = slot;
		this.emptyIcon = emptyIcon;
	}

	@Override
	public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
		this.owner.onEquipItem(this.slot, oldStack, newStack);
		super.setByPlayer(newStack, oldStack);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean mayPlace(ItemStack itemStack) {
		return this.owner.isEquippableInSlot(itemStack, this.slot);
	}

	@Override
	public boolean isActive() {
		return this.owner.canUseSlot(this.slot);
	}

	@Override
	public boolean mayPickup(Player player) {
		ItemStack itemStack = this.getItem();
		return !itemStack.isEmpty() && !player.isCreative() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)
			? false
			: super.mayPickup(player);
	}

	@Nullable
	@Override
	public ResourceLocation getNoItemIcon() {
		return this.emptyIcon;
	}
}
