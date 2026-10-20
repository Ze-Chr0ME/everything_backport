package backport.everything_backport.wildernessbound;

import backport.everything_backport.EverythingBackport;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

/** 26.3's explorer maps: their own items and the map icons of the structures they lead to. */
public final class WildMaps {
	public static final Holder<MapDecorationType> ABANDONED_CAMP = decoration("abandoned_camp", "abandoned_camp");
	public static final Holder<MapDecorationType> ANCIENT_CITY = decoration("ancient_city", "ancient_city");
	public static final Holder<MapDecorationType> DESERT_PYRAMID = decoration("desert_pyramid", "desert_pyramid");
	public static final Holder<MapDecorationType> MINESHAFT = decoration("mineshaft", "mineshaft");
	public static final Holder<MapDecorationType> OCEAN_RUIN_WARM = decoration("ocean_ruin_warm", "warm_ocean_ruins");

	/** The explorer map item 26.3 gives for maps that 1.21.7 makes as named filled maps, by the structure tag they search. */
	private static final Map<TagKey<Structure>, Item> BY_DESTINATION = Map.ofEntries(
		Map.entry(StructureTags.ON_OCEAN_EXPLORER_MAPS, WildItems.OCEAN_MONUMENT_MAP),
		Map.entry(StructureTags.ON_WOODLAND_EXPLORER_MAPS, WildItems.WOODLAND_MANSION_MAP),
		Map.entry(StructureTags.ON_TRIAL_CHAMBERS_MAPS, WildItems.BURIED_TRIAL_CHAMBERS_MAP),
		Map.entry(StructureTags.ON_JUNGLE_EXPLORER_MAPS, WildItems.JUNGLE_PYRAMID_MAP),
		Map.entry(StructureTags.ON_SWAMP_EXPLORER_MAPS, WildItems.SWAMP_HUT_MAP),
		Map.entry(StructureTags.ON_DESERT_VILLAGE_MAPS, WildItems.DESERT_VILLAGE_MAP),
		Map.entry(StructureTags.ON_PLAINS_VILLAGE_MAPS, WildItems.PLAINS_VILLAGE_MAP),
		Map.entry(StructureTags.ON_SAVANNA_VILLAGE_MAPS, WildItems.SAVANNA_VILLAGE_MAP),
		Map.entry(StructureTags.ON_SNOWY_VILLAGE_MAPS, WildItems.SNOWY_VILLAGE_MAP),
		Map.entry(StructureTags.ON_TAIGA_VILLAGE_MAPS, WildItems.TAIGA_VILLAGE_MAP),
		Map.entry(StructureTags.ON_TREASURE_MAPS, WildItems.BURIED_TREASURE_MAP)
	);
	private static final Set<Item> EXPLORER_MAPS = Set.of(
		WildItems.OCEAN_MONUMENT_MAP,
		WildItems.WOODLAND_MANSION_MAP,
		WildItems.BURIED_TRIAL_CHAMBERS_MAP,
		WildItems.JUNGLE_PYRAMID_MAP,
		WildItems.SWAMP_HUT_MAP,
		WildItems.DESERT_VILLAGE_MAP,
		WildItems.PLAINS_VILLAGE_MAP,
		WildItems.SAVANNA_VILLAGE_MAP,
		WildItems.SNOWY_VILLAGE_MAP,
		WildItems.TAIGA_VILLAGE_MAP,
		WildItems.BURIED_TREASURE_MAP,
		WildItems.BURIED_ANCIENT_CITY_MAP,
		WildItems.BURIED_MINESHAFT_MAP,
		WildItems.DESERT_PYRAMID_MAP,
		WildItems.ABANDONED_CAMP_MAP,
		WildItems.WARM_OCEAN_RUINS_MAP
	);

	private WildMaps() {
	}

	private static Holder<MapDecorationType> decoration(String name, String asset) {
		ResourceKey<MapDecorationType> key = ResourceKey.create(Registries.MAP_DECORATION_TYPE, EverythingBackport.vanilla(name));
		return Registry.registerForHolder(
			BuiltInRegistries.MAP_DECORATION_TYPE, key, new MapDecorationType(EverythingBackport.vanilla(asset), true, -1, false, false)
		);
	}

	public static boolean isExplorerMap(ItemStack stack) {
		return EXPLORER_MAPS.contains(stack.getItem());
	}

	/** Turns a filled map 1.21.7 made for this structure tag into 26.3's explorer map item, which carries its own name. */
	public static ItemStack toExplorerMap(ItemStack filledMap, TagKey<Structure> destination) {
		Item item = BY_DESTINATION.get(destination);
		if (item == null || !filledMap.is(Items.FILLED_MAP)) {
			return filledMap;
		}

		ItemStack map = filledMap.transmuteCopy(item, filledMap.getCount());
		map.remove(DataComponents.ITEM_NAME);
		map.remove(DataComponents.MAP_COLOR);
		return map;
	}

	static void init() {
	}
}
