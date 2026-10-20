package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearPoses;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fills the spear render state of every humanoid, including players, and picks mob spear poses (1.21.11). */
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin {
	@Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
	private static void everythingBackport$spearState(
		LivingEntity livingEntity, HumanoidRenderState state, float partialTick, ItemModelResolver itemModelResolver, CallbackInfo ci
	) {
		SpearPoses.extract(livingEntity, state, partialTick);
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;F)V", at = @At("TAIL"))
	private void everythingBackport$spearPose(Mob mob, HumanoidRenderState state, float partialTick, CallbackInfo ci) {
		SpearPoses.applyToMob(mob, state, (Object)this instanceof AbstractZombieRenderer<?, ?, ?>);
	}
}
