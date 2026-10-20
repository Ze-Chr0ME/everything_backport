package backport.everything_backport.tinytakeover.sound;

import backport.everything_backport.tinytakeover.TinyRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.sounds.SoundEvent;

/** A chicken voice from the 26.1 {@code chicken_sound_variant} registry, with separate chick sounds. */
public record ChickenSoundVariant(ChickenSoundVariant.ChickenSoundSet adultSounds, ChickenSoundVariant.ChickenSoundSet babySounds) {
	public static final Codec<ChickenSoundVariant> DIRECT_CODEC = codec();
	public static final Codec<ChickenSoundVariant> NETWORK_CODEC = codec();
	public static final Codec<Holder<ChickenSoundVariant>> CODEC = RegistryFixedCodec.create(TinyRegistries.CHICKEN_SOUND_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ChickenSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TinyRegistries.CHICKEN_SOUND_VARIANT);

	private static Codec<ChickenSoundVariant> codec() {
		return RecordCodecBuilder.create(
			instance -> instance.group(
					ChickenSoundSet.CODEC.fieldOf("adult_sounds").forGetter(ChickenSoundVariant::adultSounds),
					ChickenSoundSet.CODEC.fieldOf("baby_sounds").forGetter(ChickenSoundVariant::babySounds)
				)
				.apply(instance, ChickenSoundVariant::new)
		);
	}

	public ChickenSoundSet sounds(boolean baby) {
		return baby ? this.babySounds : this.adultSounds;
	}

	public record ChickenSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound) {
		public static final Codec<ChickenSoundSet> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(ChickenSoundSet::ambientSound),
					SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(ChickenSoundSet::hurtSound),
					SoundEvent.CODEC.fieldOf("death_sound").forGetter(ChickenSoundSet::deathSound),
					SoundEvent.CODEC.fieldOf("step_sound").forGetter(ChickenSoundSet::stepSound)
				)
				.apply(instance, ChickenSoundSet::new)
		);
	}
}
