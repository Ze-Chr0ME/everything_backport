package backport.everything_backport.tinytakeover;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

/** Tiny Takeover (Java Edition 26.1). */
public final class TinyTakeover {
	private TinyTakeover() {
	}

	public static void init() {
		TinySounds.init();
		TinyParticles.init();
		TinyRegistries.init();
		TinyComponents.init();
		TinyEntityData.init();
		TinyBlocks.init();
		TinyItems.init();
		FlammableBlockRegistry.getDefaultInstance().add(TinyBlocks.GOLDEN_DANDELION, 60, 100);
		// 26.1's wandering trader sells golden dandelions among its common offers: 2 emeralds each, 12 uses.
		TradeOfferHelper.registerWanderingTraderOffers(
			builder -> builder.addOffersToPool(
				TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL, new VillagerTrades.ItemsForEmeralds(TinyItems.GOLDEN_DANDELION, 2, 1, 12, 1)
			)
		);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> entries.addAfter(Items.BEETROOT, TinyItems.GOLDEN_DANDELION));
	}
}
