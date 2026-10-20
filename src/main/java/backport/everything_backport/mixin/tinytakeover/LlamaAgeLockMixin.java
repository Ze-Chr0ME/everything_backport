package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Food no longer ages up a locked baby llama (26.1). */
@Mixin(Llama.class)
public abstract class LlamaAgeLockMixin {
	@ModifyExpressionValue(method = "handleEating", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Llama;isBaby()Z"))
	private boolean everythingBackport$growsUp(boolean isBaby) {
		return isBaby && !AgeLocking.isAgeLocked(this);
	}
}
