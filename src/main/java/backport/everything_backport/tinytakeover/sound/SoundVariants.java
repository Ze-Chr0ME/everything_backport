package backport.everything_backport.tinytakeover.sound;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;

/** Default and spawn-time choice of a 26.1 sound variant. */
public final class SoundVariants {
	private SoundVariants() {
	}

	public static <T> ResourceKey<T> classic(ResourceKey<? extends Registry<T>> registry) {
		return ResourceKey.create(registry, EverythingBackport.vanilla("classic"));
	}

	/** The classic voice, which every entity starts with until it spawns naturally. */
	public static <T> Holder<T> defaultVariant(RegistryAccess access, ResourceKey<? extends Registry<T>> registry) {
		Registry<T> variants = access.lookupOrThrow(registry);
		return variants.get(classic(registry)).<Holder<T>>map(holder -> holder).or(variants::getAny).orElseThrow();
	}

	public static <T> Holder<T> pickRandom(RegistryAccess access, ResourceKey<? extends Registry<T>> registry, RandomSource random) {
		return access.lookupOrThrow(registry).getRandom(random).<Holder<T>>map(holder -> holder).orElseThrow();
	}
}
