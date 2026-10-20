package backport.everything_backport.copperage;

import backport.everything_backport.copperage.block.CopperChestBlock;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import backport.everything_backport.copperage.block.ShelfBlock;
import backport.everything_backport.copperage.block.WeatheringCopperBarsBlock;
import backport.everything_backport.copperage.block.WeatheringCopperBlocks;
import backport.everything_backport.copperage.block.WeatheringCopperChainBlock;
import backport.everything_backport.copperage.block.WeatheringCopperChestBlock;
import backport.everything_backport.copperage.block.WeatheringCopperGolemStatueBlock;
import backport.everything_backport.copperage.block.WeatheringLanternBlock;
import backport.everything_backport.copperage.block.WeatheringLightningRodBlock;
import backport.everything_backport.registry.Reg;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class CopperAgeBlocks {
	public static final Block ACACIA_SHELF = shelf("acacia_shelf");
	public static final Block BAMBOO_SHELF = shelf("bamboo_shelf");
	public static final Block BIRCH_SHELF = shelf("birch_shelf");
	public static final Block CHERRY_SHELF = shelf("cherry_shelf");
	public static final Block CRIMSON_SHELF = shelf("crimson_shelf");
	public static final Block DARK_OAK_SHELF = shelf("dark_oak_shelf");
	public static final Block JUNGLE_SHELF = shelf("jungle_shelf");
	public static final Block MANGROVE_SHELF = shelf("mangrove_shelf");
	public static final Block OAK_SHELF = shelf("oak_shelf");
	public static final Block PALE_OAK_SHELF = shelf("pale_oak_shelf");
	public static final Block SPRUCE_SHELF = shelf("spruce_shelf");
	public static final Block WARPED_SHELF = shelf("warped_shelf");
	public static final List<Block> SHELVES = List.of(
		OAK_SHELF, SPRUCE_SHELF, BIRCH_SHELF, JUNGLE_SHELF, ACACIA_SHELF, DARK_OAK_SHELF, MANGROVE_SHELF, CHERRY_SHELF, PALE_OAK_SHELF, BAMBOO_SHELF, CRIMSON_SHELF,
		WARPED_SHELF
	);

	public static final Block COPPER_TORCH = Reg.block(
		"copper_torch",
		properties -> new TorchBlock(CopperAgeParticles.COPPER_FIRE_FLAME, properties),
		BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(state -> 14).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)
	);
	public static final Block COPPER_WALL_TORCH = Reg.block(
		"copper_wall_torch",
		properties -> new WallTorchBlock(CopperAgeParticles.COPPER_FIRE_FLAME, properties),
		BlockBehaviour.Properties.of()
			.overrideLootTable(COPPER_TORCH.getLootTable())
			.overrideDescription(COPPER_TORCH.getDescriptionId())
			.noCollission()
			.instabreak()
			.lightLevel(state -> 14)
			.sound(SoundType.WOOD)
			.pushReaction(PushReaction.DESTROY)
	);

	public static final WeatheringCopperBlocks COPPER_BARS = WeatheringCopperBlocks.create(
		"copper_bars",
		IronBarsBlock::new,
		WeatheringCopperBarsBlock::new,
		weatherState -> BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER).noOcclusion()
	);
	public static final WeatheringCopperBlocks COPPER_CHAIN = WeatheringCopperBlocks.create(
		"copper_chain",
		ChainBlock::new,
		WeatheringCopperChainBlock::new,
		weatherState -> BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()
	);

	public static final Block COPPER_CHEST = Reg.block(
		"copper_chest",
		properties -> new WeatheringCopperChestBlock(
			WeatheringCopper.WeatherState.UNAFFECTED, CopperAgeSounds.COPPER_CHEST_OPEN, CopperAgeSounds.COPPER_CHEST_CLOSE, properties
		),
		BlockBehaviour.Properties.of().mapColor(Blocks.COPPER_BLOCK.defaultMapColor()).strength(3.0F, 6.0F).sound(SoundType.COPPER).requiresCorrectToolForDrops()
	);
	public static final Block EXPOSED_COPPER_CHEST = Reg.block(
		"exposed_copper_chest",
		properties -> new WeatheringCopperChestBlock(
			WeatheringCopper.WeatherState.EXPOSED, CopperAgeSounds.COPPER_CHEST_OPEN, CopperAgeSounds.COPPER_CHEST_CLOSE, properties
		),
		BlockBehaviour.Properties.ofFullCopy(COPPER_CHEST).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
	);
	public static final Block WEATHERED_COPPER_CHEST = Reg.block(
		"weathered_copper_chest",
		properties -> new WeatheringCopperChestBlock(
			WeatheringCopper.WeatherState.WEATHERED, CopperAgeSounds.COPPER_CHEST_WEATHERED_OPEN, CopperAgeSounds.COPPER_CHEST_WEATHERED_CLOSE, properties
		),
		BlockBehaviour.Properties.ofFullCopy(COPPER_CHEST).mapColor(MapColor.WARPED_STEM)
	);
	public static final Block OXIDIZED_COPPER_CHEST = Reg.block(
		"oxidized_copper_chest",
		properties -> new WeatheringCopperChestBlock(
			WeatheringCopper.WeatherState.OXIDIZED, CopperAgeSounds.COPPER_CHEST_OXIDIZED_OPEN, CopperAgeSounds.COPPER_CHEST_OXIDIZED_CLOSE, properties
		),
		BlockBehaviour.Properties.ofFullCopy(COPPER_CHEST).mapColor(MapColor.WARPED_NYLIUM)
	);
	public static final Block WAXED_COPPER_CHEST = Reg.block(
		"waxed_copper_chest",
		properties -> new CopperChestBlock(WeatheringCopper.WeatherState.UNAFFECTED, CopperAgeSounds.COPPER_CHEST_OPEN, CopperAgeSounds.COPPER_CHEST_CLOSE, properties),
		BlockBehaviour.Properties.ofFullCopy(COPPER_CHEST)
	);
	public static final Block WAXED_EXPOSED_COPPER_CHEST = Reg.block(
		"waxed_exposed_copper_chest",
		properties -> new CopperChestBlock(WeatheringCopper.WeatherState.EXPOSED, CopperAgeSounds.COPPER_CHEST_OPEN, CopperAgeSounds.COPPER_CHEST_CLOSE, properties),
		BlockBehaviour.Properties.ofFullCopy(EXPOSED_COPPER_CHEST)
	);
	public static final Block WAXED_WEATHERED_COPPER_CHEST = Reg.block(
		"waxed_weathered_copper_chest",
		properties -> new CopperChestBlock(
			WeatheringCopper.WeatherState.WEATHERED, CopperAgeSounds.COPPER_CHEST_WEATHERED_OPEN, CopperAgeSounds.COPPER_CHEST_WEATHERED_CLOSE, properties
		),
		BlockBehaviour.Properties.ofFullCopy(WEATHERED_COPPER_CHEST)
	);
	public static final Block WAXED_OXIDIZED_COPPER_CHEST = Reg.block(
		"waxed_oxidized_copper_chest",
		properties -> new CopperChestBlock(
			WeatheringCopper.WeatherState.OXIDIZED, CopperAgeSounds.COPPER_CHEST_OXIDIZED_OPEN, CopperAgeSounds.COPPER_CHEST_OXIDIZED_CLOSE, properties
		),
		BlockBehaviour.Properties.ofFullCopy(OXIDIZED_COPPER_CHEST)
	);
	public static final List<Block> COPPER_CHESTS = List.of(
		COPPER_CHEST, EXPOSED_COPPER_CHEST, WEATHERED_COPPER_CHEST, OXIDIZED_COPPER_CHEST, WAXED_COPPER_CHEST, WAXED_EXPOSED_COPPER_CHEST,
		WAXED_WEATHERED_COPPER_CHEST, WAXED_OXIDIZED_COPPER_CHEST
	);

	public static final Block COPPER_GOLEM_STATUE = Reg.block(
		"copper_golem_statue",
		properties -> new WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties),
		BlockBehaviour.Properties.of()
			.mapColor(Blocks.COPPER_BLOCK.defaultMapColor())
			.strength(3.0F, 6.0F)
			.sound(CopperAgeSounds.COPPER_GOLEM_STATUE_SOUND_TYPE)
			.pushReaction(PushReaction.DESTROY)
	);
	public static final Block EXPOSED_COPPER_GOLEM_STATUE = Reg.block(
		"exposed_copper_golem_statue",
		properties -> new WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState.EXPOSED, properties),
		BlockBehaviour.Properties.ofFullCopy(COPPER_GOLEM_STATUE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
	);
	public static final Block WEATHERED_COPPER_GOLEM_STATUE = Reg.block(
		"weathered_copper_golem_statue",
		properties -> new WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState.WEATHERED, properties),
		BlockBehaviour.Properties.ofFullCopy(COPPER_GOLEM_STATUE).mapColor(MapColor.WARPED_STEM)
	);
	public static final Block OXIDIZED_COPPER_GOLEM_STATUE = Reg.block(
		"oxidized_copper_golem_statue",
		properties -> new WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState.OXIDIZED, properties),
		BlockBehaviour.Properties.ofFullCopy(COPPER_GOLEM_STATUE).mapColor(MapColor.WARPED_NYLIUM)
	);
	public static final Block WAXED_COPPER_GOLEM_STATUE = Reg.block(
		"waxed_copper_golem_statue",
		properties -> new CopperGolemStatueBlock(WeatheringCopper.WeatherState.UNAFFECTED, properties),
		BlockBehaviour.Properties.ofFullCopy(COPPER_GOLEM_STATUE)
	);
	public static final Block WAXED_EXPOSED_COPPER_GOLEM_STATUE = Reg.block(
		"waxed_exposed_copper_golem_statue",
		properties -> new CopperGolemStatueBlock(WeatheringCopper.WeatherState.EXPOSED, properties),
		BlockBehaviour.Properties.ofFullCopy(EXPOSED_COPPER_GOLEM_STATUE)
	);
	public static final Block WAXED_WEATHERED_COPPER_GOLEM_STATUE = Reg.block(
		"waxed_weathered_copper_golem_statue",
		properties -> new CopperGolemStatueBlock(WeatheringCopper.WeatherState.WEATHERED, properties),
		BlockBehaviour.Properties.ofFullCopy(WEATHERED_COPPER_GOLEM_STATUE)
	);
	public static final Block WAXED_OXIDIZED_COPPER_GOLEM_STATUE = Reg.block(
		"waxed_oxidized_copper_golem_statue",
		properties -> new CopperGolemStatueBlock(WeatheringCopper.WeatherState.OXIDIZED, properties),
		BlockBehaviour.Properties.ofFullCopy(OXIDIZED_COPPER_GOLEM_STATUE)
	);
	public static final List<Block> COPPER_GOLEM_STATUES = List.of(
		COPPER_GOLEM_STATUE, EXPOSED_COPPER_GOLEM_STATUE, WEATHERED_COPPER_GOLEM_STATUE, OXIDIZED_COPPER_GOLEM_STATUE, WAXED_COPPER_GOLEM_STATUE,
		WAXED_EXPOSED_COPPER_GOLEM_STATUE, WAXED_WEATHERED_COPPER_GOLEM_STATUE, WAXED_OXIDIZED_COPPER_GOLEM_STATUE
	);

	public static final WeatheringCopperBlocks COPPER_LANTERN = WeatheringCopperBlocks.create(
		"copper_lantern",
		LanternBlock::new,
		WeatheringLanternBlock::new,
		weatherState -> BlockBehaviour.Properties.of()
			.mapColor(MapColor.METAL)
			.forceSolidOn()
			.strength(3.5F)
			.sound(SoundType.LANTERN)
			.lightLevel(state -> 15)
			.noOcclusion()
			.pushReaction(PushReaction.DESTROY)
	);

	public static final Block EXPOSED_LIGHTNING_ROD = Reg.block(
		"exposed_lightning_rod",
		properties -> new WeatheringLightningRodBlock(WeatheringCopper.WeatherState.EXPOSED, properties),
		BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
	);
	public static final Block WEATHERED_LIGHTNING_ROD = Reg.block(
		"weathered_lightning_rod",
		properties -> new WeatheringLightningRodBlock(WeatheringCopper.WeatherState.WEATHERED, properties),
		BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_STEM)
	);
	public static final Block OXIDIZED_LIGHTNING_ROD = Reg.block(
		"oxidized_lightning_rod",
		properties -> new WeatheringLightningRodBlock(WeatheringCopper.WeatherState.OXIDIZED, properties),
		BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD).mapColor(MapColor.WARPED_NYLIUM)
	);
	public static final Block WAXED_LIGHTNING_ROD = Reg.block("waxed_lightning_rod", LightningRodBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD));
	public static final Block WAXED_EXPOSED_LIGHTNING_ROD = Reg.block(
		"waxed_exposed_lightning_rod", LightningRodBlock::new, BlockBehaviour.Properties.ofFullCopy(EXPOSED_LIGHTNING_ROD)
	);
	public static final Block WAXED_WEATHERED_LIGHTNING_ROD = Reg.block(
		"waxed_weathered_lightning_rod", LightningRodBlock::new, BlockBehaviour.Properties.ofFullCopy(WEATHERED_LIGHTNING_ROD)
	);
	public static final Block WAXED_OXIDIZED_LIGHTNING_ROD = Reg.block(
		"waxed_oxidized_lightning_rod", LightningRodBlock::new, BlockBehaviour.Properties.ofFullCopy(OXIDIZED_LIGHTNING_ROD)
	);
	public static final List<Block> LIGHTNING_RODS = List.of(
		Blocks.LIGHTNING_ROD, EXPOSED_LIGHTNING_ROD, WEATHERED_LIGHTNING_ROD, OXIDIZED_LIGHTNING_ROD, WAXED_LIGHTNING_ROD, WAXED_EXPOSED_LIGHTNING_ROD,
		WAXED_WEATHERED_LIGHTNING_ROD, WAXED_OXIDIZED_LIGHTNING_ROD
	);

	private CopperAgeBlocks() {
	}

	private static Block shelf(String name) {
		return Reg.block(
			name,
			ShelfBlock::new,
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.WOOD)
				.instrument(NoteBlockInstrument.BASS)
				.sound(CopperAgeSounds.SHELF_SOUND_TYPE)
				.ignitedByLava()
				.strength(2.0F, 3.0F)
		);
	}

	static void init() {
	}
}
