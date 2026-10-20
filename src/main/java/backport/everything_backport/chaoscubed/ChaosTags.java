package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ChaosTags {
	public static final TagKey<Block> CAUSES_CONTINUOUS_GEYSER_ERUPTIONS = block("causes_continuous_geyser_eruptions");
	public static final TagKey<Block> CAUSES_PERIODIC_GEYSER_ERUPTIONS = block("causes_periodic_geyser_eruptions");
	public static final TagKey<Block> SPELEOTHEMS = block("speleothems");
	public static final TagKey<Block> SULFUR_SPIKE_REPLACEABLE_BLOCKS = block("sulfur_spike_replaceable_blocks");
	public static final TagKey<Block> SUPPRESSES_BOUNCE = block("suppresses_bounce");
	public static final TagKey<Item> SULFUR_CUBE_FOOD = item("sulfur_cube_food");
	public static final TagKey<Item> SULFUR_CUBE_SWALLOWABLE = item("sulfur_cube_swallowable");
	public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_EXPLOSIVE = item("sulfur_cube_archetype/explosive");
	public static final TagKey<EntityType<?>> NOT_AFFECTED_BY_GEYSERS = TagKey.create(Registries.ENTITY_TYPE, EverythingBackport.vanilla("not_affected_by_geysers"));
	public static final TagKey<DamageType> SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO = TagKey.create(
		Registries.DAMAGE_TYPE, EverythingBackport.vanilla("sulfur_cube_with_block_immune_to")
	);

	private ChaosTags() {
	}

	private static TagKey<Block> block(String name) {
		return TagKey.create(Registries.BLOCK, EverythingBackport.vanilla(name));
	}

	private static TagKey<Item> item(String name) {
		return TagKey.create(Registries.ITEM, EverythingBackport.vanilla(name));
	}
}
