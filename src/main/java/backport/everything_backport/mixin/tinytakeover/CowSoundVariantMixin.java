package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyComponents;
import backport.everything_backport.tinytakeover.TinyEntityData;
import backport.everything_backport.tinytakeover.TinyRegistries;
import backport.everything_backport.tinytakeover.sound.CowSoundHolder;
import backport.everything_backport.tinytakeover.sound.CowSoundVariant;
import backport.everything_backport.tinytakeover.sound.SoundVariants;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractCow;
import net.minecraft.world.entity.animal.Cow;
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

/** 26.1 cows get a random voice (classic or moody); the sounds are swapped in by AbstractCowSoundMixin. */
@Mixin(Cow.class)
public abstract class CowSoundVariantMixin extends AbstractCow implements CowSoundHolder {
	@Unique
	private static final EntityDataAccessor<Holder<CowSoundVariant>> EVERYTHING_BACKPORT$SOUND_VARIANT = SynchedEntityData.defineId(
		Cow.class, TinyEntityData.COW_SOUND_VARIANT
	);

	protected CowSoundVariantMixin(EntityType<? extends AbstractCow> type, Level level) {
		super(type, level);
	}

	@Unique
	private CowSoundVariant everythingBackport$sounds() {
		return this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT).value();
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void everythingBackport$defineSoundVariant(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.defaultVariant(this.registryAccess(), TinyRegistries.COW_SOUND_VARIANT));
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$saveSoundVariant(ValueOutput output, CallbackInfo ci) {
		this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)
			.unwrapKey()
			.ifPresent(key -> output.store("sound_variant", ResourceKey.codec(TinyRegistries.COW_SOUND_VARIANT), key));
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void everythingBackport$readSoundVariant(ValueInput input, CallbackInfo ci) {
		input.read("sound_variant", ResourceKey.codec(TinyRegistries.COW_SOUND_VARIANT))
			.flatMap(key -> this.registryAccess().lookupOrThrow(TinyRegistries.COW_SOUND_VARIANT).get(key))
			.ifPresent(holder -> this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, holder));
	}

	@Inject(method = "finalizeSpawn", at = @At("HEAD"))
	private void everythingBackport$pickSoundVariant(
		ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData groupData,
		CallbackInfoReturnable<SpawnGroupData> cir
	) {
		this.entityData.set(
			EVERYTHING_BACKPORT$SOUND_VARIANT, SoundVariants.pickRandom(this.registryAccess(), TinyRegistries.COW_SOUND_VARIANT, level.getRandom())
		);
	}

	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$getSoundVariant(DataComponentType<? extends T> type, CallbackInfoReturnable<T> cir) {
		if (type == TinyComponents.COW_SOUND_VARIANT) {
			cir.setReturnValue(castComponentValue((DataComponentType<T>)type, this.entityData.get(EVERYTHING_BACKPORT$SOUND_VARIANT)));
		}
	}

	@Inject(method = "applyImplicitComponents", at = @At("TAIL"))
	private void everythingBackport$applySoundVariants(DataComponentGetter getter, CallbackInfo ci) {
		this.applyImplicitComponentIfPresent(getter, TinyComponents.COW_SOUND_VARIANT);
	}

	@Inject(method = "applyImplicitComponent", at = @At("HEAD"), cancellable = true)
	private <T> void everythingBackport$applySoundVariant(DataComponentType<T> type, T value, CallbackInfoReturnable<Boolean> cir) {
		if (type == TinyComponents.COW_SOUND_VARIANT) {
			this.entityData.set(EVERYTHING_BACKPORT$SOUND_VARIANT, castComponentValue(TinyComponents.COW_SOUND_VARIANT, value));
			cir.setReturnValue(true);
		}
	}



	@Override
	public CowSoundVariant everythingBackport$cowSounds() {
		return this.everythingBackport$sounds();
	}
}
