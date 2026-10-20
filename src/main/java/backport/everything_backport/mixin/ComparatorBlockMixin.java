package backport.everything_backport.mixin;

import backport.everything_backport.copperage.block.ShelfBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.9 made analog output directional; shelves only output towards their back. */
@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin {
	@WrapOperation(
		method = "getInputSignal",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getAnalogOutputSignal(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I"
		)
	)
	private int everythingBackport$directionalOutput(
		BlockState source, Level level, BlockPos blockPos, Operation<Integer> original, @Local(argsOnly = true) BlockState comparator
	) {
		if (source.getBlock() instanceof ShelfBlock && comparator.getValue(ComparatorBlock.FACING) != source.getValue(ShelfBlock.FACING)) {
			return 0;
		}

		return original.call(source, level, blockPos);
	}
}
