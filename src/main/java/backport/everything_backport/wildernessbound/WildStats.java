package backport.everything_backport.wildernessbound;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public final class WildStats {
	public static final ResourceLocation SLEEP_IN_STRAW_BED = custom("sleep_in_straw_bed");

	private WildStats() {
	}

	/** 1.21.7's private {@code Stats.makeCustomStat}. */
	private static ResourceLocation custom(String name) {
		ResourceLocation id = EverythingBackport.vanilla(name);
		Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
		Stats.CUSTOM.get(id, StatFormatter.DEFAULT);
		return id;
	}

	static void init() {
	}
}
