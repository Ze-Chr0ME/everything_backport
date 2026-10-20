package backport.everything_backport.test;

import backport.everything_backport.client.mountsofmayhem.screen.NautilusInventoryScreen;
import backport.everything_backport.mountsofmayhem.MountsEntities;
import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.entity.CamelHusk;
import backport.everything_backport.mountsofmayhem.entity.Nautilus;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.CameraType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;

/** Builds a scene with the Mounts of Mayhem content, saves screenshots and checks spears, jockeys and the nautilus inventory. */
public class MountsOfMayhemClientTest implements FabricClientGameTest {
	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			TestServerContext server = singleplayer.getServer();
			singleplayer.getClientWorld().waitForChunksRender();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("gamerule doMobSpawning false");
			// Night with night vision: zombie nautiluses and zombie horses burn in daylight since 1.21.11.
			server.runCommand("time set midnight");
			server.runCommand("gamemode creative @a");
			server.runCommand("effect give @a night_vision infinite 0 true");

			server.runCommand(
				"summon nautilus -6 -60 4 {NoAI:1b,Rotation:[180f,0f],equipment:{saddle:{id:\"minecraft:saddle\",count:1},body:{id:\"minecraft:iron_nautilus_armor\",count:1}}}"
			);
			server.runCommand("summon nautilus -3 -60 4 {NoAI:1b,Age:-24000,Rotation:[180f,0f]}");
			server.runCommand("summon zombie_nautilus 0 -60 4 {NoAI:1b,variant:\"minecraft:warm\",Rotation:[180f,0f]}");
			server.runCommand(
				"summon zombie_nautilus 3 -60 4 {NoAI:1b,variant:\"minecraft:temperate\",Rotation:[180f,0f],equipment:{body:{id:\"minecraft:diamond_nautilus_armor\",count:1}}}"
			);
			server.runCommand("summon camel_husk 7 -60 5 {NoAI:1b,Rotation:[180f,0f],equipment:{saddle:{id:\"minecraft:saddle\",count:1}}}");
			server.runCommand("summon parched -6 -60 9 {NoAI:1b,Rotation:[180f,0f],equipment:{mainhand:{id:\"minecraft:bow\",count:1}}}");
			server.runCommand(
				"summon zombie_horse -2.5 -60 9 {NoAI:1b,Tame:1b,Rotation:[180f,0f],equipment:{body:{id:\"minecraft:diamond_horse_armor\",count:1},saddle:{id:\"minecraft:saddle\",count:1}}}"
			);
			server.runCommand("summon zombie 1.5 -60 8 {NoAI:1b,Rotation:[180f,0f],equipment:{mainhand:{id:\"minecraft:iron_spear\",count:1}}}");
			server.runCommand("summon piglin 4 -60 8 {NoAI:1b,IsImmuneToZombification:1b,Rotation:[180f,0f],equipment:{mainhand:{id:\"minecraft:golden_spear\",count:1}}}");

			String[] hotbar = {
				"wooden_spear", "stone_spear", "copper_spear", "iron_spear", "golden_spear", "diamond_spear", "netherite_spear", "copper_nautilus_armor",
				"netherite_horse_armor"
			};
			for (int i = 0; i < hotbar.length; i++) {
				server.runCommand("item replace entity @p hotbar." + i + " with " + hotbar[i]);
			}

			server.runCommand("tp @p 0.5 -59 -3.5 0 15");
			context.waitTicks(40);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("mounts_of_mayhem_overview");

			// Holding a spear, then charging with it.
			context.getInput().pressKey(options -> options.keyHotbarSlots[3]);
			context.waitTicks(10);
			context.takeScreenshot("mounts_of_mayhem_spear_held");
			context.getInput().holdKey(options -> options.keyUse);
			context.waitTicks(12);
			context.takeScreenshot("mounts_of_mayhem_spear_charging");
			context.runOnClient(client -> client.options.setCameraType(CameraType.THIRD_PERSON_FRONT));
			context.waitTicks(5);
			context.takeScreenshot("mounts_of_mayhem_spear_charging_third_person");
			context.getInput().releaseKey(options -> options.keyUse);
			context.runOnClient(client -> client.options.setCameraType(CameraType.FIRST_PERSON));

			testStab(context, server);
			testJockeys(server);
			testNautilusInventory(context, server);
		}
	}

	/** A charged spear stab hurts the mob in front of the player. */
	private static void testStab(ClientGameTestContext context, TestServerContext server) {
		server.runCommand("gamemode survival @a");
		server.runCommand("effect give @a resistance 60 4 true");
		server.runCommand("summon zombie 40.5 -60 3.5 {NoAI:1b,Tags:[\"stab_target\"],Rotation:[180f,0f]}");
		server.runCommand("tp @p 40.5 -60 0.5 0 10");
		context.waitTicks(40);
		context.getInput().pressKey(options -> options.keyAttack);
		context.waitTicks(10);
		context.takeScreenshot("mounts_of_mayhem_spear_stab");
		float health = server.computeOnServer(
			s -> s.overworld().getEntities(EntityType.ZOMBIE, zombie -> zombie.getTags().contains("stab_target")).stream().findFirst().map(Zombie::getHealth).orElse(-1.0F)
		);
		if (!(health >= 0.0F && health < 20.0F)) {
			throw new AssertionError("The spear stab did not hurt the zombie (health " + health + ")");
		}


		// Lunge pushes the player forward after a stab.
		server.runCommand("item replace entity @p weapon.mainhand with iron_spear[enchantments={\"minecraft:lunge\":3}]");
		server.runCommand("tp @p 60.5 -60 0.5 0 10");
		context.waitTicks(40);
		double before = server.computeOnServer(s -> s.overworld().players().getFirst().getZ());
		context.getInput().pressKey(options -> options.keyAttack);
		context.waitTicks(10);
		double after = server.computeOnServer(s -> s.overworld().players().getFirst().getZ());
		if (!(after - before > 0.5)) {
			throw new AssertionError("Lunge did not push the player forward (moved " + (after - before) + ")");
		}

		server.runCommand("gamemode creative @a");
	}

	/** Naturally spawned husks, zombie horses and trident drowned bring their 1.21.11 mounts and riders. */
	private static void testJockeys(TestServerContext server) {
		String result = server.computeOnServer(s -> {
			ServerLevel level = s.overworld();
			List<String> missing = new ArrayList<>();
			if (!spawnsJockey(level, EntityType.HUSK, 400, mob -> mob.getVehicle() instanceof CamelHusk && mob.getMainHandItem().is(MountsItems.IRON_SPEAR))) {
				missing.add("husk on a camel husk");
			}

			if (!spawnsJockey(level, EntityType.ZOMBIE_HORSE, 5, mob -> mob.getFirstPassenger() instanceof Zombie zombie && zombie.getMainHandItem().is(MountsItems.IRON_SPEAR))) {
				missing.add("spear zombie on a zombie horse");
			}

			if (!spawnsJockey(level, EntityType.DROWNED, 1500, mob -> mob.getVehicle() instanceof ZombieNautilus)) {
				missing.add("drowned on a zombie nautilus");
			}

			return String.join(", ", missing);
		});
		if (!result.isEmpty()) {
			throw new AssertionError("Natural spawns are missing: " + result);
		}
	}

	private static boolean spawnsJockey(ServerLevel level, EntityType<? extends Mob> type, int attempts, Predicate<Mob> check) {
		for (int i = 0; i < attempts; i++) {
			Mob mob = type.create(level, EntitySpawnReason.NATURAL);
			if (mob == null) {
				return false;
			}

			mob.snapTo(80.5, -60.0, 80.5 + (i % 20) * 3, 0.0F, 0.0F);
			mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.NATURAL, null);
			level.addFreshEntityWithPassengers(mob);
			boolean found = check.test(mob);
			mob.getRootVehicle().getSelfAndPassengers().toList().forEach(Entity::discard);
			if (found) {
				return true;
			}
		}

		return false;
	}

	/** A tamed nautilus opens its saddle and armor inventory. */
	private static void testNautilusInventory(ClientGameTestContext context, TestServerContext server) {
		server.runOnServer(s -> {
			ServerLevel level = s.overworld();
			ServerPlayer player = level.players().getFirst();
			Nautilus nautilus = MountsEntities.NAUTILUS.create(level, EntitySpawnReason.COMMAND);
			nautilus.snapTo(player.getX(), player.getY(), player.getZ() + 3.0, 180.0F, 0.0F);
			nautilus.setNoAi(true);
			nautilus.tame(player);
			nautilus.setItemSlot(net.minecraft.world.entity.EquipmentSlot.BODY, new ItemStack(MountsItems.GOLDEN_NAUTILUS_ARMOR));
			level.addFreshEntity(nautilus);
			nautilus.openCustomInventoryScreen(player);
		});
		context.waitTicks(10);
		boolean open = context.computeOnClient(client -> client.screen instanceof NautilusInventoryScreen);
		if (!open) {
			throw new AssertionError("The nautilus inventory screen did not open");
		}

		context.takeScreenshot("mounts_of_mayhem_nautilus_inventory");
		context.runOnClient(client -> client.setScreen(null));
	}
}
