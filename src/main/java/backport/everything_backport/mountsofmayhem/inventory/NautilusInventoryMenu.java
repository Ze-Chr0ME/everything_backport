package backport.everything_backport.mountsofmayhem.inventory;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** 1.21.11's {@code AbstractMountInventoryMenu} and {@code NautilusInventoryMenu}: saddle and armor slots. */
public class NautilusInventoryMenu extends AbstractContainerMenu {
	private static final ResourceLocation SADDLE_SLOT_SPRITE = EverythingBackport.vanilla("container/slot/saddle");
	private static final ResourceLocation ARMOR_SLOT_SPRITE = EverythingBackport.vanilla("container/slot/nautilus_armor_inventory");
	private final Container mountContainer;
	private final AbstractNautilus nautilus;

	public NautilusInventoryMenu(int containerId, Inventory inventory, Container container, AbstractNautilus nautilus, int columns) {
		super(null, containerId);
		this.mountContainer = container;
		this.nautilus = nautilus;
		container.startOpen(inventory.player);
		this.addSlot(new MountArmorSlot(nautilus.createEquipmentSlotContainer(EquipmentSlot.SADDLE), nautilus, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE));
		this.addSlot(new MountArmorSlot(nautilus.createEquipmentSlotContainer(EquipmentSlot.BODY), nautilus, EquipmentSlot.BODY, 0, 8, 36, ARMOR_SLOT_SPRITE));
		this.addStandardInventorySlots(inventory, 8, 84);
	}

	public AbstractNautilus getNautilus() {
		return this.nautilus;
	}

	@Override
	public boolean stillValid(Player player) {
		return !this.nautilus.hasInventoryChanged(this.mountContainer)
			&& this.mountContainer.stillValid(player)
			&& this.nautilus.isAlive()
			&& player.canInteractWithEntity(this.nautilus, 4.0);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.mountContainer.stopOpen(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack = slot.getItem();
			result = itemStack.copy();
			int inventoryStart = 2 + this.mountContainer.getContainerSize();
			if (index < inventoryStart) {
				if (!this.moveItemStackTo(itemStack, inventoryStart, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(1).mayPlace(itemStack) && !this.getSlot(1).hasItem()) {
				if (!this.moveItemStackTo(itemStack, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.getSlot(0).mayPlace(itemStack) && !this.getSlot(0).hasItem()) {
				if (!this.moveItemStackTo(itemStack, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (this.mountContainer.getContainerSize() == 0 || !this.moveItemStackTo(itemStack, 2, inventoryStart, false)) {
				int hotbarStart = inventoryStart + 27;
				int hotbarEnd = hotbarStart + 9;
				if (index >= hotbarStart && index < hotbarEnd) {
					if (!this.moveItemStackTo(itemStack, inventoryStart, hotbarStart, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= inventoryStart && index < hotbarStart) {
					if (!this.moveItemStackTo(itemStack, hotbarStart, hotbarEnd, false)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemStack, hotbarStart, hotbarStart, false)) {
					return ItemStack.EMPTY;
				}

				return ItemStack.EMPTY;
			}

			if (itemStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return result;
	}
}
