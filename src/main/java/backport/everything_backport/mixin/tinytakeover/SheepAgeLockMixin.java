package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.sheep.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Eating grass no longer ages up a locked lamb (26.1 canAgeUp). */
@Mixin(Sheep.class)
public abstract class SheepAgeLockMixin {
	@ModifyExpressionValue(method = "ate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/sheep/Sheep;isBaby()Z"))
	private boolean everythingBackport$growsUp(boolean isBaby) {
		return isBaby && AgeLocking.canAgeUp((AgeableMob)(Object)this);
	}
}
