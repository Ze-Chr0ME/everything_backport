package backport.everything_backport.mountsofmayhem.inventory;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mixin.ServerPlayerInvoker;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

/** Opening a nautilus inventory, 1.21.11's {@code ClientboundMountScreenOpenPacket}. */
public final class MountMenus {
	private MountMenus() {
	}

	public static int getInventorySize(int columns) {
		return columns * 3;
	}

	public static void init() {
		PayloadTypeRegistry.playS2C().register(OpenNautilusScreenPayload.TYPE, OpenNautilusScreenPayload.STREAM_CODEC);
	}

	public static void openNautilusInventory(ServerPlayer player, AbstractNautilus nautilus, Container container) {
		if (player.containerMenu != player.inventoryMenu) {
			player.closeContainer();
		}

		ServerPlayerInvoker invoker = (ServerPlayerInvoker)player;
		invoker.everythingBackport$nextContainerCounter();
		int containerId = invoker.everythingBackport$getContainerCounter();
		int columns = nautilus.getInventoryColumns();
		ServerPlayNetworking.send(player, new OpenNautilusScreenPayload(containerId, columns, nautilus.getId()));
		player.containerMenu = new NautilusInventoryMenu(containerId, player.getInventory(), container, nautilus, columns);
		invoker.everythingBackport$initMenu(player.containerMenu);
	}

	public record OpenNautilusScreenPayload(int containerId, int inventoryColumns, int entityId) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<OpenNautilusScreenPayload> TYPE = new CustomPacketPayload.Type<>(EverythingBackport.id("open_nautilus_screen"));
		public static final StreamCodec<FriendlyByteBuf, OpenNautilusScreenPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.CONTAINER_ID,
			OpenNautilusScreenPayload::containerId,
			ByteBufCodecs.VAR_INT,
			OpenNautilusScreenPayload::inventoryColumns,
			ByteBufCodecs.INT,
			OpenNautilusScreenPayload::entityId,
			OpenNautilusScreenPayload::new
		);

		@Override
		public CustomPacketPayload.Type<OpenNautilusScreenPayload> type() {
			return TYPE;
		}
	}
}
