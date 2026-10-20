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

/** A cow voice from the 26.1 {@code cow_sound_variant} registry; calves sound like adults. */
public record CowSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> stepSound) {
	public static final Codec<CowSoundVariant> DIRECT_CODEC = codec();
	public static final Codec<CowSoundVariant> NETWORK_CODEC = codec();
	public static final Codec<Holder<CowSoundVariant>> CODEC = RegistryFixedCodec.create(TinyRegistries.COW_SOUND_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CowSoundVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(TinyRegistries.COW_SOUND_VARIANT);

	private static Codec<CowSoundVariant> codec() {
		return RecordCodecBuilder.create(
			instance -> instance.group(
					SoundEvent.CODEC.fieldOf("ambient_sound").forGetter(CowSoundVariant::ambientSound),
					SoundEvent.CODEC.fieldOf("hurt_sound").forGetter(CowSoundVariant::hurtSound),
					SoundEvent.CODEC.fieldOf("death_sound").forGetter(CowSoundVariant::deathSound),
					SoundEvent.CODEC.fieldOf("step_sound").forGetter(CowSoundVariant::stepSound)
				)
				.apply(instance, CowSoundVariant::new)
		);
	}
}
