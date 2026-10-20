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

/** A pig voice from the 26.1 {@code pig_sound_variant} registry, with separate piglet sounds. */
public record PigSoundVariant(PigSoundVariant.PigSoundSet adultSounds, PigSoundVariant.PigSoundSet babySounds) {
	public static final Codec<PigSoundVariant> DIRECT_CODEC = codec();
	public static final Codec<PigSoundVariant> NETWORK_CODEC = codec();
	public static final Codec<Holder<PigSoundVariant>> CODEC = RegistryFixedCodec.create(TinyRegistries.PIG_SOUND_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PigSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TinyRegistries.PIG_SOUND_VARIANT);

	private static Codec<PigSoundVariant> codec() {
		return RecordCodecBuilder.create(
			instance -> instance.group(
					PigSoundSet.CODEC.fieldOf("adult_sounds").forGetter(PigSoundVariant::adultSounds),
					PigSoundSet.CODEC.fieldOf("baby_sounds").forGetter(PigSoundVariant::babySounds)
				)
				.apply(instance, PigSoundVariant::new)
		);
	}

	public PigSoundSet sounds(boolean baby) {
		return baby ? this.babySounds : this.adultSounds;
	}

	public record PigSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound, Holder<SoundEvent> eatSound) {
		public static final Codec<PigSoundSet> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(PigSoundSet::ambientSound),
					SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(PigSoundSet::hurtSound),
					SoundEvent.CODEC.fieldOf("death_sound").forGetter(PigSoundSet::deathSound),
					SoundEvent.CODEC.fieldOf("step_sound").forGetter(PigSoundSet::stepSound),
					SoundEvent.CODEC.fieldOf("eat_sound").forGetter(PigSoundSet::eatSound)
				)
				.apply(instance, PigSoundSet::new)
		);
	}
}
