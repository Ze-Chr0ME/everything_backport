package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Feeding a locked baby (or an undead foal) no longer speeds up its growth (26.1 canAgeUp). */
@Mixin(Animal.class)
public abstract class AnimalAgeLockMixin {
	@ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;isBaby()Z"))
	private boolean everythingBackport$growsUp(boolean isBaby) {
		return isBaby && AgeLocking.canAgeUp((AgeableMob)(Object)this);
	}
}
