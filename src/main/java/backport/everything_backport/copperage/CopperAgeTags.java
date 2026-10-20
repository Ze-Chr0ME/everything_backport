package backport.everything_backport.copperage;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class CopperAgeTags {
	private CopperAgeTags() {
	}

	public static final class Blocks {
		public static final TagKey<Block> BARS = create("bars");
		public static final TagKey<Block> CHAINS = create("chains");
		public static final TagKey<Block> COPPER = create("copper");
		public static final TagKey<Block> COPPER_CHESTS = create("copper_chests");
		public static final TagKey<Block> COPPER_GOLEM_STATUES = create("copper_golem_statues");
		public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = create("incorrect_for_copper_tool");
		public static final TagKey<Block> LANTERNS = create("lanterns");
		public static final TagKey<Block> LIGHTNING_RODS = create("lightning_rods");
		public static final TagKey<Block> WOODEN_SHELVES = create("wooden_shelves");

		private static TagKey<Block> create(String name) {
			return TagKey.create(Registries.BLOCK, EverythingBackport.vanilla(name));
		}
	}

	public static final class Items {
		public static final TagKey<Item> BARS = create("bars");
		public static final TagKey<Item> CHAINS = create("chains");
		public static final TagKey<Item> COPPER = create("copper");
		public static final TagKey<Item> COPPER_CHESTS = create("copper_chests");
		public static final TagKey<Item> COPPER_GOLEM_STATUES = create("copper_golem_statues");
		public static final TagKey<Item> COPPER_TOOL_MATERIALS = create("copper_tool_materials");
		public static final TagKey<Item> LANTERNS = create("lanterns");
		public static final TagKey<Item> LIGHTNING_RODS = create("lightning_rods");
		public static final TagKey<Item> REPAIRS_COPPER_ARMOR = create("repairs_copper_armor");
		public static final TagKey<Item> SHEARABLE_FROM_COPPER_GOLEM = create("shearable_from_copper_golem");
		public static final TagKey<Item> WOODEN_SHELVES = create("wooden_shelves");

		private static TagKey<Item> create(String name) {
			return TagKey.create(Registries.ITEM, EverythingBackport.vanilla(name));
		}
	}

	public static final class EntityTypes {
		public static final TagKey<EntityType<?>> ACCEPTS_IRON_GOLEM_GIFT = create("accepts_iron_golem_gift");
		public static final TagKey<EntityType<?>> CANDIDATE_FOR_IRON_GOLEM_GIFT = create("candidate_for_iron_golem_gift");
		public static final TagKey<EntityType<?>> CANNOT_BE_PUSHED_ONTO_BOATS = create("cannot_be_pushed_onto_boats");

		private static TagKey<EntityType<?>> create(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, EverythingBackport.vanilla(name));
		}
	}
}
