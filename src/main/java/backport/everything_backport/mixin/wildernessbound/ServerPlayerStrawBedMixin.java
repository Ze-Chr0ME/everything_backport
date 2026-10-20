package backport.everything_backport.mixin.wildernessbound;

import backport.everything_backport.wildernessbound.WildStats;
import backport.everything_backport.wildernessbound.block.StrawBedBlock;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.3's straw bed rule: sleeping in a straw bed never sets the respawn point, and counts as its own statistic. */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerStrawBedMixin {
	@Unique
	private boolean everythingBackport$sleepingInStrawBed;

	@Inject(method = "startSleepInBed", at = @At("HEAD"))
	private void everythingBackport$checkStrawBed(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
		this.everythingBackport$sleepingInStrawBed = ((ServerPlayer)(Object)this).level().getBlockState(pos).getBlock() instanceof StrawBedBlock;
	}

	@WrapWithCondition(
		method = "startSleepInBed",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V")
	)
	private boolean everythingBackport$keepRespawnPoint(ServerPlayer self, ServerPlayer.RespawnConfig config, boolean showMessage) {
		return !this.everythingBackport$sleepingInStrawBed;
	}

	@Inject(method = "startSleepInBed", at = @At("RETURN"))
	private void everythingBackport$clearStrawBed(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
		this.everythingBackport$sleepingInStrawBed = false;
	}

	/** The sleep statistic is awarded from a lambda inside {@code startSleepInBed}, while the flag above is still set. */
	@ModifyVariable(method = "awardStat(Lnet/minecraft/stats/Stat;I)V", at = @At("HEAD"), argsOnly = true)
	private Stat<?> everythingBackport$strawBedStat(Stat<?> stat) {
		return this.everythingBackport$sleepingInStrawBed && stat == Stats.CUSTOM.get(Stats.SLEEP_IN_BED) ? Stats.CUSTOM.get(WildStats.SLEEP_IN_STRAW_BED) : stat;
	}
}
