package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.11 piglins don't melee while holding a spear; they charge with it instead (Piglin#canUseNonMeleeWeapon). */
@Mixin(MeleeAttack.class)
public abstract class MeleeAttackMixin {
	@ModifyReturnValue(method = "isHoldingUsableProjectileWeapon", at = @At("RETURN"))
	private static boolean everythingBackport$piglinSpear(boolean original, Mob mob) {
		return original || mob instanceof Piglin && mob.isHolding(itemStack -> itemStack.has(MountsComponents.KINETIC_WEAPON));
	}
}
