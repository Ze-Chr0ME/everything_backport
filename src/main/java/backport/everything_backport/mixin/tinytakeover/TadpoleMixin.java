package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLockable;
import backport.everything_backport.tinytakeover.AgeLocking;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Tadpoles can be kept from turning into frogs with a golden dandelion (26.1). */
@Mixin(Tadpole.class)
public abstract class TadpoleMixin extends AbstractFish implements AgeLockable {
	@Unique
	private static final EntityDataAccessor<Boolean> EVERYTHING_BACKPORT$AGE_LOCKED = SynchedEntityData.defineId(Tadpole.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private int everythingBackport$ageLockParticleTimer;

	protected TadpoleMixin(EntityType<? extends AbstractFish> type, Level level) {
		super(type, level);
	}

	@Shadow
	private void setAge(int age) {
		throw new AssertionError();
	}

	@Override
	public void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(EVERYTHING_BACKPORT$AGE_LOCKED, false);
	}

	@WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Tadpole;setAge(I)V"))
	private void everythingBackport$growUnlessLocked(Tadpole self, int age, Operation<Void> original) {
		if (!this.everythingBackport$isAgeLocked()) {
			original.call(self, age);
		}
	}

	@Inject(method = "aiStep", at = @At("TAIL"))
	private void everythingBackport$ageLockParticles(CallbackInfo ci) {
		this.everythingBackport$ageLockParticleTimer = AgeLocking.makeAgeLockedParticle(
			this.level(), this, this.everythingBackport$ageLockParticleTimer, this.everythingBackport$isAgeLocked()
		);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$saveAgeLocked(ValueOutput output, CallbackInfo ci) {
		output.putBoolean("AgeLocked", this.everythingBackport$isAgeLocked());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$readAgeLocked(ValueInput input, CallbackInfo ci) {
		this.everythingBackport$setAgeLocked(input.getBooleanOr("AgeLocked", false));
	}

	@Inject(method = "saveToBucketTag", at = @At("TAIL"))
	private void everythingBackport$bucketAgeLocked(ItemStack bucket, CallbackInfo ci) {
		net.minecraft.world.item.component.CustomData.update(
			net.minecraft.core.component.DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> tag.putBoolean("AgeLocked", this.everythingBackport$isAgeLocked())
		);
	}

	@Inject(method = "loadFromBucketTag", at = @At("TAIL"))
	private void everythingBackport$unbucketAgeLocked(CompoundTag tag, CallbackInfo ci) {
		this.everythingBackport$setAgeLocked(tag.getBooleanOr("AgeLocked", false));
	}

	/** A locked tadpole can't be fed to speed up growing. */
	@ModifyExpressionValue(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Tadpole;isFood(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean everythingBackport$noFeedingWhenLocked(boolean isFood) {
		return isFood && !this.everythingBackport$isAgeLocked();
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
		this.setAge(0);
		this.everythingBackport$ageLockParticleTimer = AgeLocking.AGE_LOCK_COOLDOWN_TICKS;
	}

	@Override
	public boolean everythingBackport$canBeAgeLocked() {
		return true;
	}
}
