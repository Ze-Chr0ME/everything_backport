package backport.everything_backport.client.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** 26.2: the score under a player's name shows up to their {@code below_name_distance} attribute away instead of a fixed 10 blocks. */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererBelowNameMixin {
	@ModifyConstant(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", constant = @Constant(doubleValue = 100.0))
	private double everythingBackport$belowNameDistance(double maxDistanceSq, AbstractClientPlayer player, PlayerRenderState state, float partialTicks) {
		return Mth.square(ChaosAttributes.get(player, ChaosAttributes.BELOW_NAME_DISTANCE));
	}
}
