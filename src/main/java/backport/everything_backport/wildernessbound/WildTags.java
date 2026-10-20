package backport.everything_backport.wildernessbound;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public final class WildTags {
	public static final TagKey<Block> REQUIRED_FOR_POPLAR_LEAF_AMBIENCE = block("required_for_poplar_leaf_ambience");
	public static final TagKey<Block> CUSHION_USES_COLLISION_SHAPE = block("cushion_uses_collision_shape");
	public static final TagKey<Block> POPLAR_LOGS = block("poplar_logs");
	public static final TagKey<Item> POPLAR_LOGS_ITEM = TagKey.create(Registries.ITEM, EverythingBackport.vanilla("poplar_logs"));

	private WildTags() {
	}

	private static TagKey<Block> block(String name) {
		return TagKey.create(Registries.BLOCK, EverythingBackport.vanilla(name));
	}

	public static TagKey<Structure> structure(String name) {
		return TagKey.create(Registries.STRUCTURE, EverythingBackport.vanilla(name));
	}
}
