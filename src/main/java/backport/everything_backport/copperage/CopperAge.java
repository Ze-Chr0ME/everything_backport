package backport.everything_backport.copperage;

import backport.everything_backport.copperage.block.WeatheringCopperBlocks;
import backport.everything_backport.mixin.PoiTypesAccessor;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** The Copper Age (Java Edition 1.21.9). */
public final class CopperAge {
	private CopperAge() {
	}

	public static void init() {
		CopperAgeSounds.init();
		CopperAgeParticles.init();
		CopperAgeBlocks.init();
		CopperAgeEntities.init();
		CopperAgeItems.init();
		CopperAgeBlockEntities.init();
		CopperAgeMemories.init();
		registerWeathering();
		registerLightningRodPoi();
		registerFlammability();
		registerCreativeTabs();
	}

	private static void registerWeathering() {
		for (WeatheringCopperBlocks blocks : List.of(CopperAgeBlocks.COPPER_BARS, CopperAgeBlocks.COPPER_CHAIN, CopperAgeBlocks.COPPER_LANTERN)) {
			blocks.weatheringMapping().forEach(OxidizableBlocksRegistry::registerOxidizableBlockPair);
			blocks.waxedMapping().forEach(OxidizableBlocksRegistry::registerWaxableBlockPair);
		}

		registerWeatheringSet(CopperAgeBlocks.COPPER_CHESTS);
		registerWeatheringSet(CopperAgeBlocks.COPPER_GOLEM_STATUES);
		registerWeatheringSet(CopperAgeBlocks.LIGHTNING_RODS);
	}

	/** Blocks in the order unaffected, exposed, weathered, oxidized, then the four waxed versions. */
	private static void registerWeatheringSet(List<Block> blocks) {
		for (int i = 0; i < 3; i++) {
			OxidizableBlocksRegistry.registerOxidizableBlockPair(blocks.get(i), blocks.get(i + 1));
		}

		for (int i = 0; i < 4; i++) {
			OxidizableBlocksRegistry.registerWaxableBlockPair(blocks.get(i), blocks.get(i + 4));
		}
	}

	private static void registerLightningRodPoi() {
		Holder<PoiType> lightningRod = BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(PoiTypes.LIGHTNING_ROD);
		Map<BlockState, Holder<PoiType>> typeByState = PoiTypesAccessor.everythingBackport$getTypeByState();
		for (Block block : CopperAgeBlocks.LIGHTNING_RODS) {
			for (BlockState state : block.getStateDefinition().getPossibleStates()) {
				typeByState.put(state, lightningRod);
			}
		}
	}

	private static void registerFlammability() {
		FlammableBlockRegistry flammable = FlammableBlockRegistry.getDefaultInstance();
		for (Block shelf : CopperAgeBlocks.SHELVES) {
			if (shelf != CopperAgeBlocks.CRIMSON_SHELF && shelf != CopperAgeBlocks.WARPED_SHELF) {
				flammable.add(shelf, 30, 20);
			}
		}

		FuelRegistryEvents.BUILD.register((builder, context) -> builder.add(CopperAgeTags.Items.WOODEN_SHELVES, context.baseSmeltTime() * 3 / 2));
	}

	private static void registerCreativeTabs() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
			var bars = CopperAgeItems.COPPER_BARS;
			var chain = CopperAgeItems.COPPER_CHAIN;
			entries.addAfter(Items.CUT_COPPER_SLAB, bars.unaffected());
			entries.addAfter(Items.COPPER_BULB, chain.unaffected());
			entries.addAfter(Items.EXPOSED_CUT_COPPER_SLAB, bars.exposed());
			entries.addAfter(Items.EXPOSED_COPPER_BULB, chain.exposed());
			entries.addAfter(Items.WEATHERED_CUT_COPPER_SLAB, bars.weathered());
			entries.addAfter(Items.WEATHERED_COPPER_BULB, chain.weathered());
			entries.addAfter(Items.OXIDIZED_CUT_COPPER_SLAB, bars.oxidized());
			entries.addAfter(Items.OXIDIZED_COPPER_BULB, chain.oxidized());
			entries.addAfter(Items.WAXED_CUT_COPPER_SLAB, bars.waxed());
			entries.addAfter(Items.WAXED_COPPER_BULB, chain.waxed());
			entries.addAfter(Items.WAXED_EXPOSED_CUT_COPPER_SLAB, bars.waxedExposed());
			entries.addAfter(Items.WAXED_EXPOSED_COPPER_BULB, chain.waxedExposed());
			entries.addAfter(Items.WAXED_WEATHERED_CUT_COPPER_SLAB, bars.waxedWeathered());
			entries.addAfter(Items.WAXED_WEATHERED_COPPER_BULB, chain.waxedWeathered());
			entries.addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, bars.waxedOxidized());
			entries.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB, chain.waxedOxidized());
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.addAfter(Items.SOUL_TORCH, CopperAgeItems.COPPER_TORCH);
			entries.addAfter(Items.SOUL_LANTERN, stacks(CopperAgeItems.COPPER_LANTERN.asList()));
			entries.addAfter(Items.CHAIN, stacks(CopperAgeItems.COPPER_CHAIN.asList()));
			entries.addAfter(Items.LIGHTNING_ROD, stacks(CopperAgeItems.LIGHTNING_RODS));
			entries.addAfter(Items.CHISELED_BOOKSHELF, stacks(CopperAgeItems.SHELVES));
			entries.addAfter(Items.CHEST, stacks(CopperAgeItems.COPPER_CHESTS));
			entries.addAfter(Items.VAULT, stacks(CopperAgeItems.COPPER_GOLEM_STATUES));
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
			entries.addAfter(Items.DAYLIGHT_DETECTOR, CopperAgeBlocks.WAXED_LIGHTNING_ROD);
			entries.addAfter(Items.CHEST, CopperAgeBlocks.WAXED_COPPER_CHEST);
			entries.addAfter(Items.CHISELED_BOOKSHELF, CopperAgeBlocks.OAK_SHELF);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.addAfter(
			Items.STONE_HOE, CopperAgeItems.COPPER_SHOVEL, CopperAgeItems.COPPER_PICKAXE, CopperAgeItems.COPPER_AXE, CopperAgeItems.COPPER_HOE
		));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.addAfter(Items.STONE_SWORD, CopperAgeItems.COPPER_SWORD);
			entries.addAfter(Items.STONE_AXE, CopperAgeItems.COPPER_AXE);
			entries.addAfter(
				Items.LEATHER_BOOTS, CopperAgeItems.COPPER_HELMET, CopperAgeItems.COPPER_CHESTPLATE, CopperAgeItems.COPPER_LEGGINGS, CopperAgeItems.COPPER_BOOTS
			);
			entries.addAfter(Items.LEATHER_HORSE_ARMOR, CopperAgeItems.COPPER_HORSE_ARMOR);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> entries.addAfter(Items.AMETHYST_SHARD, CopperAgeItems.COPPER_NUGGET));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> entries.addAfter(Items.COD_SPAWN_EGG, CopperAgeItems.COPPER_GOLEM_SPAWN_EGG));
	}

	private static List<ItemStack> stacks(List<Item> items) {
		return items.stream().map(ItemStack::new).toList();
	}
}
