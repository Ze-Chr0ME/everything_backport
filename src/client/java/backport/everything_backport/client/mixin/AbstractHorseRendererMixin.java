package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.EquineBodyArmorState;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseRenderer.class)
public abstract class AbstractHorseRendererMixin {
	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;Lnet/minecraft/client/renderer/entity/state/EquineRenderState;F)V",
		at = @At("TAIL")
	)
	private void everythingBackport$bodyArmor(AbstractHorse horse, EquineRenderState state, float partialTick, CallbackInfo ci) {
		((EquineBodyArmorState)state).everythingBackport$setBodyArmorItem(horse.getBodyArmorItem().copy());
	}
}
