package backport.everything_backport.copperage;

import backport.everything_backport.EverythingBackport;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public final class CopperAgeMemories {
	public static final MemoryModuleType<Set<GlobalPos>> VISITED_BLOCK_POSITIONS = register(
		"visited_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList)
	);
	public static final MemoryModuleType<Set<GlobalPos>> UNREACHABLE_TRANSPORT_BLOCK_POSITIONS = register(
		"unreachable_transport_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList)
	);
	public static final MemoryModuleType<Integer> TRANSPORT_ITEMS_COOLDOWN_TICKS = register("transport_items_cooldown_ticks", null);

	private CopperAgeMemories() {
	}

	private static <U> MemoryModuleType<U> register(String name, Codec<U> codec) {
		return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, EverythingBackport.vanilla(name), new MemoryModuleType<>(Optional.ofNullable(codec)));
	}

	static void init() {
	}
}
