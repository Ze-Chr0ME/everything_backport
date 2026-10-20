package backport.everything_backport.mountsofmayhem.entity;

import backport.everything_backport.mountsofmayhem.MountsRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.TemperatureVariants;

/** The variants themselves are loaded from {@code data/minecraft/zombie_nautilus_variant}. */
public class ZombieNautilusVariants {
	public static final ResourceKey<ZombieNautilusVariant> TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
	public static final ResourceKey<ZombieNautilusVariant> WARM = createKey(TemperatureVariants.WARM);
	public static final ResourceKey<ZombieNautilusVariant> DEFAULT = TEMPERATE;

	private static ResourceKey<ZombieNautilusVariant> createKey(ResourceLocation id) {
		return ResourceKey.create(MountsRegistries.ZOMBIE_NAUTILUS_VARIANT, id);
	}
}
