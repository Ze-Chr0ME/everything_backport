package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsOfMayhem;
import backport.everything_backport.mountsofmayhem.entity.AbstractNautilus;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Counts the distance ridden on a nautilus (1.21.11's {@code minecraft:nautilus_one_cm}). */
@Mixin(ServerPlayer.class)
public abstract class NautilusRidingStatMixin {
	@Inject(method = "checkRidingStatistics", at = @At("TAIL"))
	private void everythingBackport$nautilusDistance(double dx, double dy, double dz, CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		if (player.isPassenger() && player.getVehicle() instanceof AbstractNautilus && (dx != 0.0 || dy != 0.0 || dz != 0.0)) {
			player.awardStat(MountsOfMayhem.NAUTILUS_ONE_CM, Math.round((float)Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F));
		}
	}
}
