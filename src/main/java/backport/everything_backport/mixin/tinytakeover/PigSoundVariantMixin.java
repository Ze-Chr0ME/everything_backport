package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyComponents;
import backport.everything_backport.tinytakeover.TinyEntityData;
import backport.everything_backport.tinytakeover.TinyRegistries;
import backport.everything_backport.tinytakeover.sound.PigSoundVariant;
import backport.everything_backport.tinytakeover.sound.SoundVariants;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.1 pigs get a random voice (classic, big or mini), piglets their own sounds, and pigs make a sound when they eat. */
@Mixin(Pig.class)
public abstract class PigSoundVariantMixin extends Animal {
	@Unique
	private static final EntityDataAccessor<Holder<PigSoundVariant>> EVERYTHING_BACKPORT$SOUND_VARIANT = SynchedEntityData.defineId(
		Pig.class, TinyEntityData.PIG_SOUND_VARIANT
	);

	protected PigSoundVariantMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Unique
	private PigSoundVariant.PigSoundSet everythingBackport$sounds() {
		return this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT).value().sounds(this.isBaby());
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void everythingBackport$defineSoundVariant(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.defaultVariant(this.registryAccess(), TinyRegistries.PIG_SOUND_VARIANT));
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$saveSoundVariant(ValueOutput output, CallbackInfo ci) {
		this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)
			.unwrapKey()
			.ifPresent(key -> output.store("sound_variant", ResourceKey.codec(TinyRegistries.PIG_SOUND_VARIANT), key));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$readSoundVariant(ValueInput input, CallbackInfo ci) {
		input.read("sound_variant", ResourceKey.codec(TinyRegistries.PIG_SOUND_VARIANT))
			.flatMap(key -> this.registryAccess().lookupOrThrow(TinyRegistries.PIG_SOUND_VARIANT).get(key))
			.ifPresent(holder -> this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, holder));
	}

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	private void everythingBackport$pickSoundVariant(
		ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData groupData,
		CallbackInfoReturnable<SpawnGroupData> cir
	) {
		this.entityData.set(
			EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.pickRandom(this.registryAccess(), TinyRegistries.PIG_SOUND_VARIANT, level.getRandom())
		);
	}

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$getSoundVariant(DataComponentType<? extends T> type, CallbackInfoReturnable<T> cir) {
		if (type == TinyComponents.PIG_SOUND_VARIANT) {
			cir.setReturnValue(castComponentValue((DataComponentType<T>)type, this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)));
		}
	}

	@Inject(method = "applyImplicitComponents", at = @At("TAIL"))
	private void everythingBackport$applySoundVariants(DataComponentGetter getter, CallbackInfo ci) {
		this.applyImplicitComponentIfPresent(getter, TinyComponents.PIG_SOUND_VARIANT);
	}

	@Inject(method = "applyImplicitComponent", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$applySoundVariant(DataComponentType<T> type, T value, CallbackInfoReturnable<Boolean> cir) {
		if (type == TinyComponents.PIG_SOUND_VARIANT) {
			this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, castComponentValue(TinyComponents.PIG_SOUND_VARIANT, value));
			cir.setReturnValue(true);
		}
	}



	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PIG_AMBIENT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$ambient(SoundEvent original) {
		return this.everythingBackport$sounds().ambientSound().value();
	}

	@ModifyExpressionValue(method = "getHurtSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PIG_HURT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$hurt(SoundEvent original) {
		return this.everythingBackport$sounds().hurtSound().value();
	}

	@ModifyExpressionValue(method = "getDeathSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PIG_DEATH:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$death(SoundEvent original) {
		return this.everythingBackport$sounds().deathSound().value();
	}

	@ModifyExpressionValue(method = "playStepSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;PIG_STEP:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$step(SoundEvent original) {
		return this.everythingBackport$sounds().stepSound().value();
	}

	/** 26.1 pigs make a sound when fed; 1.21.7 pigs eat silently. */
	@Override
	public void playEatingSound() {
		this.makeSound(this.everythingBackport$sounds().eatSound().value());
	}
}
