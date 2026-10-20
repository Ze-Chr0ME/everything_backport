package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Breath of the Nautilus counts as water breathing (1.21.11). */
@Mixin(MobEffectUtil.class)
public abstract class BreathOfTheNautilusMixin {
	@ModifyReturnValue(method = "hasWaterBreathing", at = @At("RETURN"))
	private static boolean everythingBackport$breathOfTheNautilus(boolean original, LivingEntity livingEntity) {
		return original || livingEntity.hasEffect(MountsEffects.BREATH_OF_THE_NAUTILUS);
	}
}
