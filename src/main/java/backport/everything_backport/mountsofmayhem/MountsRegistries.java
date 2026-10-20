package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilusVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class MountsRegistries {
	public static final ResourceKey<Registry<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = ResourceKey.createRegistryKey(
		EverythingBackport.vanilla("zombie_nautilus_variant")
	);

	private MountsRegistries() {
	}

	static void init() {
		DynamicRegistries.registerSynced(ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariant.DIRECT_CODEC, ZombieNautilusVariant.NETWORK_CODEC);
	}
}
