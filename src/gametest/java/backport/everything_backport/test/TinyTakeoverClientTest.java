package backport.everything_backport.test;

import backport.everything_backport.tinytakeover.AgeLockable;
import backport.everything_backport.tinytakeover.TinyItems;
import backport.everything_backport.tinytakeover.TrumpetInstruments;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.NoteBlock;

/** Builds scenes with the Tiny Takeover babies, saves screenshots and checks the age lock, sound variants, hitboxes and trumpet. */
public class TinyTakeoverClientTest implements FabricClientGameTest {
	private static final String AGE = "Age:-24000";
	private static final String IS_BABY = "IsBaby:1b";

	/** Groups of babies, one screenshot each: {entity, extra NBT}. */
	private static final String[][][] GROUPS = {
		{
			{"cow", AGE}, {"cow", AGE + ",variant:\"minecraft:warm\""}, {"cow", AGE + ",variant:\"minecraft:cold\""}, {"mooshroom", AGE},
			{"pig", AGE}, {"pig", AGE + ",variant:\"minecraft:cold\""}, {"sheep", AGE + ",Color:14b"}, {"chicken", AGE},
			{"chicken", AGE + ",variant:\"minecraft:cold\""}, {"goat", AGE}, {"rabbit", AGE}, {"rabbit", ""}, {"turtle", AGE}, {"armadillo", AGE}
		},
		{
			{"wolf", AGE}, {"cat", AGE + ",variant:\"minecraft:calico\",Owner:[I;1,2,3,4],CollarColor:3b"}, {"ocelot", AGE}, {"fox", AGE},
			{"fox", AGE + ",Type:\"snow\""}, {"panda", AGE}, {"polar_bear", AGE}, {"bee", AGE}, {"axolotl", AGE + ",Variant:1"}, {"dolphin", AGE},
			{"squid", AGE}, {"glow_squid", AGE}, {"sniffer", "Age:-48000"}, {"strider", AGE}
		},
		{
			{"horse", AGE + ",Variant:513"}, {"skeleton_horse", AGE}, {"zombie_horse", AGE}, {"donkey", AGE}, {"mule", AGE}, {"llama", AGE},
			{"trader_llama", AGE}, {"camel", AGE}, {"hoglin", AGE}, {"zoglin", IS_BABY}, {"villager", AGE + ",VillagerData:{type:\"minecraft:snow\"}"},
			{"villager", AGE + ",VillagerData:{type:\"minecraft:desert\"}"}, {"zombie_villager", IS_BABY}
		},
		{
			{"zombie", IS_BABY}, {"zombie", IS_BABY + ",equipment:{head:{id:\"minecraft:iron_helmet\",count:1},chest:{id:\"minecraft:iron_chestplate\",count:1},legs:{id:\"minecraft:iron_leggings\",count:1},feet:{id:\"minecraft:iron_boots\",count:1},mainhand:{id:\"minecraft:iron_sword\",count:1}}}"},
			{"husk", IS_BABY + ",equipment:{chest:{id:\"minecraft:leather_chestplate\",count:1},legs:{id:\"minecraft:golden_leggings\",count:1}}"},
			{"drowned", IS_BABY + ",equipment:{head:{id:\"minecraft:diamond_helmet\",count:1},feet:{id:\"minecraft:netherite_boots\",count:1}}"},
			{"zombie_villager", IS_BABY + ",equipment:{head:{id:\"minecraft:turtle_helmet\",count:1},chest:{id:\"minecraft:chainmail_chestplate\",count:1}}"},
			{"piglin", IS_BABY + ",IsImmuneToZombification:1b"},
			{"piglin", IS_BABY + ",IsImmuneToZombification:1b,equipment:{head:{id:\"minecraft:golden_helmet\",count:1},chest:{id:\"minecraft:copper_chestplate\",count:1},legs:{id:\"minecraft:diamond_leggings\",count:1},mainhand:{id:\"minecraft:golden_sword\",count:1}}}"},
			{"zombified_piglin", IS_BABY}, {"zombie", ""}, {"armor_stand", "Small:1b,ShowArms:1b,equipment:{head:{id:\"minecraft:iron_helmet\",count:1},chest:{id:\"minecraft:iron_chestplate\",count:1}}"}
		}
	};

	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			TestServerContext server = singleplayer.getServer();
			singleplayer.getClientWorld().waitForChunksRender();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("gamerule doMobSpawning false");
			server.runCommand("time set noon");
			server.runCommand("gamemode creative @a");

			for (int g = 0; g < GROUPS.length; g++) {
				String[][] group = GROUPS[g];
				int baseX = g * 40;
				for (int i = 0; i < group.length; i++) {
					double x = baseX + (i % 7) * 2.0 - 6.0;
					double z = 4 + (i / 7) * 3.5;
					String nbt = "NoAI:1b,Silent:1b,PersistenceRequired:1b,Rotation:[200f,0f]" + (group[i][1].isEmpty() ? "" : "," + group[i][1]);
					server.runCommand("summon " + group[i][0] + " " + x + " -60 " + z + " {" + nbt + "}");
				}
			}

			server.runCommand("setblock 120 -60 4 golden_dandelion");
			server.runCommand("setblock 121 -60 4 potted_golden_dandelion");
			server.runCommand("setblock 119 -60 4 dandelion");
			server.runCommand("item replace entity @p hotbar.0 with golden_dandelion");

			for (int g = 0; g < GROUPS.length; g++) {
				server.runCommand("tp @p " + (g * 40 + 0.5) + " -57.5 -5 0 22");
				context.waitTicks(30);
				singleplayer.getClientWorld().waitForChunksRender();
				context.takeScreenshot("tiny_takeover_babies_" + (g + 1));
			}

			server.runCommand("tp @p 120.5 -59 1.5 0 35");
			context.waitTicks(30);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("tiny_takeover_golden_dandelion");

			List<String> failures = new ArrayList<>();
			failures.addAll(server.computeOnServer(TinyTakeoverClientTest::checkAgeLock));
			failures.addAll(server.computeOnServer(TinyTakeoverClientTest::checkHitboxes));
			failures.addAll(server.computeOnServer(TinyTakeoverClientTest::checkSoundVariants));
			failures.addAll(server.computeOnServer(TinyTakeoverClientTest::checkTrumpet));
			if (!failures.isEmpty()) {
				throw new AssertionError(String.join("; ", failures));
			}

			context.waitTicks(5);
			context.takeScreenshot("tiny_takeover_age_lock_particles");
		}
	}

	/** A golden dandelion stops a calf from growing and is used up; a second use (after the cooldown) unlocks it. */
	private static List<String> checkAgeLock(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		ServerPlayer player = level.players().getFirst();
		Cow calf = EntityType.COW.create(level, EntitySpawnReason.COMMAND);
		calf.snapTo(120.5, -60.0, 6.5, 180.0F, 0.0F);
		calf.setAge(-24000);
		calf.setNoAi(true);
		level.addFreshEntity(calf);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TinyItems.GOLDEN_DANDELION, 2));
		player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
		calf.interact(player, InteractionHand.MAIN_HAND);
		player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
		AgeLockable lockable = (AgeLockable)calf;
		if (!lockable.everythingBackport$isAgeLocked()) {
			failures.add("golden dandelion did not age lock the calf");
		}

		if (player.getMainHandItem().getCount() != 1) {
			failures.add("golden dandelion was not used up (count " + player.getMainHandItem().getCount() + ")");
		}

		int age = calf.getAge();
		for (int i = 0; i < 100; i++) {
			calf.aiStep();
		}

		if (calf.getAge() != age) {
			failures.add("age-locked calf kept growing (" + age + " to " + calf.getAge() + ")");
		}

		calf.setAge(-24000);
		return failures;
	}

	private static List<String> checkHitboxes(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		Villager villager = EntityType.VILLAGER.create(level, EntitySpawnReason.COMMAND);
		villager.setAge(-24000);
		villager.refreshDimensions();
		if (Math.abs(villager.getBbHeight() - 0.99F) > 0.01F || Math.abs(villager.getBbWidth() - 0.49F) > 0.01F) {
			failures.add("baby villager hitbox is " + villager.getBbWidth() + "x" + villager.getBbHeight() + ", expected 0.49x0.99");
		}

		return failures;
	}

	/** Pigs, cows, chickens and cats pick a sound variant when they spawn and save it. */
	private static List<String> checkSoundVariants(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		for (String type : List.of("pig", "cow", "chicken", "cat")) {
			String tag = "sound_" + type;
			try {
				server.getCommands().getDispatcher().execute("summon " + type + " 140 -60 4 {NoAI:1b,Tags:[\"" + tag + "\"]}", server.createCommandSourceStack());
				int found = server.getCommands()
					.getDispatcher()
					.execute("execute if data entity @e[tag=" + tag + ",limit=1] sound_variant", server.createCommandSourceStack());
				if (found != 1) {
					failures.add(type + " has no sound_variant");
				}
			} catch (CommandSyntaxException e) {
				failures.add(type + ": " + e.getMessage());
			}
		}

		return failures;
	}

	/** A note block on copper plays the 26.1 trumpet. */
	private static List<String> checkTrumpet(MinecraftServer server) {
		ServerLevel level = server.overworld();
		BlockPos noteBlock = new BlockPos(150, -59, 4);
		server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "setblock 150 -59 4 note_block");
		server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "setblock 150 -60 4 copper_block");
		Object instrument = level.getBlockState(noteBlock).getValue(NoteBlock.INSTRUMENT);
		return instrument == TrumpetInstruments.TRUMPET ? List.of() : List.of("note block on copper plays " + instrument + ", not the trumpet");
	}
}
