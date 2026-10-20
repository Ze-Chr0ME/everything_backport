package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyComponents;
import backport.everything_backport.tinytakeover.TinyEntityData;
import backport.everything_backport.tinytakeover.TinyRegistries;
import backport.everything_backport.tinytakeover.sound.CatSoundVariant;
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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
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

/** 26.1 cats get a random voice (classic or royal) and kittens their own sounds. */
@Mixin(Cat.class)
public abstract class CatSoundVariantMixin extends TamableAnimal {
	@Unique
	private static final EntityDataAccessor<Holder<CatSoundVariant>> EVERYTHING_BACKPORT$SOUND_VARIANT = SynchedEntityData.defineId(
		Cat.class, TinyEntityData.CAT_SOUND_VARIANT
	);

	protected CatSoundVariantMixin(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
	}

	@Unique
	private CatSoundVariant.CatSoundSet everythingBackport$sounds() {
		return this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT).value().sounds(this.isBaby());
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void everythingBackport$defineSoundVariant(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.defaultVariant(this.registryAccess(), TinyRegistries.CAT_SOUND_VARIANT));
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$saveSoundVariant(ValueOutput output, CallbackInfo ci) {
		this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)
			.unwrapKey()
			.ifPresent(key -> output.store("sound_variant", ResourceKey.codec(TinyRegistries.CAT_SOUND_VARIANT), key));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$readSoundVariant(ValueInput input, CallbackInfo ci) {
		input.read("sound_variant", ResourceKey.codec(TinyRegistries.CAT_SOUND_VARIANT))
			.flatMap(key -> this.registryAccess().lookupOrThrow(TinyRegistries.CAT_SOUND_VARIANT).get(key))
			.ifPresent(holder -> this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, holder));
	}

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	private void everythingBackport$pickSoundVariant(
		ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData groupData,
		CallbackInfoReturnable<SpawnGroupData> cir
	) {
		this.entityData.set(
			EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.pickRandom(this.registryAccess(), TinyRegistries.CAT_SOUND_VARIANT, level.getRandom())
		);
	}

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$getSoundVariant(DataComponentType<? extends T> type, CallbackInfoReturnable<T> cir) {
		if (type == TinyComponents.CAT_SOUND_VARIANT) {
			cir.setReturnValue(castComponentValue((DataComponentType<T>)type, this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)));
		}
	}

	@Inject(method = "applyImplicitComponents", at = @At("TAIL"))
	private void everythingBackport$applySoundVariants(DataComponentGetter getter, CallbackInfo ci) {
		this.applyImplicitComponentIfPresent(getter, TinyComponents.CAT_SOUND_VARIANT);
	}

	@Inject(method = "applyImplicitComponent", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$applySoundVariant(DataComponentType<T> type, T value, CallbackInfoReturnable<Boolean> cir) {
		if (type == TinyComponents.CAT_SOUND_VARIANT) {
			this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, castComponentValue(TinyComponents.CAT_SOUND_VARIANT, value));
			cir.setReturnValue(true);
		}
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_PURR:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$purr(SoundEvent original) {
		return this.everythingBackport$sounds().purrSound().value();
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_PURREOW:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$purreow(SoundEvent original) {
		return this.everythingBackport$sounds().purreowSound().value();
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_AMBIENT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$ambient(SoundEvent original) {
		return this.everythingBackport$sounds().ambientSound().value();
	}

	@ModifyExpressionValue(method = "getAmbientSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_STRAY_AMBIENT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$strayAmbient(SoundEvent original) {
		return this.everythingBackport$sounds().strayAmbientSound().value();
	}

	@ModifyExpressionValue(method = "hiss", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_HISS:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$hiss(SoundEvent original) {
		return this.everythingBackport$sounds().hissSound().value();
	}

	@ModifyExpressionValue(method = "getHurtSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_HURT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$hurt(SoundEvent original) {
		return this.everythingBackport$sounds().hurtSound().value();
	}

	@ModifyExpressionValue(method = "getDeathSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_DEATH:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$death(SoundEvent original) {
		return this.everythingBackport$sounds().deathSound().value();
	}

	@ModifyExpressionValue(method = "playEatingSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_EAT:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$eat(SoundEvent original) {
		return this.everythingBackport$sounds().eatSound().value();
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_BEG_FOR_FOOD:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$begForFood(SoundEvent original) {
		return this.everythingBackport$sounds().begForFoodSound().value();
	}

	@ModifyExpressionValue(method = "handleLieDown", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;CAT_PURR:Lnet/minecraft/sounds/SoundEvent;"))
	private SoundEvent everythingBackport$lyingPurr(SoundEvent original) {
		return this.everythingBackport$sounds().purrSound().value();
	}
}
