package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Underwater, Breath of the Nautilus stops drowning but doesn't refill air unless Water Breathing or Conduit Power also
 * apply (1.21.11's {@code MobEffectUtil#shouldEffectsRefillAirsupply}).
 */
@Mixin(LivingEntity.class)
public abstract class BreathOfTheNautilusAirMixin {
	@WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;increaseAirSupply(I)I", ordinal = 0))
	private int everythingBackport$noRefillUnderwater(LivingEntity livingEntity, int airSupply, Operation<Integer> original) {
		boolean refills = !livingEntity.hasEffect(MountsEffects.BREATH_OF_THE_NAUTILUS)
			|| livingEntity.hasEffect(MobEffects.WATER_BREATHING)
			|| livingEntity.hasEffect(MobEffects.CONDUIT_POWER);
		return refills ? original.call(livingEntity, airSupply) : airSupply;
	}
}
