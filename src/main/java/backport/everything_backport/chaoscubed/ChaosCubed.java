package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.chaoscubed.entity.SulfurCube;
import java.util.List;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

/** Chaos Cubed (Java Edition 26.2). */
public final class ChaosCubed {
	public static final ResourceKey<JukeboxSong> BOUNCE = ResourceKey.create(Registries.JUKEBOX_SONG, EverythingBackport.vanilla("bounce"));
	public static final Holder<GameEvent> BOUNCE_EVENT = Registry.registerForHolder(
		BuiltInRegistries.GAME_EVENT, EverythingBackport.vanilla("bounce"), new GameEvent(GameEvent.DEFAULT_NOTIFICATION_RADIUS)
	);
	/** 26.2's default dispense behavior for items a sulfur cube can swallow, used when nothing else is registered for the item. */
	public static final DispenseItemBehavior SWALLOW_BEHAVIOR = new DefaultDispenseItemBehavior() {
		@Override
		protected ItemStack execute(BlockSource source, ItemStack dispensed) {
			return feedSulfurCube(source.level(), source.pos().relative(source.state().getValue(DispenserBlock.FACING)), dispensed)
				? dispensed
				: super.execute(source, dispensed);
		}
	};

	private ChaosCubed() {
	}

	public static void init() {
		ChaosSounds.init();
		ChaosAttributes.init();
		ChaosParticles.init();
		ChaosComponents.init();
		ChaosRegistries.init();
		ChaosBlocks.init();
		ChaosEntities.init();
		ChaosItems.init();
		ChaosWorldgen.init();
		registerDispenserBehaviors();
		// 26.2's wandering trader sells two sulfur spikes for an emerald among its common offers, 5 uses.
		TradeOfferHelper.registerWanderingTraderOffers(
			builder -> builder.addOffersToPool(
				TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL, new VillagerTrades.ItemsForEmeralds(ChaosItems.SULFUR_SPIKE, 1, 2, 5, 1)
			)
		);
		registerCreativeTabs();
	}

	/** 26.2's {@code SulfurCubeBlockDispenseItemBehavior.dispenseBlock}: the first cube in front swallows the item. */
	public static boolean feedSulfurCube(ServerLevel level, BlockPos pos, ItemStack dispensed) {
		for (SulfurCube entity : level.getEntitiesOfClass(SulfurCube.class, new AABB(pos))) {
			if (entity.equipItem(dispensed)) {
				dispensed.shrink(1);
				return true;
			}
		}

		return false;
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(ChaosItems.SULFUR_CUBE_BUCKET, DispenserBlock.DISPENSER_REGISTRY.get(Items.TROPICAL_FISH_BUCKET));
		// A dispenser feeds TNT to a sulfur cube in front before priming it, and lights an explosive cube before placing fire.
		DispenseItemBehavior tnt = DispenserBlock.DISPENSER_REGISTRY.get(Items.TNT);
		DispenserBlock.registerBehavior(Items.TNT, (source, dispensed) -> {
			ServerLevel level = source.level();
			if (level.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES) && feedSulfurCube(level, inFront(source), dispensed)) {
				playDispenseEffects(source);
				return dispensed;
			}

			return tnt.dispense(source, dispensed);
		});
		DispenseItemBehavior flintAndSteel = DispenserBlock.DISPENSER_REGISTRY.get(Items.FLINT_AND_STEEL);
		DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, (source, dispensed) -> {
			ServerLevel level = source.level();
			List<SulfurCube> cubes = level.getEntitiesOfClass(SulfurCube.class, new AABB(inFront(source)), SulfurCube::canExplode);
			if (!cubes.isEmpty()) {
				cubes.getFirst().primeTime(false);
				dispensed.hurtAndBreak(1, level, null, item -> {});
				playDispenseEffects(source);
				return dispensed;
			}

			return flintAndSteel.dispense(source, dispensed);
		});
	}

	private static BlockPos inFront(BlockSource source) {
		return source.pos().relative(source.state().getValue(DispenserBlock.FACING));
	}

	/** What {@code DefaultDispenseItemBehavior} does after a successful dispense: the click and the smoke. */
	private static void playDispenseEffects(BlockSource source) {
		Direction facing = source.state().getValue(DispenserBlock.FACING);
		source.level().levelEvent(1000, source.pos(), 0);
		source.level().levelEvent(2000, source.pos(), facing.get3DDataValue());
	}

	private static void registerCreativeTabs() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS)
			.register(
				entries -> entries.addAfter(
					Items.CUT_RED_SANDSTONE_SLAB,
					ChaosItems.CINNABAR,
					ChaosItems.CINNABAR_STAIRS,
					ChaosItems.CINNABAR_SLAB,
					ChaosItems.CINNABAR_WALL,
					ChaosItems.CHISELED_CINNABAR,
					ChaosItems.POLISHED_CINNABAR,
					ChaosItems.POLISHED_CINNABAR_STAIRS,
					ChaosItems.POLISHED_CINNABAR_SLAB,
					ChaosItems.POLISHED_CINNABAR_WALL,
					ChaosItems.CINNABAR_BRICKS,
					ChaosItems.CINNABAR_BRICK_STAIRS,
					ChaosItems.CINNABAR_BRICK_SLAB,
					ChaosItems.CINNABAR_BRICK_WALL,
					ChaosItems.SULFUR,
					ChaosItems.SULFUR_STAIRS,
					ChaosItems.SULFUR_SLAB,
					ChaosItems.SULFUR_WALL,
					ChaosItems.CHISELED_SULFUR,
					ChaosItems.POLISHED_SULFUR,
					ChaosItems.POLISHED_SULFUR_STAIRS,
					ChaosItems.POLISHED_SULFUR_SLAB,
					ChaosItems.POLISHED_SULFUR_WALL,
					ChaosItems.SULFUR_BRICKS,
					ChaosItems.SULFUR_BRICK_STAIRS,
					ChaosItems.SULFUR_BRICK_SLAB,
					ChaosItems.SULFUR_BRICK_WALL
				)
			);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
			.register(entries -> entries.addAfter(Items.POINTED_DRIPSTONE, ChaosItems.CINNABAR, ChaosItems.SULFUR, ChaosItems.SULFUR_SPIKE, ChaosItems.POTENT_SULFUR));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.addAfter(Items.TADPOLE_BUCKET, ChaosItems.SULFUR_CUBE_BUCKET);
			entries.addAfter(Items.MUSIC_DISC_LAVA_CHICKEN, ChaosItems.MUSIC_DISC_BOUNCE);
		});
		// 1.21.7 sorts spawn eggs alphabetically.
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> entries.addAfter(Items.STRIDER_SPAWN_EGG, ChaosItems.SULFUR_CUBE_SPAWN_EGG));
	}
}
