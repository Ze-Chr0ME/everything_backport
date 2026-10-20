package backport.everything_backport.test;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;

/** Builds a scene with the Copper Age content and saves screenshots of it. */
public class CopperAgeClientTest implements FabricClientGameTest {
	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			TestServerContext server = singleplayer.getServer();
			singleplayer.getClientWorld().waitForChunksRender();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("time set noon");
			server.runCommand("gamemode creative @a");

			String[] chests = {"copper_chest", "exposed_copper_chest", "weathered_copper_chest", "oxidized_copper_chest"};
			for (int i = 0; i < 4; i++) {
				server.runCommand("setblock " + (-7 + i) + " -60 6 " + chests[i] + "[facing=north]");
			}

			server.runCommand("setblock -3 -60 6 copper_chest[facing=north,type=right]");
			server.runCommand("setblock -2 -60 6 copper_chest[facing=north,type=left]");
			String[] statues = {"copper_golem_statue", "exposed_copper_golem_statue", "weathered_copper_golem_statue", "oxidized_copper_golem_statue"};
			String[] poses = {"standing", "sitting", "running", "star"};
			for (int i = 0; i < 4; i++) {
				server.runCommand("setblock " + i + " -60 6 " + statues[i] + "[facing=north,copper_golem_pose=" + poses[i] + "]");
			}

			String[] shelves = {"oak_shelf", "cherry_shelf", "crimson_shelf"};
			for (int i = 0; i < 3; i++) {
				server.runCommand("setblock " + (5 + i) + " -60 6 " + shelves[i] + "[facing=north]");
			}

			server.runCommand(
				"data merge block 5 -60 6 {Items:[{Slot:0b,id:\"minecraft:diamond_sword\",count:1},{Slot:1b,id:\"minecraft:copper_chest\",count:1},{Slot:2b,id:\"minecraft:apple\",count:1}]}"
			);
			server.runCommand(
				"data merge block 6 -60 6 {Items:[{Slot:0b,id:\"minecraft:copper_lantern\",count:1},{Slot:1b,id:\"minecraft:shield\",count:1},{Slot:2b,id:\"minecraft:copper_golem_statue\",count:1}]}"
			);
			server.runCommand(
				"data merge block 7 -60 6 {Items:[{Slot:0b,id:\"minecraft:white_bed\",count:1},{Slot:1b,id:\"minecraft:oak_fence\",count:1},{Slot:2b,id:\"minecraft:copper_pickaxe\",count:1}]}"
			);

			String[] upper = {
				"copper_lantern", "exposed_copper_lantern", "weathered_copper_lantern", "oxidized_copper_lantern", "copper_bars", "exposed_copper_bars",
				"weathered_copper_bars", "oxidized_copper_bars", "copper_chain", "exposed_copper_chain", "weathered_copper_chain", "oxidized_copper_chain"
			};
			for (int i = 0; i < upper.length; i++) {
				server.runCommand("setblock " + (-7 + i) + " -59 7 " + upper[i]);
			}

			String[] rods = {"lightning_rod", "exposed_lightning_rod", "weathered_lightning_rod", "oxidized_lightning_rod"};
			for (int i = 0; i < 4; i++) {
				server.runCommand("setblock " + (5 + i) + " -59 7 " + rods[i]);
			}

			server.runCommand("setblock 8 -60 4 copper_torch");
			server.runCommand("setblock 8 -60 6 stone");
			server.runCommand("setblock 8 -60 5 copper_wall_torch[facing=north]");
			String[] weathers = {"unaffected", "exposed", "weathered", "oxidized"};
			for (int i = 0; i < 4; i++) {
				String equipment = i == 1 ? ",equipment:{saddle:{id:\"minecraft:poppy\",count:1}}" : i == 0 ? ",equipment:{mainhand:{id:\"minecraft:iron_ingot\",count:1}}" : "";
				server.runCommand(
					"summon copper_golem " + (-5.5 + i * 1.5) + " -60 3.5 {NoAI:1b,Rotation:[180f,0f],weather_state:\"" + weathers[i] + "\"" + equipment + "}"
				);
			}

			server.runCommand(
				"summon zombie 3.5 -60 3.5 {NoAI:1b,Rotation:[180f,0f],equipment:{head:{id:\"minecraft:copper_helmet\",count:1},chest:{id:\"minecraft:copper_chestplate\",count:1},legs:{id:\"minecraft:copper_leggings\",count:1},feet:{id:\"minecraft:copper_boots\",count:1},mainhand:{id:\"minecraft:copper_sword\",count:1}}}"
			);
			server.runCommand("summon horse 6.5 -60 2.5 {NoAI:1b,Tame:1b,Rotation:[180f,0f],equipment:{body:{id:\"minecraft:copper_horse_armor\",count:1}}}");

			String[] hotbar = {
				"copper_sword", "copper_pickaxe", "copper_chest", "copper_golem_statue", "oak_shelf", "copper_lantern", "copper_torch", "copper_bars",
				"copper_golem_spawn_egg"
			};
			for (int i = 0; i < hotbar.length; i++) {
				server.runCommand("item replace entity @p hotbar." + i + " with " + hotbar[i]);
			}

			server.runCommand("tp @p 0.5 -60 -2.5 0 15");
			context.waitTicks(40);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("copper_age_overview");

			server.runCommand("tp @p 6.5 -59.5 3.2 0 25");
			context.waitTicks(20);
			context.takeScreenshot("copper_age_shelves");

			server.runCommand("tp @p -4 -59.5 1.5 0 20");
			context.waitTicks(20);
			context.takeScreenshot("copper_age_golems");

			// A copper golem carries items from a copper chest into a regular chest.
			server.runCommand("setblock 30 -60 0 copper_chest{Items:[{Slot:0b,id:\"minecraft:cobblestone\",count:10}]}");
			server.runCommand("setblock 34 -60 0 chest");
			server.runCommand("summon copper_golem 32 -60 2");
			server.runCommand("tp @p 32 -58 -4 0 30");
			boolean sorted = false;
			for (int ticks = 0; ticks < 1200 && !sorted; ticks += 20) {
				context.waitTicks(20);
				sorted = server.computeOnServer(s -> {
					var chest = (net.minecraft.world.Container)s.overworld().getBlockEntity(new net.minecraft.core.BlockPos(34, -60, 0));
					return chest != null && !chest.isEmpty();
				});
			}

			if (!sorted) {
				throw new AssertionError("The copper golem did not move items into the chest");
			}

			context.takeScreenshot("copper_golem_sorted_items");
		}
	}
}
