package backport.everything_backport.mixin.wildernessbound;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 26.3's {@code #shulker_does_not_teleport_to}: shulkers no longer teleport onto bedrock. */
@Mixin(Shulker.class)
public abstract class ShulkerTeleportMixin {
	@WrapOperation(
		method = "teleportSomewhere",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Shulker;findAttachableSurface(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Direction;")
	)
	private Direction everythingBackport$notOntoBedrock(Shulker self, BlockPos target, Operation<Direction> original) {
		Direction direction = original.call(self, target);
		return direction != null && self.level().getBlockState(target.relative(direction)).is(Blocks.BEDROCK) ? null : direction;
	}
}
