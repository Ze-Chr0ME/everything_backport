package backport.everything_backport.test;

import backport.everything_backport.wildernessbound.WildBlocks;
import backport.everything_backport.wildernessbound.WildEntities;
import backport.everything_backport.wildernessbound.WildItems;
import backport.everything_backport.wildernessbound.WildStats;
import backport.everything_backport.wildernessbound.WildWorldgen;
import backport.everything_backport.wildernessbound.block.PoplarSaplingBlock;
import backport.everything_backport.wildernessbound.block.ShelfMushroomBlock;
import backport.everything_backport.wildernessbound.entity.Cushion;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

/**
 * Builds Wilderness Bound scenes, saves screenshots and checks poplar growth, poplar features, straw beds, cushions, shelf mushrooms
 * and the dappled forest placement in a flat world, then finds a dappled forest and an abandoned camp in a generated one.
 */
public class WildernessBoundClientTest implements FabricClientGameTest {
	private static final String[] BLOCKS = {
		"poplar_log", "poplar_wood", "stripped_poplar_log", "stripped_poplar_wood", "poplar_planks", "poplar_stairs", "poplar_slab", "poplar_fence",
		"poplar_fence_gate", "poplar_door", "poplar_trapdoor", "poplar_pressure_plate", "poplar_button", "red_poplar_leaves", "orange_poplar_leaves",
		"yellow_poplar_leaves", "poplar_sapling", "potted_poplar_sapling", "red_shrub", "poplar_shelf", "white_wool_stairs", "red_wool_slab",
		"blue_concrete_stairs", "lime_concrete_slab"
	};
	/** Seed of the generated world; dappled forest and camp searches start from spawn. */
	private static final String SEED = "26.3";

	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			TestServerContext server = singleplayer.getServer();
			singleplayer.getClientWorld().waitForChunksRender();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("gamerule doMobSpawning false");
			server.runCommand("gamerule randomTickSpeed 0");
			server.runCommand("time set noon");
			server.runCommand("gamemode creative @a");

			// The poplar set and the new stairs and slabs in two rows, with signs, a shelf mushroom on a log and a straw bed.
			for (int i = 0; i < BLOCKS.length; i++) {
				int x = (i % 12) * 2 - 11;
				int z = 6 + (i / 12) * 3;
				server.runCommand("setblock " + x + " -60 " + z + " " + BLOCKS[i]);
			}

			server.runCommand("setblock 13 -60 6 poplar_sign[rotation=8]{front_text:{messages:['\"Poplar\"','\"\"','\"\"','\"\"']}}");
			server.runCommand("setblock 15 -59 6 poplar_planks");
			server.runCommand("setblock 15 -60 6 poplar_hanging_sign[rotation=8]{front_text:{messages:['\"Hanging\"','\"\"','\"\"','\"\"']}}");
			server.runCommand("fill 17 -60 6 17 -58 6 poplar_log");
			server.runCommand("setblock 17 -59 5 shelf_mushroom[facing=north,age=0]");
			server.runCommand("setblock 18 -60 6 shelf_mushroom[facing=east,age=1]");
			server.runCommand("setblock 20 -60 7 straw_bed[facing=south,part=foot]");
			server.runCommand("setblock 20 -60 8 straw_bed[facing=south,part=head]");
			server.runCommand("tp @p -4 -57 1 0 30");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("wilderness_bound_blocks");
			server.runCommand("tp @p 12 -57 1 0 30");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("wilderness_bound_blocks_2");

			// Cushions in every color, a poplar boat and a chest boat.
			String[] colors = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
			for (int i = 0; i < colors.length; i++) {
				server.runCommand("summon cushion " + (40.5 + (i % 8) * 1.5) + " -60 " + (6.5 + (i / 8) * 1.5) + " {color:\"" + colors[i] + "\"}");
			}

			server.runCommand("summon poplar_boat 54 -60 6.5");
			server.runCommand("summon poplar_chest_boat 57 -60 6.5");
			server.runCommand("tp @p 47 -57.5 1.5 0 30");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("wilderness_bound_cushions");

			List<String> failures = new ArrayList<>();
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkRegistries));
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkBiomeSource));
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkShelfMushroom));
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkCushion));

			// Poplar trees: one per leaf color, a fallen one and a grown sapling.
			server.runCommand("fill 80 -61 0 120 -61 30 grass_block");
			server.runCommand("place feature minecraft:red_poplar 84 -60 10");
			server.runCommand("place feature minecraft:orange_poplar_leaf_litter 92 -60 10");
			server.runCommand("place feature minecraft:yellow_poplar 100 -60 10");
			server.runCommand("place feature minecraft:fallen_poplar_tree 108 -60 10");
			// A patch tries 8 spots spread 3 blocks up and down, so on flat ground only a few land; place several.
			for (int x = 84; x <= 108; x += 8) {
				server.runCommand("place feature minecraft:patch_red_shrub " + x + " -60 22");
			}
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkPoplarFeatures));
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkSapling));
			server.runCommand("tp @p 96 -54 -14 0 10");
			context.waitTicks(100);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("wilderness_bound_poplars");

			// A straw bed skips the night without setting the respawn point and crumbles when its sleeper gets up.
			server.runCommand("time set midnight");
			server.runCommand("gamemode survival @a");
			context.waitTicks(5);
			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkStrawBed));
			server.runCommand("gamemode creative @a");
			server.runCommand("time set noon");
			if (!failures.isEmpty()) {
				throw new AssertionError(String.join("; ", failures));
			}
		}

		try (
			TestSingleplayerContext singleplayer = context.worldBuilder()
				.setUseConsistentSettings(false)
				.adjustSettings(settings -> settings.setSeed(SEED))
				.create()
		) {
			TestServerContext server = singleplayer.getServer();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("gamerule doMobSpawning false");
			server.runCommand("time set noon");
			server.runCommand("gamemode spectator @a");
			BlockPos forest = server.computeOnServer(WildernessBoundClientTest::findDappledForest);
			if (forest == null) {
				throw new AssertionError("no dappled forest within 6400 blocks of spawn in a generated world");
			}

			server.runCommand("forceload add " + forest.getX() + " " + forest.getZ());
			server.runCommand("tp @p " + forest.getX() + " " + (forest.getY() + 12) + " " + forest.getZ() + " 45 20");
			context.waitTicks(100);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("wilderness_bound_dappled_forest");
			List<String> failures = new ArrayList<>(server.computeOnServer(server2 -> checkDappledForest(server2, forest)));

			BlockPos camp = server.computeOnServer(WildernessBoundClientTest::findAbandonedCamp);
			if (camp == null) {
				failures.add("no abandoned camp within 100 chunks of spawn in a generated world");
			} else {
				server.runCommand("forceload add " + camp.getX() + " " + camp.getZ());
				server.runCommand("tp @p " + (camp.getX() + 8) + " 140 " + (camp.getZ() + 8) + " 0 90");
				context.waitTicks(100);
				BlockPos campBlock = server.computeOnServer(server2 -> findCampBlock(server2, camp));
				if (campBlock == null) {
					String starts = server.computeOnServer(
						server2 -> server2.overworld()
							.getChunk(camp)
							.getAllStarts()
							.entrySet()
							.stream()
							.map(entry -> entry.getKey() + " " + entry.getValue().getBoundingBox() + " " + entry.getValue().getPieces().stream().map(Object::toString).toList())
							.toList()
							.toString()
					);
					failures.add("the abandoned camp at " + camp + " has no barrel, campfire, straw bed or wool stairs; starts there: " + starts);
				} else {
					server.runCommand("tp @p " + (campBlock.getX() + 0.5) + " " + (campBlock.getY() + 7) + " " + (campBlock.getZ() - 6.5) + " 0 45");
					context.waitTicks(60);
					singleplayer.getClientWorld().waitForChunksRender();
					context.takeScreenshot("wilderness_bound_abandoned_camp");
				}
			}

			failures.addAll(server.computeOnServer(WildernessBoundClientTest::checkExplorerMapTrade));
			if (!failures.isEmpty()) {
				throw new AssertionError(String.join("; ", failures));
			}
		}
	}

	private static List<String> checkRegistries(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		if (server.registryAccess().lookupOrThrow(Registries.BIOME).get(WildWorldgen.DAPPLED_FOREST).isEmpty()) {
			failures.add("dappled_forest biome is not registered");
		}

		for (String name : List.of("abandoned_camp_forest", "abandoned_camp_swamp", "abandoned_camp_dappled_forest")) {
			if (server.registryAccess().lookupOrThrow(Registries.STRUCTURE).get(backport.everything_backport.EverythingBackport.vanilla(name)).isEmpty()) {
				failures.add(name + " structure is not registered");
			}
		}

		return failures;
	}

	/** The vanilla overworld source turns plains into dappled forest at 26.3's climate, and leaves warmer plains alone. */
	private static List<String> checkBiomeSource(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		MultiNoiseBiomeSource source = MultiNoiseBiomeSource.createFromPreset(
			server.registryAccess().lookupOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD)
		);
		if (source.possibleBiomes().stream().noneMatch(biome -> biome.is(WildWorldgen.DAPPLED_FOREST))) {
			failures.add("dappled forest is not a possible biome of the overworld source");
		}

		// Cold, dry, mid-inland, mid erosion, surface, positive weirdness: 1.21.7 picks plains, 26.3 dappled forest.
		Holder<Biome> cold = source.getNoiseBiome(Climate.target(-0.3F, -0.6F, 0.2F, 0.1F, 0.0F, 0.3F));
		if (!cold.is(WildWorldgen.DAPPLED_FOREST)) {
			failures.add("dappled forest climate gave " + cold.getRegisteredName());
		}

		Holder<Biome> temperate = source.getNoiseBiome(Climate.target(0.0F, -0.2F, 0.2F, 0.1F, 0.0F, 0.3F));
		if (temperate.is(WildWorldgen.DAPPLED_FOREST)) {
			failures.add("dappled forest replaced temperate " + temperate.getRegisteredName());
		}

		return failures;
	}

	/** Bone meal grows a small shelf mushroom, which then drops two when broken; it pops off without its log. */
	private static List<String> checkShelfMushroom(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		BlockPos pos = new BlockPos(17, -59, 5);
		BlockState state = level.getBlockState(pos);
		if (!state.is(WildBlocks.SHELF_MUSHROOM)) {
			return List.of("shelf mushroom was not placed: " + state);
		}

		((ShelfMushroomBlock)WildBlocks.SHELF_MUSHROOM).performBonemeal(level, level.random, pos, state);
		if (level.getBlockState(pos).getValue(ShelfMushroomBlock.AGE) != 1) {
			failures.add("bone meal did not grow the shelf mushroom");
		}

		level.setBlockAndUpdate(new BlockPos(17, -59, 6), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
		if (level.getBlockState(pos).is(WildBlocks.SHELF_MUSHROOM)) {
			failures.add("shelf mushroom survived without its log");
		}

		return failures;
	}

	/** A cushion keeps its color, carries a rider and pops off when its support goes. */
	private static List<String> checkCushion(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		List<Cushion> cushions = level.getEntitiesOfClass(Cushion.class, new net.minecraft.world.phys.AABB(38, -62, 4, 54, -56, 10));
		if (cushions.size() != 16) {
			failures.add("expected 16 cushions, found " + cushions.size());
		}

		long colors = cushions.stream().map(Cushion::getColor).distinct().count();
		if (colors != 16) {
			failures.add("cushions have " + colors + " colors, expected 16");
		}

		level.setBlockAndUpdate(new BlockPos(70, -61, 20), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
		Cushion seat = WildEntities.CUSHION.create(level, EntitySpawnReason.COMMAND);
		seat.snapTo(70.5, -60.0, 20.5, 0.0F, 0.0F);
		level.addFreshEntity(seat);
		if (!seat.survives()) {
			failures.add("cushion on stone does not survive");
		}

		ServerPlayer player = server.getPlayerList().getPlayers().getFirst();
		if (!player.startRiding(seat) || player.getVehicle() != seat) {
			failures.add("player could not sit on a cushion");
		}

		player.stopRiding();
		level.setBlockAndUpdate(new BlockPos(70, -61, 20), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
		if (seat.survives()) {
			failures.add("cushion survives without support");
		}

		seat.discard();
		return failures;
	}

	private static List<String> checkPoplarFeatures(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		int logs = 0;
		int red = 0;
		int orange = 0;
		int yellow = 0;
		int litter = 0;
		int shrubs = 0;
		for (BlockPos pos : BlockPos.betweenClosed(78, -61, 2, 118, -40, 30)) {
			BlockState state = level.getBlockState(pos);
			if (state.is(WildBlocks.POPLAR_LOG)) {
				logs++;
			} else if (state.is(WildBlocks.RED_POPLAR_LEAVES)) {
				red++;
			} else if (state.is(WildBlocks.ORANGE_POPLAR_LEAVES)) {
				orange++;
			} else if (state.is(WildBlocks.YELLOW_POPLAR_LEAVES)) {
				yellow++;
			} else if (state.is(net.minecraft.world.level.block.Blocks.LEAF_LITTER)) {
				litter++;
			} else if (state.is(WildBlocks.RED_SHRUB)) {
				shrubs++;
			}
		}

		if (logs < 20 || red == 0 || orange == 0 || yellow == 0) {
			failures.add("poplar features placed " + logs + " logs and " + red + "/" + orange + "/" + yellow + " red/orange/yellow leaves");
		}

		if (litter == 0) {
			failures.add("the leaf litter poplar placed no leaf litter");
		}

		if (shrubs == 0) {
			failures.add("the red shrub patch placed no shrubs");
		}

		return failures;
	}

	/** A poplar sapling grows into a poplar of one of the three leaf colors. */
	private static List<String> checkSapling(MinecraftServer server) {
		ServerLevel level = server.overworld();
		BlockPos pos = new BlockPos(116, -60, 24);
		level.setBlockAndUpdate(pos, WildBlocks.POPLAR_SAPLING.defaultBlockState());
		PoplarSaplingBlock sapling = (PoplarSaplingBlock)WildBlocks.POPLAR_SAPLING;
		for (int i = 0; i < 8 && level.getBlockState(pos).is(WildBlocks.POPLAR_SAPLING); i++) {
			sapling.advanceTree(level, pos, level.getBlockState(pos), level.random);
		}

		return level.getBlockState(pos).is(WildBlocks.POPLAR_LOG) ? List.of() : List.of("poplar sapling did not grow: " + level.getBlockState(pos));
	}

	private static List<String> checkStrawBed(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		ServerPlayer player = server.getPlayerList().getPlayers().getFirst();
		BlockPos head = new BlockPos(20, -60, 8);
		player.teleportTo(20.5, -60.0, 10.5);
		ServerPlayer.RespawnConfig before = player.getRespawnConfig();
		int slept = player.getStats().getValue(Stats.CUSTOM.get(WildStats.SLEEP_IN_STRAW_BED));
		var result = player.startSleepInBed(head);
		if (result.left().isPresent()) {
			return List.of("could not sleep in a straw bed: " + result.left().get());
		}

		if (!java.util.Objects.equals(player.getRespawnConfig(), before)) {
			failures.add("sleeping in a straw bed set the respawn point");
		}

		if (player.getStats().getValue(Stats.CUSTOM.get(WildStats.SLEEP_IN_STRAW_BED)) != slept + 1) {
			failures.add("sleeping in a straw bed did not count sleep_in_straw_bed");
		}

		if (Math.abs(player.getY() - (head.getY() + 0.25)) > 0.01) {
			failures.add("straw bed sleeper lies at " + player.getY());
		}

		player.stopSleepInBed(true, true);
		if (level.getBlockState(head).is(WildBlocks.STRAW_BED) || level.getBlockState(head.north()).is(WildBlocks.STRAW_BED)) {
			failures.add("straw bed did not crumble when its sleeper got up");
		}

		return failures;
	}

	private static BlockPos findDappledForest(MinecraftServer server) {
		ServerLevel level = server.overworld();
		Pair<BlockPos, Holder<Biome>> found = level.findClosestBiome3d(biome -> biome.is(WildWorldgen.DAPPLED_FOREST), level.getSharedSpawnPos(), 6400, 32, 64);
		if (found == null) {
			return null;
		}

		BlockPos pos = found.getFirst();
		level.getChunk(pos);
		return level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
	}

	/** A dappled forest is full of poplars. */
	private static List<String> checkDappledForest(MinecraftServer server, BlockPos center) {
		ServerLevel level = server.overworld();
		int logs = 0;
		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-24, -8, -24), center.offset(24, 24, 24))) {
			if (level.getBlockState(pos).is(WildBlocks.POPLAR_LOG)) {
				logs++;
			}
		}

		return logs > 10 ? List.of() : List.of("only " + logs + " poplar logs around the dappled forest at " + center);
	}

	private static BlockPos findAbandonedCamp(MinecraftServer server) {
		ServerLevel level = server.overworld();
		HolderSet<Structure> camps = level.registryAccess()
			.lookupOrThrow(Registries.STRUCTURE)
			.get(net.minecraft.tags.TagKey.create(Registries.STRUCTURE, backport.everything_backport.EverythingBackport.vanilla("abandoned_camp")))
			.map(set -> (HolderSet<Structure>)set)
			.orElse(null);
		if (camps == null) {
			return null;
		}

		Pair<BlockPos, Holder<Structure>> found = level.getChunkSource()
			.getGenerator()
			.findNearestMapStructure(level, camps, level.getSharedSpawnPos(), 100, false);
		return found == null ? null : found.getFirst();
	}

	/** A block only an abandoned camp places, near the camp's start chunk. */
	private static BlockPos findCampBlock(MinecraftServer server, BlockPos camp) {
		ServerLevel level = server.overworld();
		for (BlockPos pos : BlockPos.betweenClosed(camp.getX() - 24, level.getMinY() + 40, camp.getZ() - 24, camp.getX() + 40, 200, camp.getZ() + 40)) {
			BlockState state = level.getBlockState(pos);
			if (state.is(net.minecraft.world.level.block.Blocks.BARREL)
				|| state.is(net.minecraft.world.level.block.Blocks.CAMPFIRE)
				|| state.is(WildBlocks.STRAW_BED)
				|| state.is(WildBlocks.WOOL_STAIRS.get(net.minecraft.world.item.DyeColor.WHITE))) {
				return pos.immutable();
			}
		}

		return null;
	}

	/** A cartographer's ocean explorer map is 26.3's ocean monument map item. */
	private static List<String> checkExplorerMapTrade(MinecraftServer server) {
		ServerLevel level = server.overworld();
		var villager = EntityType.VILLAGER.create(level, EntitySpawnReason.COMMAND);
		villager.snapTo(level.getSharedSpawnPos(), 0.0F, 0.0F);
		MerchantOffer offer = new VillagerTrades.TreasureMapForEmeralds(
				13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.OCEAN_MONUMENT, 12, 5
			)
			.getOffer(villager, level.random);
		if (offer == null) {
			return List.of("no ocean monument for the explorer map trade");
		}

		ItemStack result = offer.getResult();
		return result.is(WildItems.OCEAN_MONUMENT_MAP) && result.has(net.minecraft.core.component.DataComponents.MAP_ID)
			? List.of()
			: List.of("ocean explorer map trade gave " + result);
	}
}
