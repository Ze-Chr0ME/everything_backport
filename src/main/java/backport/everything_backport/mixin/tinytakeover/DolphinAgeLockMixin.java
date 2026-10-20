package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Dolphin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Fish no longer ages up a locked baby dolphin (26.1 canAgeUp). */
@Mixin(Dolphin.class)
public abstract class DolphinAgeLockMixin {
	@ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Dolphin;isBaby()Z"))
	private boolean everythingBackport$growsUp(boolean isBaby) {
		return isBaby && AgeLocking.canAgeUp((AgeableMob)(Object)this);
	}
}
