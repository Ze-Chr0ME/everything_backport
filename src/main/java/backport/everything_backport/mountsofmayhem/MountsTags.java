package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class MountsTags {
	private MountsTags() {
	}

	public static final class Items {
		public static final TagKey<Item> ZOMBIE_HORSE_FOOD = create("zombie_horse_food");
		public static final TagKey<Item> CAMEL_HUSK_FOOD = create("camel_husk_food");
		public static final TagKey<Item> NAUTILUS_BUCKET_FOOD = create("nautilus_bucket_food");
		public static final TagKey<Item> NAUTILUS_FOOD = create("nautilus_food");
		public static final TagKey<Item> NAUTILUS_TAMING_ITEMS = create("nautilus_taming_items");
		public static final TagKey<Item> SPEARS = create("spears");
		public static final TagKey<Item> MELEE_WEAPON_ENCHANTABLE = create("enchantable/melee_weapon");
		public static final TagKey<Item> SWEEPING_ENCHANTABLE = create("enchantable/sweeping");
		public static final TagKey<Item> LUNGE_ENCHANTABLE = create("enchantable/lunge");

		private static TagKey<Item> create(String name) {
			return TagKey.create(Registries.ITEM, EverythingBackport.vanilla(name));
		}
	}

	public static final class EntityTypes {
		public static final TagKey<EntityType<?>> BURN_IN_DAYLIGHT = create("burn_in_daylight");
		public static final TagKey<EntityType<?>> CAN_WEAR_NAUTILUS_ARMOR = create("can_wear_nautilus_armor");
		public static final TagKey<EntityType<?>> NAUTILUS_HOSTILES = create("nautilus_hostiles");
		public static final TagKey<EntityType<?>> CAN_FLOAT_WHILE_RIDDEN = create("can_float_while_ridden");

		private static TagKey<EntityType<?>> create(String name) {
			return TagKey.create(Registries.ENTITY_TYPE, EverythingBackport.vanilla(name));
		}
	}

	public static final class Blocks {
		public static final TagKey<Block> CAN_GLIDE_THROUGH = TagKey.create(Registries.BLOCK, EverythingBackport.vanilla("can_glide_through"));
	}

	public static final class Biomes {
		public static final TagKey<Biome> SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS = TagKey.create(
			Registries.BIOME, EverythingBackport.vanilla("spawns_coral_variant_zombie_nautilus")
		);
	}
}
