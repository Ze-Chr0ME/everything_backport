package backport.everything_backport.wildernessbound;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.copperage.CopperAgeBlockEntities;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

/** Wilderness Bound (Java Edition 26.3). */
public final class WildernessBound {
	/** 1.21.7's creative inventory order for dyed blocks. */
	private static final List<DyeColor> COLOR_ORDER = List.of(
		DyeColor.WHITE,
		DyeColor.LIGHT_GRAY,
		DyeColor.GRAY,
		DyeColor.BLACK,
		DyeColor.BROWN,
		DyeColor.RED,
		DyeColor.ORANGE,
		DyeColor.YELLOW,
		DyeColor.LIME,
		DyeColor.GREEN,
		DyeColor.CYAN,
		DyeColor.LIGHT_BLUE,
		DyeColor.BLUE,
		DyeColor.PURPLE,
		DyeColor.MAGENTA,
		DyeColor.PINK
	);

	private WildernessBound() {
	}

	public static void init() {
		WildSounds.init();
		WildParticles.init();
		WildBlocks.init();
		WildEntities.init();
		WildItems.init();
		WildMaps.init();
		WildWorldgen.init();
		WildStats.init();
		BlockEntityType.SIGN.addSupportedBlock(WildBlocks.POPLAR_SIGN);
		BlockEntityType.SIGN.addSupportedBlock(WildBlocks.POPLAR_WALL_SIGN);
		BlockEntityType.HANGING_SIGN.addSupportedBlock(WildBlocks.POPLAR_HANGING_SIGN);
		BlockEntityType.HANGING_SIGN.addSupportedBlock(WildBlocks.POPLAR_WALL_HANGING_SIGN);
		CopperAgeBlockEntities.SHELF.addSupportedBlock(WildBlocks.POPLAR_SHELF);
		DispenserBlock.registerBehavior(WildItems.POPLAR_BOAT, new BoatDispenseItemBehavior(WildEntities.POPLAR_BOAT));
		DispenserBlock.registerBehavior(WildItems.POPLAR_CHEST_BOAT, new BoatDispenseItemBehavior(WildEntities.POPLAR_CHEST_BOAT));
		registerBlockBehaviors();
		registerTrades();
		registerCreativeTabs();
	}

	private static void registerBlockBehaviors() {
		StrippableBlockRegistry.register(WildBlocks.POPLAR_LOG, WildBlocks.STRIPPED_POPLAR_LOG);
		StrippableBlockRegistry.register(WildBlocks.POPLAR_WOOD, WildBlocks.STRIPPED_POPLAR_WOOD);

		// 26.3's FireBlock.bootStrap.
		FlammableBlockRegistry fire = FlammableBlockRegistry.getDefaultInstance();
		for (Block block : List.of(WildBlocks.POPLAR_PLANKS, WildBlocks.POPLAR_SLAB, WildBlocks.POPLAR_FENCE_GATE, WildBlocks.POPLAR_FENCE, WildBlocks.POPLAR_STAIRS)) {
			fire.add(block, 5, 20);
		}

		for (Block block : List.of(WildBlocks.POPLAR_LOG, WildBlocks.STRIPPED_POPLAR_LOG, WildBlocks.STRIPPED_POPLAR_WOOD, WildBlocks.POPLAR_WOOD)) {
			fire.add(block, 5, 5);
		}

		for (Block block : List.of(WildBlocks.RED_POPLAR_LEAVES, WildBlocks.ORANGE_POPLAR_LEAVES, WildBlocks.YELLOW_POPLAR_LEAVES, WildBlocks.STRAW_BED)) {
			fire.add(block, 30, 60);
		}

		fire.add(WildBlocks.RED_SHRUB, 60, 100);
		WildBlocks.WOOL_STAIRS.values().forEach(block -> fire.add(block, 30, 60));
		WildBlocks.WOOL_SLAB.values().forEach(block -> fire.add(block, 30, 60));
		fire.add(WildBlocks.POPLAR_SHELF, 30, 20);

		// 26.3's compostable item components: low 30%, medium 65%, medium high 85%.
		CompostingChanceRegistry compost = CompostingChanceRegistry.INSTANCE;
		for (Item item : List.of(WildItems.POPLAR_SAPLING, WildItems.RED_POPLAR_LEAVES, WildItems.ORANGE_POPLAR_LEAVES, WildItems.YELLOW_POPLAR_LEAVES, WildItems.RED_SHRUB)) {
			compost.add(item, 0.3F);
		}

		compost.add(WildItems.SHELF_MUSHROOM, 0.65F);
		compost.add(WildItems.STRAW_BED, 0.85F);

		// 26.3's cooking fuel components for items 1.21.7's fuel tags don't cover: wool stairs burn like wool, slabs and cushions half as long.
		FuelRegistryEvents.BUILD.register((builder, context) -> {
			int wool = context.baseSmeltTime() / 2;
			WildItems.WOOL_STAIRS.values().forEach(item -> builder.add(item, wool));
			WildItems.WOOL_SLAB.values().forEach(item -> builder.add(item, wool / 2));
			WildItems.CUSHIONS.values().forEach(item -> builder.add(item, wool / 2));
			builder.add(WildItems.POPLAR_SHELF, context.baseSmeltTime() * 3 / 2);
		});
	}

	private static void registerTrades() {
		// 26.3's wandering trader: poplar saplings and shelf mushrooms among its common offers, poplar logs among its uncommon ones.
		TradeOfferHelper.registerWanderingTraderOffers(
			builder -> builder.addOffersToPool(
					TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
					new VillagerTrades.ItemsForEmeralds(WildItems.POPLAR_SAPLING, 5, 1, 8, 1),
					new VillagerTrades.ItemsForEmeralds(WildItems.SHELF_MUSHROOM, 1, 3, 12, 1)
				)
				.addOffersToPool(
					TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL, new VillagerTrades.ItemsForEmeralds(WildItems.POPLAR_LOG, 1, 8, 4, 1)
				)
		);
	}

	private static void registerCreativeTabs() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
			.register(
				entries -> entries.addAfter(
					Items.PALE_OAK_BUTTON,
					WildItems.POPLAR_LOG,
					WildItems.POPLAR_WOOD,
					WildItems.STRIPPED_POPLAR_LOG,
					WildItems.STRIPPED_POPLAR_WOOD,
					WildItems.POPLAR_PLANKS,
					WildItems.POPLAR_STAIRS,
					WildItems.POPLAR_SLAB,
					WildItems.POPLAR_FENCE,
					WildItems.POPLAR_FENCE_GATE,
					WildItems.POPLAR_DOOR,
					WildItems.POPLAR_TRAPDOOR,
					WildItems.POPLAR_PRESSURE_PLATE,
					WildItems.POPLAR_BUTTON
				)
			);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
			entries.addAfter(Items.PINK_WOOL, colored(WildItems.WOOL_STAIRS, WildItems.WOOL_SLAB));
			entries.addAfter(Items.PINK_CONCRETE, colored(WildItems.CONCRETE_STAIRS, WildItems.CONCRETE_SLAB));
			entries.addAfter(Items.PINK_BED, colored(WildItems.CUSHIONS));
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
			entries.addAfter(Items.PALE_OAK_LOG, WildItems.POPLAR_LOG);
			entries.addAfter(Items.PALE_OAK_LEAVES, WildItems.RED_POPLAR_LEAVES, WildItems.ORANGE_POPLAR_LEAVES, WildItems.YELLOW_POPLAR_LEAVES);
			entries.addAfter(Items.PALE_OAK_SAPLING, WildItems.POPLAR_SAPLING);
			entries.addAfter(Items.RED_MUSHROOM, WildItems.SHELF_MUSHROOM);
			entries.addAfter(Items.BUSH, WildItems.RED_SHRUB);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.addAfter(BuiltInRegistries.ITEM.getValue(EverythingBackport.vanilla("pale_oak_shelf")), WildItems.POPLAR_SHELF);
			entries.addAfter(Items.PALE_OAK_HANGING_SIGN, WildItems.POPLAR_SIGN, WildItems.POPLAR_HANGING_SIGN);
			entries.addAfter(Items.PINK_BED, WildItems.STRAW_BED);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
			.register(entries -> entries.addAfter(Items.PALE_OAK_CHEST_BOAT, WildItems.POPLAR_BOAT, WildItems.POPLAR_CHEST_BOAT));
	}

	@SafeVarargs
	private static List<ItemStack> colored(Map<DyeColor, Item>... families) {
		return java.util.Arrays.stream(families)
			.flatMap(family -> COLOR_ORDER.stream().map(color -> new ItemStack(family.get(color))))
			.toList();
	}
}
