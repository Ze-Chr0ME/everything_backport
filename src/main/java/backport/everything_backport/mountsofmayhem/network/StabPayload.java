package backport.everything_backport.mountsofmayhem.network;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.PiercingWeapon;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/** 1.21.11's {@code ServerboundPlayerActionPacket.Action.STAB}: a piercing attack with the main hand. */
public record StabPayload() implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<StabPayload> TYPE = new CustomPacketPayload.Type<>(EverythingBackport.id("stab"));
	public static final StreamCodec<FriendlyByteBuf, StabPayload> STREAM_CODEC = StreamCodec.unit(new StabPayload());

	@Override
	public CustomPacketPayload.Type<StabPayload> type() {
		return TYPE;
	}

	public static void init() {
		PayloadTypeRegistry.playC2S().register(TYPE, STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> handle(context.player()));
	}

	private static void handle(ServerPlayer player) {
		player.resetLastActionTime();
		if (!player.isSpectator()) {
			ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
			if (!SpearCombat.cannotAttackWithItem(player, itemStack, 5)) {
				PiercingWeapon piercingWeapon = itemStack.get(MountsComponents.PIERCING_WEAPON);
				if (piercingWeapon != null) {
					piercingWeapon.attack(player, EquipmentSlot.MAINHAND);
				}
			}
		}
	}
}
