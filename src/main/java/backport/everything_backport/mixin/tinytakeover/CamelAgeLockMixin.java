package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Cactus no longer ages up a locked baby camel (26.1 canAgeUp). */
@Mixin(Camel.class)
public abstract class CamelAgeLockMixin {
	@ModifyExpressionValue(method = "handleEating", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/camel/Camel;isBaby()Z"))
	private boolean everythingBackport$growsUp(boolean isBaby) {
		return isBaby && AgeLocking.canAgeUp((AgeableMob)(Object)this);
	}
}
