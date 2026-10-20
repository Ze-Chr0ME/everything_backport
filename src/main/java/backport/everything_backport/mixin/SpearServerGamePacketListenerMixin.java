package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.SpearCombat;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/** Attacks are checked against the held item's attack range instead of the interaction range (1.21.11). */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class SpearServerGamePacketListenerMixin {
	@WrapOperation(
		method = "handleInteract",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;canInteractWithEntity(Lnet/minecraft/world/phys/AABB;D)Z")
	)
	private boolean everythingBackport$attackRange(
		ServerPlayer player, AABB box, double tolerance, Operation<Boolean> original, @Local(argsOnly = true) ServerboundInteractPacket packet
	) {
		return everythingBackport$isAttack(packet) ? SpearCombat.isWithinAttackRange(player, box, tolerance) : original.call(player, box, tolerance);
	}

	@Unique
	private static boolean everythingBackport$isAttack(ServerboundInteractPacket packet) {
		boolean[] attack = {false};
		packet.dispatch(new ServerboundInteractPacket.Handler() {
			@Override
			public void onInteraction(InteractionHand hand) {
			}

			@Override
			public void onInteraction(InteractionHand hand, Vec3 location) {
			}

			@Override
			public void onAttack() {
				attack[0] = true;
			}
		});
		return attack[0];
	}
}
