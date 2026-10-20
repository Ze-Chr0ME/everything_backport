package backport.everything_backport.mixin;

import backport.everything_backport.copperage.block.KeepsBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Keeps the block entity when a copper chest or statue weathers into another block (1.21.9). */
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
	@WrapOperation(
		method = "setBlockState",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z", ordinal = 0)
	)
	private boolean everythingBackport$keepBlockEntity(BlockState oldState, Operation<Boolean> original, @Local(argsOnly = true) BlockState newState) {
		return original.call(oldState) && !(oldState.getBlock() instanceof KeepsBlockEntity keeps && keeps.shouldChangedStateKeepBlockEntity(newState));
	}
}
