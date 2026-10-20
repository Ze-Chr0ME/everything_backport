package backport.everything_backport.mixin.wildernessbound;

import backport.everything_backport.wildernessbound.block.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 26.3's straw beds: sleepers lie on the low straw rather than at bed height, and the bed crumbles once they get up. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityStrawBedMixin {
	@Unique
	private @Nullable BlockPos everythingBackport$leftStrawBed;

	@Inject(method = "setPosToBed", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$lieOnStraw(BlockPos pos, CallbackInfo ci) {
		LivingEntity self = (LivingEntity)(Object)this;
		if (self.level().getBlockState(pos).getBlock() instanceof StrawBedBlock) {
			self.setPos(pos.getX() + 0.5, pos.getY() + StrawBedBlock.SLEEP_HEIGHT, pos.getZ() + 0.5);
			ci.cancel();
		}
	}

	@Inject(method = "stopSleeping", at = @At("HEAD"))
	private void everythingBackport$rememberStrawBed(CallbackInfo ci) {
		LivingEntity self = (LivingEntity)(Object)this;
		Level level = self.level();
		this.everythingBackport$leftStrawBed = level.isClientSide()
			? null
			: self.getSleepingPos().filter(level::hasChunkAt).filter(pos -> level.getBlockState(pos).getBlock() instanceof StrawBedBlock).orElse(null);
	}

	@Inject(method = "stopSleeping", at = @At("TAIL"))
	private void everythingBackport$destroyStrawBed(CallbackInfo ci) {
		BlockPos pos = this.everythingBackport$leftStrawBed;
		if (pos != null) {
			this.everythingBackport$leftStrawBed = null;
			StrawBedBlock.destroyBed(((LivingEntity)(Object)this).level(), pos);
		}
	}
}
