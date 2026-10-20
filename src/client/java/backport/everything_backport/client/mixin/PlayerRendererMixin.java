package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearPoses;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Players hold spears in 1.21.11's spear pose. */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
	@Inject(
		method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V",
		at = @At("TAIL")
	)
	private void everythingBackport$spearPose(AbstractClientPlayer player, PlayerRenderState state, float partialTick, CallbackInfo ci) {
		SpearPoses.applyToPlayer(player, state);
	}
}
