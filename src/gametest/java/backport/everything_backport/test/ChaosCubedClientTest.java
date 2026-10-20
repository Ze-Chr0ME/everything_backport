package backport.everything_backport.test;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import backport.everything_backport.chaoscubed.ChaosBlocks;
import backport.everything_backport.chaoscubed.ChaosComponents;
import backport.everything_backport.chaoscubed.ChaosEntities;
import backport.everything_backport.chaoscubed.ChaosItems;
import backport.everything_backport.chaoscubed.ChaosRegistries;
import backport.everything_backport.chaoscubed.ChaosWorldgen;
import backport.everything_backport.chaoscubed.block.PotentSulfurBlock;
import backport.everything_backport.chaoscubed.block.PotentSulfurState;
import backport.everything_backport.chaoscubed.entity.SulfurCube;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Builds Chaos Cubed scenes, saves screenshots and checks the sulfur cube archetypes, potent sulfur states, sulfur worldgen
 * features and the sulfur caves placement, first in a flat world and then in a generated one.
 */
public class ChaosCubedClientTest implements FabricClientGameTest {
	private static final String[] BLOCKS = {
		"cinnabar", "cinnabar_stairs", "cinnabar_slab", "cinnabar_wall", "chiseled_cinnabar", "polished_cinnabar", "polished_cinnabar_stairs",
		"polished_cinnabar_slab", "polished_cinnabar_wall", "cinnabar_bricks", "cinnabar_brick_stairs", "cinnabar_brick_slab", "cinnabar_brick_wall",
		"sulfur", "sulfur_stairs", "sulfur_slab", "sulfur_wall", "chiseled_sulfur", "polished_sulfur", "polished_sulfur_stairs", "polished_sulfur_slab",
		"polished_sulfur_wall", "sulfur_bricks", "sulfur_brick_stairs", "sulfur_brick_slab", "sulfur_brick_wall", "potent_sulfur"
	};
	/** Seed of the generated world; the sulfur caves search starts from spawn. */
	private static final String SEED = "26.2";

	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			TestServerContext server = singleplayer.getServer();
			singleplayer.getClientWorld().waitForChunksRender();
			server.runCommand("gamerule doDaylightCycle false");
			server.runCommand("gamerule doMobSpawning false");
			server.runCommand("time set noon");
			server.runCommand("gamemode creative @a");

			// Every building block in two rows, sulfur spikes hanging from a sulfur ceiling and growing from the floor.
			for (int i = 0; i < BLOCKS.length; i++) {
				int x = (i % 14) * 2 - 13;
				int z = 6 + (i / 14) * 3;
				server.runCommand("setblock " + x + " -60 " + z + " " + BLOCKS[i]);
			}

			server.runCommand("fill 16 -55 5 20 -55 7 sulfur");
			server.runCommand("setblock 17 -56 6 sulfur_spike[vertical_direction=down,thickness=base]");
			server.runCommand("setblock 17 -57 6 sulfur_spike[vertical_direction=down,thickness=frustum]");
			server.runCommand("setblock 17 -58 6 sulfur_spike[vertical_direction=down,thickness=tip]");
			server.runCommand("setblock 19 -56 6 sulfur_spike[vertical_direction=down,thickness=tip]");
			server.runCommand("setblock 17 -61 6 sulfur");
			server.runCommand("setblock 17 -60 6 sulfur_spike[vertical_direction=up,thickness=frustum]");
			server.runCommand("setblock 17 -59 6 sulfur_spike[vertical_direction=up,thickness=tip]");
			server.runCommand("tp @p 2.5 -57 -6.5 0 25");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("chaos_cubed_blocks");

			// Sulfur cubes: empty adult and baby, and adults holding TNT, planks, magma and a honeycomb block.
			String cube = "NoAI:1b,Silent:1b,PersistenceRequired:1b,Rotation:[180f,0f]";
			server.runCommand("summon sulfur_cube 40 -60 6 {" + cube + ",Size:1}");
			server.runCommand("summon sulfur_cube 42.5 -60 6 {" + cube + ",Size:0,Age:-24000}");
			String[] held = {"tnt", "oak_planks", "magma_block", "honeycomb_block"};
			for (int i = 0; i < held.length; i++) {
				server.runCommand(
					"summon sulfur_cube " + (45 + i * 2.5) + " -60 6 {" + cube + ",Size:1,equipment:{body:{id:\"minecraft:" + held[i] + "\",count:1}}}"
				);
			}

			server.runCommand("tp @p 46 -58.5 1.5 0 20");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("chaos_cubed_sulfur_cubes");

			// A continuous geyser (lava under potent sulfur under three water blocks) and a periodic one (magma), in glass columns.
			for (int x : new int[]{80, 84}) {
				server.runCommand("fill " + (x - 1) + " -63 5 " + (x + 1) + " -57 7 glass");
				server.runCommand("setblock " + x + " -63 6 " + (x == 80 ? "lava" : "magma_block"));
				server.runCommand("setblock " + x + " -62 6 potent_sulfur");
				server.runCommand("fill " + x + " -61 6 " + x + " -59 6 water");
				server.runCommand("fill " + x + " -58 6 " + x + " -57 6 air");
			}

			server.runCommand("tp @p 82 -56 -2 0 20");
			context.waitTicks(60);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("chaos_cubed_geysers");

			List<String> failures = new ArrayList<>();
			failures.addAll(server.computeOnServer(ChaosCubedClientTest::checkRegistries));
			failures.addAll(server.computeOnServer(ChaosCubedClientTest::checkSulfurCube));
			failures.addAll(server.computeOnServer(ChaosCubedClientTest::checkPotentSulfur));
			failures.addAll(server.computeOnServer(ChaosCubedClientTest::checkBiomeSource));
			// Features parse and place: a spike cluster on a sulfur floor and ceiling, and a sulfur spring.
			server.runCommand("fill 120 -60 0 140 -50 20 air");
			server.runCommand("fill 120 -61 0 140 -61 20 sulfur");
			server.runCommand("fill 120 -50 0 140 -50 20 sulfur");
			server.runCommand("place feature minecraft:sulfur_spike_cluster 130 -55 10");
			server.runCommand("place feature minecraft:sulfur_spring 150 -60 10");
			failures.addAll(server.computeOnServer(ChaosCubedClientTest::checkFeatures));
			server.runCommand("tp @p 130 -58 -4 0 15");
			context.waitTicks(20);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("chaos_cubed_spike_cluster");
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
			server.runCommand("gamemode spectator @a");
			server.runCommand("effect give @a night_vision infinite 0 true");
			BlockPos cave = server.computeOnServer(ChaosCubedClientTest::findSulfurCaves);
			if (cave == null) {
				throw new AssertionError("no sulfur caves within 4096 blocks of spawn in a generated world");
			}

			server.runCommand("forceload add " + cave.getX() + " " + cave.getZ());
			server.runCommand("tp @p " + cave.getX() + " " + cave.getY() + " " + cave.getZ() + " 45 10");
			context.waitTicks(100);
			singleplayer.getClientWorld().waitForChunksRender();
			context.takeScreenshot("chaos_cubed_sulfur_caves");
			List<String> failures = server.computeOnServer(server2 -> checkSulfurCavesSurface(server2, cave));
			if (!failures.isEmpty()) {
				throw new AssertionError(String.join("; ", failures));
			}
		}
	}

	private static List<String> checkRegistries(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		int archetypes = server.registryAccess().lookupOrThrow(ChaosRegistries.SULFUR_CUBE_ARCHETYPE).listElements().toList().size();
		if (archetypes != 12) {
			failures.add("expected 12 sulfur cube archetypes, found " + archetypes);
		}

		if (server.registryAccess().lookupOrThrow(Registries.BIOME).get(ChaosWorldgen.SULFUR_CAVES).isEmpty()) {
			failures.add("sulfur_caves biome is not registered");
		}

		return failures;
	}

	/** Swallowing picks the archetype (planks bounce, TNT can be primed), shearing ejects the block and buckets keep it. */
	private static List<String> checkSulfurCube(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		SulfurCube cube = ChaosEntities.SULFUR_CUBE.create(level, EntitySpawnReason.COMMAND);
		cube.snapTo(60.5, -60.0, 20.5, 0.0F, 0.0F);
		cube.setSize(SulfurCube.MAX_SIZE, true);
		level.addFreshEntity(cube);
		if (!cube.equipItem(new ItemStack(Items.OAK_PLANKS))) {
			failures.add("sulfur cube did not swallow oak planks");
		}

		double bounciness = cube.getAttributeValue(ChaosAttributes.BOUNCINESS);
		if (Math.abs(bounciness - 0.9) > 0.001) {
			failures.add("bouncy archetype gave bounciness " + bounciness + ", expected 0.9");
		}

		ItemStack bucket = new ItemStack(ChaosItems.SULFUR_CUBE_BUCKET);
		cube.saveToBucketTag(bucket);
		if (bucket.get(ChaosComponents.SULFUR_CUBE_CONTENT) == null || !bucket.get(ChaosComponents.SULFUR_CUBE_CONTENT).absorbedBlockItemStack().is(Items.OAK_PLANKS)) {
			failures.add("bucket did not keep the swallowed block");
		}

		cube.shear(level, SoundSource.PLAYERS, new ItemStack(Items.SHEARS));
		if (cube.hasBodyItem()) {
			failures.add("shearing did not eject the swallowed block");
		}

		if (cube.getAttributeValue(ChaosAttributes.BOUNCINESS) != 0.0) {
			cube.tick();
		}

		if (cube.getAttributeValue(ChaosAttributes.BOUNCINESS) != 0.0) {
			failures.add("bounciness stayed after the block was ejected");
		}

		SulfurCube explosive = ChaosEntities.SULFUR_CUBE.create(level, EntitySpawnReason.COMMAND);
		explosive.snapTo(64.5, -60.0, 20.5, 0.0F, 0.0F);
		explosive.setSize(SulfurCube.MAX_SIZE, true);
		level.addFreshEntity(explosive);
		explosive.equipItem(new ItemStack(Items.TNT));
		if (!explosive.canExplode() || !explosive.primeTime(false) || !explosive.isPrimed()) {
			failures.add("a sulfur cube holding TNT could not be primed");
		}

		explosive.discard();
		SulfurCube baby = ChaosEntities.SULFUR_CUBE.create(level, EntitySpawnReason.COMMAND);
		baby.setSize(1, true);
		if (!baby.isBaby() || baby.equipItem(new ItemStack(Items.OAK_PLANKS))) {
			failures.add("size 1 sulfur cubes should be babies that cannot swallow blocks");
		}

		return failures;
	}

	/** Potent sulfur under water is a continuous geyser over lava and a periodic one over magma. */
	private static List<String> checkPotentSulfur(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		BlockState continuous = level.getBlockState(new BlockPos(80, -62, 6));
		BlockState periodic = level.getBlockState(new BlockPos(84, -62, 6));
		if (!continuous.is(ChaosBlocks.POTENT_SULFUR) || continuous.getValue(PotentSulfurBlock.STATE) != PotentSulfurState.CONTINUOUS) {
			failures.add("potent sulfur over lava is " + continuous + ", expected continuous");
		}

		if (!periodic.is(ChaosBlocks.POTENT_SULFUR)
			|| periodic.getValue(PotentSulfurBlock.STATE) != PotentSulfurState.DORMANT && periodic.getValue(PotentSulfurBlock.STATE) != PotentSulfurState.ERUPTING) {
			failures.add("potent sulfur over magma is " + periodic + ", expected dormant or erupting");
		}

		return failures;
	}

	/** The vanilla overworld source gains sulfur caves at 26.2's climate and keeps lush caves where both fit. */
	private static List<String> checkBiomeSource(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		MultiNoiseBiomeSource source = MultiNoiseBiomeSource.createFromPreset(
			server.registryAccess().lookupOrThrow(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST).getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD)
		);
		if (source.possibleBiomes().stream().noneMatch(biome -> biome.is(ChaosWorldgen.SULFUR_CAVES))) {
			failures.add("sulfur caves are not a possible biome of the overworld source");
		}

		Holder<Biome> inside = source.getNoiseBiome(Climate.target(0.0F, 0.0F, 0.2F, 0.6F, 0.5F, -0.95F));
		if (!inside.is(ChaosWorldgen.SULFUR_CAVES)) {
			failures.add("sulfur caves climate gave " + inside.getRegisteredName());
		}

		Holder<Biome> lush = source.getNoiseBiome(Climate.target(0.0F, 0.9F, 0.2F, 0.6F, 0.5F, -0.95F));
		if (lush.is(ChaosWorldgen.SULFUR_CAVES)) {
			failures.add("sulfur caves replaced lush caves");
		}

		Holder<Biome> surface = source.getNoiseBiome(Climate.target(0.0F, 0.0F, 0.2F, 0.6F, 0.0F, -0.95F));
		if (surface.is(ChaosWorldgen.SULFUR_CAVES)) {
			failures.add("sulfur caves reached the surface");
		}

		return failures;
	}

	private static List<String> checkFeatures(MinecraftServer server) {
		List<String> failures = new ArrayList<>();
		ServerLevel level = server.overworld();
		int spikes = 0;
		for (BlockPos pos : BlockPos.betweenClosed(120, -60, 0, 140, -51, 20)) {
			if (level.getBlockState(pos).is(ChaosBlocks.SULFUR_SPIKE)) {
				spikes++;
			}
		}

		if (spikes == 0) {
			failures.add("the sulfur spike cluster placed no spikes");
		}

		int potent = 0;
		for (BlockPos pos : BlockPos.betweenClosed(138, -70, -2, 162, -52, 22)) {
			if (level.getBlockState(pos).is(ChaosBlocks.POTENT_SULFUR)) {
				potent++;
			}
		}

		if (potent == 0) {
			failures.add("the sulfur spring placed no potent sulfur");
		}

		return failures;
	}

	/** A cave block in sulfur caves near spawn, found the way {@code /locate biome} does, then the nearest air in it. */
	private static BlockPos findSulfurCaves(MinecraftServer server) {
		ServerLevel level = server.overworld();
		BlockPos spawn = level.getSharedSpawnPos();
		BiomeSource source = level.getChunkSource().getGenerator().getBiomeSource();
		Pair<BlockPos, Holder<Biome>> found = level.findClosestBiome3d(biome -> biome.is(ChaosWorldgen.SULFUR_CAVES), spawn, 4096, 32, 8);
		if (found == null || source.possibleBiomes().stream().noneMatch(biome -> biome.is(ChaosWorldgen.SULFUR_CAVES))) {
			return null;
		}

		BlockPos center = found.getFirst();
		for (BlockPos pos : BlockPos.withinManhattan(center, 12, 12, 12)) {
			if (level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir() && level.getBiome(pos).is(ChaosWorldgen.SULFUR_CAVES)) {
				return pos.immutable();
			}
		}

		return center;
	}

	/** The sulfur caves surface rule turns the biome's stone into bands of sulfur and cinnabar. */
	private static List<String> checkSulfurCavesSurface(MinecraftServer server, BlockPos cave) {
		ServerLevel level = server.overworld();
		int sulfur = 0;
		int cinnabar = 0;
		for (BlockPos pos : BlockPos.betweenClosed(cave.offset(-12, -12, -12), cave.offset(12, 12, 12))) {
			BlockState state = level.getBlockState(pos);
			if (state.is(ChaosBlocks.SULFUR)) {
				sulfur++;
			} else if (state.is(ChaosBlocks.CINNABAR)) {
				cinnabar++;
			}
		}

		return sulfur > 0 && cinnabar > 0 ? List.of() : List.of("sulfur caves at " + cave + " have " + sulfur + " sulfur and " + cinnabar + " cinnabar blocks nearby");
	}
}
