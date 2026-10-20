package backport.everything_backport.wildernessbound;

import backport.everything_backport.registry.Reg;
import backport.everything_backport.wildernessbound.entity.CushionItem;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.component.MapDecorations;

public final class WildItems {
	public static final Item POPLAR_PLANKS = Reg.blockItem(WildBlocks.POPLAR_PLANKS);
	public static final Item POPLAR_SAPLING = Reg.blockItem(WildBlocks.POPLAR_SAPLING);
	public static final Item POPLAR_LOG = Reg.blockItem(WildBlocks.POPLAR_LOG);
	public static final Item STRIPPED_POPLAR_LOG = Reg.blockItem(WildBlocks.STRIPPED_POPLAR_LOG);
	public static final Item STRIPPED_POPLAR_WOOD = Reg.blockItem(WildBlocks.STRIPPED_POPLAR_WOOD);
	public static final Item POPLAR_WOOD = Reg.blockItem(WildBlocks.POPLAR_WOOD);
	public static final Item RED_POPLAR_LEAVES = Reg.blockItem(WildBlocks.RED_POPLAR_LEAVES);
	public static final Item ORANGE_POPLAR_LEAVES = Reg.blockItem(WildBlocks.ORANGE_POPLAR_LEAVES);
	public static final Item YELLOW_POPLAR_LEAVES = Reg.blockItem(WildBlocks.YELLOW_POPLAR_LEAVES);
	public static final Item RED_SHRUB = Reg.blockItem(WildBlocks.RED_SHRUB);
	public static final Item SHELF_MUSHROOM = Reg.blockItem(WildBlocks.SHELF_MUSHROOM);
	public static final Item POPLAR_SLAB = Reg.blockItem(WildBlocks.POPLAR_SLAB);
	public static final Item POPLAR_SHELF = Reg.blockItem(
		WildBlocks.POPLAR_SHELF, new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
	);
	public static final Item POPLAR_FENCE = Reg.blockItem(WildBlocks.POPLAR_FENCE);
	public static final Item POPLAR_STAIRS = Reg.blockItem(WildBlocks.POPLAR_STAIRS);
	public static final Item POPLAR_BUTTON = Reg.blockItem(WildBlocks.POPLAR_BUTTON);
	public static final Item POPLAR_PRESSURE_PLATE = Reg.blockItem(WildBlocks.POPLAR_PRESSURE_PLATE);
	public static final Item POPLAR_DOOR = Reg.blockItem(WildBlocks.POPLAR_DOOR, DoubleHighBlockItem::new);
	public static final Item POPLAR_TRAPDOOR = Reg.blockItem(WildBlocks.POPLAR_TRAPDOOR);
	public static final Item POPLAR_FENCE_GATE = Reg.blockItem(WildBlocks.POPLAR_FENCE_GATE);
	public static final Item POPLAR_BOAT = Reg.item("poplar_boat", properties -> new BoatItem(WildEntities.POPLAR_BOAT, properties), new Item.Properties().stacksTo(1));
	public static final Item POPLAR_CHEST_BOAT = Reg.item(
		"poplar_chest_boat", properties -> new BoatItem(WildEntities.POPLAR_CHEST_BOAT, properties), new Item.Properties().stacksTo(1)
	);
	public static final Item POPLAR_SIGN = Reg.blockItem(
		WildBlocks.POPLAR_SIGN, (block, properties) -> new SignItem(block, WildBlocks.POPLAR_WALL_SIGN, properties.stacksTo(16))
	);
	public static final Item POPLAR_HANGING_SIGN = Reg.blockItem(
		WildBlocks.POPLAR_HANGING_SIGN, (block, properties) -> new HangingSignItem(block, WildBlocks.POPLAR_WALL_HANGING_SIGN, properties.stacksTo(16))
	);
	public static final Item STRAW_BED = Reg.blockItem(WildBlocks.STRAW_BED, (block, properties) -> new BedItem(block, properties.stacksTo(16)));
	public static final Map<DyeColor, Item> WOOL_STAIRS = blockItems(WildBlocks.WOOL_STAIRS);
	public static final Map<DyeColor, Item> WOOL_SLAB = blockItems(WildBlocks.WOOL_SLAB);
	public static final Map<DyeColor, Item> CONCRETE_STAIRS = blockItems(WildBlocks.CONCRETE_STAIRS);
	public static final Map<DyeColor, Item> CONCRETE_SLAB = blockItems(WildBlocks.CONCRETE_SLAB);
	public static final Map<DyeColor, Item> CUSHIONS = cushions();

	/** 26.3's explorer maps: each its own item instead of a filled map with a name and a tint, in 26.3's item order. */
	public static final Item OCEAN_MONUMENT_MAP = map("ocean_monument_map");
	public static final Item WOODLAND_MANSION_MAP = map("woodland_mansion_map");
	public static final Item BURIED_TRIAL_CHAMBERS_MAP = map("buried_trial_chambers_map");
	public static final Item JUNGLE_PYRAMID_MAP = map("jungle_pyramid_map");
	public static final Item SWAMP_HUT_MAP = map("swamp_hut_map");
	public static final Item DESERT_VILLAGE_MAP = map("desert_village_map");
	public static final Item PLAINS_VILLAGE_MAP = map("plains_village_map");
	public static final Item SAVANNA_VILLAGE_MAP = map("savanna_village_map");
	public static final Item SNOWY_VILLAGE_MAP = map("snowy_village_map");
	public static final Item TAIGA_VILLAGE_MAP = map("taiga_village_map");
	public static final Item BURIED_TREASURE_MAP = map("buried_treasure_map");
	public static final Item BURIED_ANCIENT_CITY_MAP = map("buried_ancient_city_map");
	public static final Item BURIED_MINESHAFT_MAP = map("buried_mineshaft_map");
	public static final Item DESERT_PYRAMID_MAP = map("desert_pyramid_map");
	public static final Item ABANDONED_CAMP_MAP = map("abandoned_camp_map");
	public static final Item WARM_OCEAN_RUINS_MAP = map("warm_ocean_ruins_map");

	private WildItems() {
	}

	private static Map<DyeColor, Item> blockItems(Map<DyeColor, Block> blocks) {
		Map<DyeColor, Item> items = new EnumMap<>(DyeColor.class);
		blocks.forEach((color, block) -> items.put(color, Reg.blockItem(block)));
		return items;
	}

	private static Map<DyeColor, Item> cushions() {
		Map<DyeColor, Item> items = new LinkedHashMap<>();
		for (DyeColor color : DyeColor.values()) {
			items.put(color, Reg.item(color.getSerializedName() + "_cushion", properties -> new CushionItem(color, properties), new Item.Properties().stacksTo(16)));
		}

		return items;
	}

	private static Item map(String name) {
		return Reg.item(name, MapItem::new, new Item.Properties().component(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY));
	}

	static void init() {
	}
}
