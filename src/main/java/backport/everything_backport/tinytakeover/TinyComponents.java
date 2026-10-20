package backport.everything_backport.tinytakeover;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.tinytakeover.sound.CatSoundVariant;
import backport.everything_backport.tinytakeover.sound.ChickenSoundVariant;
import backport.everything_backport.tinytakeover.sound.CowSoundVariant;
import backport.everything_backport.tinytakeover.sound.PigSoundVariant;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/** Entity components for the 26.1 sound variants, so spawn eggs and buckets can carry them. */
public final class TinyComponents {
	public static final DataComponentType<Holder<PigSoundVariant>> PIG_SOUND_VARIANT = register(
		"pig/sound_variant", PigSoundVariant.CODEC, PigSoundVariant.STREAM_CODEC
	);
	public static final DataComponentType<Holder<CowSoundVariant>> COW_SOUND_VARIANT = register(
		"cow/sound_variant", CowSoundVariant.CODEC, CowSoundVariant.STREAM_CODEC
	);
	public static final DataComponentType<Holder<ChickenSoundVariant>> CHICKEN_SOUND_VARIANT = register(
		"chicken/sound_variant", ChickenSoundVariant.CODEC, ChickenSoundVariant.STREAM_CODEC
	);
	public static final DataComponentType<Holder<CatSoundVariant>> CAT_SOUND_VARIANT = register(
		"cat/sound_variant", CatSoundVariant.CODEC, CatSoundVariant.STREAM_CODEC
	);

	private TinyComponents() {
	}

	private static <T> DataComponentType<T> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			EverythingBackport.vanilla(name),
			DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build()
		);
	}

	static void init() {
	}
}
