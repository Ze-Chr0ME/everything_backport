package backport.everything_backport.client.mountsofmayhem;

import backport.everything_backport.client.mountsofmayhem.renderer.CamelHuskRenderer;
import backport.everything_backport.client.mountsofmayhem.renderer.NautilusRenderer;
import backport.everything_backport.client.mountsofmayhem.renderer.ParchedRenderer;
import backport.everything_backport.client.mountsofmayhem.renderer.ZombieNautilusRenderer;
import backport.everything_backport.client.mountsofmayhem.screen.NautilusInventoryScreen;
import backport.everything_backport.mountsofmayhem.MountsEntities;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import backport.everything_backport.mountsofmayhem.inventory.MountMenus;
import backport.everything_backport.mountsofmayhem.inventory.NautilusInventoryMenu;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.SimpleContainer;

/** Client side of Mounts of Mayhem (1.21.11). */
public final class MountsOfMayhemClient {
	private MountsOfMayhemClient() {
	}

	public static void init() {
		MountsModelLayers.init();
		EntityRendererRegistry.register(MountsEntities.NAUTILUS, NautilusRenderer::new);
		EntityRendererRegistry.register(MountsEntities.ZOMBIE_NAUTILUS, ZombieNautilusRenderer::new);
		EntityRendererRegistry.register(MountsEntities.CAMEL_HUSK, CamelHuskRenderer::new);
		EntityRendererRegistry.register(MountsEntities.PARCHED, ParchedRenderer::new);
		ClientPlayNetworking.registerGlobalReceiver(MountMenus.OpenNautilusScreenPayload.TYPE, (payload, context) -> openNautilusScreen(context.client(), payload));
	}

	/** 1.21.11's {@code ClientPacketListener#handleMountScreenOpen}, mirroring 1.21.7's horse screen handling. */
	private static void openNautilusScreen(Minecraft minecraft, MountMenus.OpenNautilusScreenPayload payload) {
		LocalPlayer player = minecraft.player;
		if (player != null && player.level().getEntity(payload.entityId()) instanceof AbstractNautilus nautilus) {
			int columns = payload.inventoryColumns();
			SimpleContainer container = new SimpleContainer(MountMenus.getInventorySize(columns));
			NautilusInventoryMenu menu = new NautilusInventoryMenu(payload.containerId(), player.getInventory(), container, nautilus, columns);
			player.containerMenu = menu;
			minecraft.setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
		}
	}
}
