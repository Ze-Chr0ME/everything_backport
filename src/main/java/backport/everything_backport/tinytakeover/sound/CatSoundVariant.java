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

/** A cat voice from the 26.1 {@code cat_sound_variant} registry, with separate kitten sounds. */
public record CatSoundVariant(CatSoundVariant.CatSoundSet adultSounds, CatSoundVariant.CatSoundSet babySounds) {
	public static final Codec<CatSoundVariant> DIRECT_CODEC = codec();
	public static final Codec<CatSoundVariant> NETWORK_CODEC = codec();
	public static final Codec<Holder<CatSoundVariant>> CODEC = RegistryFixedCodec.create(TinyRegistries.CAT_SOUND_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CatSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TinyRegistries.CAT_SOUND_VARIANT);

	private static Codec<CatSoundVariant> codec() {
		return RecordCodecBuilder.create(
			instance -> instance.group(
					CatSoundSet.CODEC.fieldOf("adult_sounds").forGetter(CatSoundVariant::adultSounds),
					CatSoundSet.CODEC.fieldOf("baby_sounds").forGetter(CatSoundVariant::babySounds)
				)
				.apply(instance, CatSoundVariant::new)
		);
	}

	public CatSoundSet sounds(boolean baby) {
		return baby ? this.babySounds : this.adultSounds;
	}

	public record CatSoundSet(Holder<SoundEvent> ambientSound, Holder<SoundEvent> strayAmbientSound, Holder<SoundEvent> hissSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> eatSound, Holder<SoundEvent> begForFoodSound, Holder<SoundEvent> purrSound, Holder<SoundEvent> purreowSound) {
		public static final Codec<CatSoundSet> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CatSoundSet::ambientSound),
					SoundEvent.CODEC.fieldOf("stray_ambient_sound").forGetter(CatSoundSet::strayAmbientSound),
					SoundEvent.CODEC.fieldOf("hiss_sound").forGetter(CatSoundSet::hissSound),
					SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CatSoundSet::hurtSound),
					SoundEvent.CODEC.fieldOf("death_sound").forGetter(CatSoundSet::deathSound),
					SoundEvent.CODEC.fieldOf("eat_sound").forGetter(CatSoundSet::eatSound),
					SoundEvent.CODEC.fieldOf("beg_for_food_sound").forGetter(CatSoundSet::begForFoodSound),
					SoundEvent.CODEC.fieldOf("purr_sound").forGetter(CatSoundSet::purrSound),
					SoundEvent.CODEC.fieldOf("purreow_sound").forGetter(CatSoundSet::purreowSound)
				)
				.apply(instance, CatSoundSet::new)
		);
	}
}
