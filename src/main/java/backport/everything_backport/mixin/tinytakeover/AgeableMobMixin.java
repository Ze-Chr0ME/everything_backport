package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLockable;
import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 26.1's golden dandelion age lock and per-mob baby start age on every ageable mob. */
@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob implements AgeLockable {
	@Unique
	private static final EntityDataAccessor<Boolean> EVERYTHING_BACKPORT$AGE_LOCKED = SynchedEntityData.defineId(AgeableMob.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private int everythingBackport$ageLockParticleTimer;

	protected AgeableMobMixin(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
	}

	@Shadow
	public abstract void setAge(int age);

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void everythingBackport$defineAgeLocked(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(EVERYTHING_BACKPORT$AGE_LOCKED, false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$saveAgeLocked(ValueOutput output, CallbackInfo ci) {
		output.putBoolean("AgeLocked", this.everythingBackport$isAgeLocked());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$readAgeLocked(ValueInput input, CallbackInfo ci) {
		this.everythingBackport$setAgeLocked(input.getBooleanOr("AgeLocked", false));
	}

	/** Only the growing-up branch is gated; adults still count their breeding cooldown down. */
	@WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AgeableMob;setAge(I)V", ordinal = 0))
	private void everythingBackport$growUnlessLocked(AgeableMob self, int age, Operation<Void> original) {
		if (AgeLocking.canAgeUp(self)) {
			original.call(self, age);
		}
	}

	@Inject(method = "aiStep", at = @At("TAIL"))
	private void everythingBackport$ageLockParticles(CallbackInfo ci) {
		this.everythingBackport$ageLockParticleTimer = AgeLocking.makeAgeLockedParticle(
			this.level(), this, this.everythingBackport$ageLockParticleTimer, this.everythingBackport$isAgeLocked()
		);
	}

	@ModifyConstant(method = {"finalizeSpawn", "setBaby"}, constant = @Constant(intValue = -24000))
	private int everythingBackport$babyStartAge(int original) {
		return AgeLocking.babyStartAge((AgeableMob)(Object)this);
	}

	@Override
	public boolean everythingBackport$isAgeLocked() {
		return this.entityData.get(EVERYTHING_BACKPORT$AGE_LOCKED);
	}

	@Override
	public void everythingBackport$setAgeLocked(boolean locked) {
		this.entityData.set(EVERYTHING_BACKPORT$AGE_LOCKED, locked);
	}

	@Override
	public int everythingBackport$getAgeLockParticleTimer() {
		return this.everythingBackport$ageLockParticleTimer;
	}

	@Override
	public void everythingBackport$toggleAgeLock() {
		this.everythingBackport$setAgeLocked(!this.everythingBackport$isAgeLocked());
		this.setAge(AgeLocking.babyStartAge((AgeableMob)(Object)this));
		this.everythingBackport$ageLockParticleTimer = AgeLocking.AGE_LOCK_COOLDOWN_TICKS;
	}

	@Override
	public boolean everythingBackport$canBeAgeLocked() {
		return this.isBaby();
	}
}
