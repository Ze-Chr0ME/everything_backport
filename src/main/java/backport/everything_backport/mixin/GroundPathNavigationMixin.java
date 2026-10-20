package backport.everything_backport.mixin;

import backport.everything_backport.copperage.entity.BelowSurfacePathing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Lets the copper golem path to chests that are not on the surface. */
@Mixin(GroundPathNavigation.class)
public abstract class GroundPathNavigationMixin extends PathNavigation implements BelowSurfacePathing {
	@Unique
	private boolean everythingBackport$canPathToTargetsBelowSurface;

	private GroundPathNavigationMixin(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	public void everythingBackport$setCanPathToTargetsBelowSurface(boolean canPathToTargetsBelowSurface) {
		this.everythingBackport$canPathToTargetsBelowSurface = canPathToTargetsBelowSurface;
	}

	@Inject(method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$pathBelowSurface(BlockPos blockPos, int accuracy, CallbackInfoReturnable<Path> cir) {
		if (this.everythingBackport$canPathToTargetsBelowSurface
			&& this.level.getChunkSource().getChunkNow(blockPos.getX() >> 4, blockPos.getZ() >> 4) != null) {
			cir.setReturnValue(super.createPath(blockPos, accuracy));
		}
	}
}
