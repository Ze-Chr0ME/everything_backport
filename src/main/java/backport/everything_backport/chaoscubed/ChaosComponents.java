package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.chaoscubed.entity.SulfurCubeContent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ChaosComponents {
	public static final DataComponentType<SulfurCubeContent> SULFUR_CUBE_CONTENT = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		EverythingBackport.vanilla("sulfur_cube_content"),
		DataComponentType.<SulfurCubeContent>builder().persistent(SulfurCubeContent.CODEC).networkSynchronized(SulfurCubeContent.STREAM_CODEC).cacheEncoding().build()
	);

	private ChaosComponents() {
	}

	static void init() {
	}
}
