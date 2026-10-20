package backport.everything_backport.tinytakeover;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.tinytakeover.sound.CatSoundVariant;
import backport.everything_backport.tinytakeover.sound.ChickenSoundVariant;
import backport.everything_backport.tinytakeover.sound.CowSoundVariant;
import backport.everything_backport.tinytakeover.sound.PigSoundVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/** The data-driven sound variant registries added in 26.1. */
public final class TinyRegistries {
	public static final ResourceKey<Registry<CatSoundVariant>> CAT_SOUND_VARIANT = ResourceKey.createRegistryKey(EverythingBackport.vanilla("cat_sound_variant"));
	public static final ResourceKey<Registry<ChickenSoundVariant>> CHICKEN_SOUND_VARIANT = ResourceKey.createRegistryKey(
		EverythingBackport.vanilla("chicken_sound_variant")
	);
	public static final ResourceKey<Registry<CowSoundVariant>> COW_SOUND_VARIANT = ResourceKey.createRegistryKey(EverythingBackport.vanilla("cow_sound_variant"));
	public static final ResourceKey<Registry<PigSoundVariant>> PIG_SOUND_VARIANT = ResourceKey.createRegistryKey(EverythingBackport.vanilla("pig_sound_variant"));

	private TinyRegistries() {
	}

	static void init() {
		DynamicRegistries.registerSynced(CAT_SOUND_VARIANT, CatSoundVariant.DIRECT_CODEC, CatSoundVariant.NETWORK_CODEC);
		DynamicRegistries.registerSynced(CHICKEN_SOUND_VARIANT, ChickenSoundVariant.DIRECT_CODEC, ChickenSoundVariant.NETWORK_CODEC);
		DynamicRegistries.registerSynced(COW_SOUND_VARIANT, CowSoundVariant.DIRECT_CODEC, CowSoundVariant.NETWORK_CODEC);
		DynamicRegistries.registerSynced(PIG_SOUND_VARIANT, PigSoundVariant.DIRECT_CODEC, PigSoundVariant.NETWORK_CODEC);
	}
}
