package backport.everything_backport.client.mountsofmayhem.screen;

import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import backport.everything_backport.mountsofmayhem.inventory.NautilusInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;

/** 1.21.11's {@code NautilusInventoryScreen} (with {@code AbstractMountInventoryScreen} folded in): a saddle and an armor slot. */
public class NautilusInventoryScreen extends AbstractContainerScreen<NautilusInventoryMenu> {
	private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
	private static final ResourceLocation NAUTILUS_INVENTORY_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/nautilus.png");
	private final AbstractNautilus nautilus;
	private float xMouse;
	private float yMouse;

	public NautilusInventoryScreen(NautilusInventoryMenu menu, Inventory inventory, AbstractNautilus nautilus) {
		super(menu, inventory, nautilus.getDisplayName());
		this.nautilus = nautilus;
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		int left = (this.width - this.imageWidth) / 2;
		int top = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, NAUTILUS_INVENTORY_LOCATION, left, top, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
		if (this.nautilus.canUseSlot(EquipmentSlot.SADDLE)) {
			this.drawSlot(guiGraphics, left + 7, top + 35 - 18);
		}

		if (this.nautilus.canUseSlot(EquipmentSlot.BODY)) {
			this.drawSlot(guiGraphics, left + 7, top + 35);
		}

		InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, left + 26, top + 18, left + 78, top + 70, 17, 0.25F, this.xMouse, this.yMouse, this.nautilus);
	}

	private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, x, y, 18, 18);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.xMouse = mouseX;
		this.yMouse = mouseY;
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
}
