package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Time spent in a hive doesn't age a locked baby bee (26.1). */
@Mixin(targets = "net.minecraft.world.level.block.entity.BeehiveBlockEntity$Occupant")
public abstract class BeehiveAgeLockMixin {
	@WrapOperation(method = "setBeeReleaseData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;setAge(I)V"))
	private static void everythingBackport$skipLocked(Bee bee, int age, Operation<Void> original) {
		if (!AgeLocking.isAgeLocked(bee)) {
			original.call(bee, age);
		}
	}
}
