package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.chaoscubed.entity.SulfurCubeArchetype;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/** The data-driven sulfur cube archetype registry added in 26.2, synced to clients as 26.2 does. */
public final class ChaosRegistries {
	public static final ResourceKey<Registry<SulfurCubeArchetype>> SULFUR_CUBE_ARCHETYPE = ResourceKey.createRegistryKey(
		EverythingBackport.vanilla("sulfur_cube_archetype")
	);

	private ChaosRegistries() {
	}

	static void init() {
		DynamicRegistries.registerSynced(SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetype.DIRECT_CODEC);
	}
}
