package backport.everything_backport.mixin;

import backport.everything_backport.copperage.block.CopperChestBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Copper chests play their own open and close sounds. */
@Mixin(targets = "net.minecraft.world.level.block.entity.ChestBlockEntity$1")
public abstract class ChestOpenersCounterMixin {
	@WrapOperation(
		method = {"onOpen", "onClose"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/entity/ChestBlockEntity;playSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/sounds/SoundEvent;)V"
		)
	)
	private void everythingBackport$copperChestSound(
		Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent, Operation<Void> original
	) {
		if (blockState.getBlock() instanceof CopperChestBlock copperChest) {
			boolean opening = soundEvent == net.minecraft.sounds.SoundEvents.CHEST_OPEN;
			soundEvent = opening ? copperChest.getOpenChestSound() : copperChest.getCloseChestSound();
		}

		original.call(level, blockPos, blockState, soundEvent);
	}
}
