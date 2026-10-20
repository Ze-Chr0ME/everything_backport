package backport.everything_backport.wildernessbound;

import backport.everything_backport.copperage.block.ShelfBlock;
import backport.everything_backport.copperage.CopperAgeSounds;
import backport.everything_backport.registry.Reg;
import backport.everything_backport.wildernessbound.block.PoplarLeavesBlock;
import backport.everything_backport.wildernessbound.block.PoplarSaplingBlock;
import backport.everything_backport.wildernessbound.block.ShelfMushroomBlock;
import backport.everything_backport.wildernessbound.block.StrawBedBlock;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class WildBlocks {
	public static final BlockSetType POPLAR_SET = BlockSetType.register(new BlockSetType("poplar"));
	public static final WoodType POPLAR_WOOD_TYPE = WoodType.register(new WoodType("poplar", POPLAR_SET));

	public static final Block POPLAR_PLANKS = Reg.block(
		"poplar_planks",
		BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()
	);
	public static final Block POPLAR_SAPLING = Reg.block(
		"poplar_sapling",
		properties -> new PoplarSaplingBlock(
			List.of(
				grower("poplar", WildWorldgen.RED_POPLAR),
				grower("everything_backport:orange_poplar", WildWorldgen.ORANGE_POPLAR),
				grower("everything_backport:yellow_poplar", WildWorldgen.YELLOW_POPLAR)
			),
			properties
		),
		BlockBehaviour.Properties.of().mapColor(MapColor.METAL).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)
	);
	public static final Block POPLAR_LOG = Reg.block(
		"poplar_log", RotatedPillarBlock::new, Blocks.logProperties(MapColor.COLOR_LIGHT_GRAY, MapColor.PODZOL, SoundType.WOOD)
	);
	public static final Block STRIPPED_POPLAR_LOG = Reg.block(
		"stripped_poplar_log",
		RotatedPillarBlock::new,
		Blocks.logProperties(POPLAR_PLANKS.defaultMapColor(), POPLAR_PLANKS.defaultMapColor(), SoundType.WOOD)
	);
	public static final Block POPLAR_WOOD = Reg.block(
		"poplar_wood",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()
	);
	public static final Block STRIPPED_POPLAR_WOOD = Reg.block(
		"stripped_poplar_wood",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F)
			.sound(SoundType.WOOD)
			.ignitedByLava()
	);
	public static final Block RED_POPLAR_LEAVES = leaves("red_poplar_leaves", WildParticles.RED_POPLAR_LEAVES, MapColor.COLOR_RED);
	public static final Block ORANGE_POPLAR_LEAVES = leaves("orange_poplar_leaves", WildParticles.ORANGE_POPLAR_LEAVES, MapColor.COLOR_ORANGE);
	public static final Block YELLOW_POPLAR_LEAVES = leaves("yellow_poplar_leaves", WildParticles.YELLOW_POPLAR_LEAVES, MapColor.COLOR_YELLOW);
	public static final Block POPLAR_SHELF = Reg.block(
		"poplar_shelf",
		ShelfBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.instrument(NoteBlockInstrument.BASS)
			.sound(CopperAgeSounds.SHELF_SOUND_TYPE)
			.ignitedByLava()
			.strength(2.0F, 3.0F)
	);
	public static final Block POPLAR_SIGN = Reg.block(
		"poplar_sign",
		properties -> new StandingSignBlock(POPLAR_WOOD_TYPE, properties),
		signProperties()
	);
	public static final Block POPLAR_WALL_SIGN = Reg.block(
		"poplar_wall_sign",
		properties -> new WallSignBlock(POPLAR_WOOD_TYPE, properties),
		wallVariant(POPLAR_SIGN, signProperties())
	);
	public static final Block POPLAR_HANGING_SIGN = Reg.block(
		"poplar_hanging_sign",
		properties -> new CeilingHangingSignBlock(POPLAR_WOOD_TYPE, properties),
		signProperties()
	);
	public static final Block POPLAR_WALL_HANGING_SIGN = Reg.block(
		"poplar_wall_hanging_sign",
		properties -> new WallHangingSignBlock(POPLAR_WOOD_TYPE, properties),
		wallVariant(POPLAR_HANGING_SIGN, signProperties())
	);
	public static final Block POPLAR_PRESSURE_PLATE = Reg.block(
		"poplar_pressure_plate",
		properties -> new PressurePlateBlock(POPLAR_SET, properties),
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollission()
			.strength(0.5F)
			.ignitedByLava()
			.pushReaction(PushReaction.DESTROY)
	);
	public static final Block POPLAR_TRAPDOOR = Reg.block(
		"poplar_trapdoor",
		properties -> new TrapDoorBlock(POPLAR_SET, properties),
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.instrument(NoteBlockInstrument.BASS)
			.strength(3.0F)
			.noOcclusion()
			.isValidSpawn(Blocks::never)
			.ignitedByLava()
	);
	public static final Block POTTED_POPLAR_SAPLING = Reg.block(
		"potted_poplar_sapling", properties -> new FlowerPotBlock(POPLAR_SAPLING, properties), Blocks.flowerPotProperties()
	);
	public static final Block POPLAR_BUTTON = Reg.block("poplar_button", properties -> new ButtonBlock(POPLAR_SET, 30, properties), Blocks.buttonProperties());
	public static final Block POPLAR_STAIRS = Reg.block(
		"poplar_stairs", properties -> new StairBlock(POPLAR_PLANKS.defaultBlockState(), properties), BlockBehaviour.Properties.ofLegacyCopy(POPLAR_PLANKS)
	);
	public static final Block POPLAR_SLAB = Reg.block("poplar_slab", SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(POPLAR_PLANKS));
	public static final Block POPLAR_FENCE_GATE = Reg.block(
		"poplar_fence_gate",
		properties -> new FenceGateBlock(POPLAR_WOOD_TYPE, properties),
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
			.ignitedByLava()
	);
	public static final Block POPLAR_FENCE = Reg.block(
		"poplar_fence",
		FenceBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.instrument(NoteBlockInstrument.BASS)
			.strength(2.0F, 3.0F)
			.ignitedByLava()
			.sound(SoundType.WOOD)
	);
	public static final Block POPLAR_DOOR = Reg.block(
		"poplar_door",
		properties -> new DoorBlock(POPLAR_SET, properties),
		BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.instrument(NoteBlockInstrument.BASS)
			.strength(3.0F)
			.noOcclusion()
			.ignitedByLava()
			.pushReaction(PushReaction.DESTROY)
	);

	public static final Block RED_SHRUB = Reg.block(
		"red_shrub",
		BushBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.CRIMSON_NYLIUM)
			.replaceable()
			.noCollission()
			.instabreak()
			.sound(WildSounds.RED_SHRUB)
			.ignitedByLava()
			.pushReaction(PushReaction.DESTROY)
	);
	public static final Block SHELF_MUSHROOM = Reg.block(
		"shelf_mushroom",
		ShelfMushroomBlock::new,
		BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).sound(WildSounds.SHELF_MUSHROOM).noOcclusion().pushReaction(PushReaction.DESTROY)
	);
	public static final Block STRAW_BED = Reg.block(
		"straw_bed",
		StrawBedBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_YELLOW)
			.sound(WildSounds.STRAW_BED)
			.strength(0.2F)
			.noOcclusion()
			.ignitedByLava()
			.pushReaction(PushReaction.DESTROY)
	);

	public static final Map<DyeColor, Block> WOOL_STAIRS = byColor(color -> stair(colored(color, "wool_stairs"), wool(color)));
	public static final Map<DyeColor, Block> WOOL_SLAB = byColor(color -> slab(colored(color, "wool_slab"), wool(color)));
	public static final Map<DyeColor, Block> CONCRETE_STAIRS = byColor(color -> stair(colored(color, "concrete_stairs"), concrete(color)));
	public static final Map<DyeColor, Block> CONCRETE_SLAB = byColor(color -> slab(colored(color, "concrete_slab"), concrete(color)));

	private WildBlocks() {
	}

	private static TreeGrower grower(String name, net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> tree) {
		return new TreeGrower(name, Optional.empty(), Optional.of(tree), Optional.empty());
	}

	private static Block leaves(String name, ParticleOptions particle, MapColor color) {
		return Reg.block(name, properties -> new PoplarLeavesBlock(0.01F, particle, properties), Blocks.leavesProperties(WildSounds.POPLAR_LEAVES).mapColor(color));
	}

	private static BlockBehaviour.Properties signProperties() {
		return BlockBehaviour.Properties.of()
			.mapColor(POPLAR_PLANKS.defaultMapColor())
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollission()
			.strength(1.0F)
			.ignitedByLava();
	}

	/** 1.21.7's private {@code Blocks.wallVariant}: wall signs share the standing sign's loot table and name. */
	private static BlockBehaviour.Properties wallVariant(Block standing, BlockBehaviour.Properties properties) {
		return properties.overrideLootTable(standing.getLootTable()).overrideDescription(standing.getDescriptionId());
	}

	private static String colored(DyeColor color, String suffix) {
		return color.getSerializedName() + "_" + suffix;
	}

	private static Map<DyeColor, Block> byColor(java.util.function.Function<DyeColor, Block> factory) {
		Map<DyeColor, Block> blocks = new EnumMap<>(DyeColor.class);
		for (DyeColor color : DyeColor.values()) {
			blocks.put(color, factory.apply(color));
		}

		return blocks;
	}

	private static Block stair(String name, Block base) {
		return Reg.block(name, properties -> new StairBlock(base.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(base));
	}

	private static Block slab(String name, Block base) {
		return Reg.block(name, SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(base));
	}

	public static Block wool(DyeColor color) {
		return switch (color) {
			case WHITE -> Blocks.WHITE_WOOL;
			case ORANGE -> Blocks.ORANGE_WOOL;
			case MAGENTA -> Blocks.MAGENTA_WOOL;
			case LIGHT_BLUE -> Blocks.LIGHT_BLUE_WOOL;
			case YELLOW -> Blocks.YELLOW_WOOL;
			case LIME -> Blocks.LIME_WOOL;
			case PINK -> Blocks.PINK_WOOL;
			case GRAY -> Blocks.GRAY_WOOL;
			case LIGHT_GRAY -> Blocks.LIGHT_GRAY_WOOL;
			case CYAN -> Blocks.CYAN_WOOL;
			case PURPLE -> Blocks.PURPLE_WOOL;
			case BLUE -> Blocks.BLUE_WOOL;
			case BROWN -> Blocks.BROWN_WOOL;
			case GREEN -> Blocks.GREEN_WOOL;
			case RED -> Blocks.RED_WOOL;
			case BLACK -> Blocks.BLACK_WOOL;
		};
	}

	public static Block concrete(DyeColor color) {
		return switch (color) {
			case WHITE -> Blocks.WHITE_CONCRETE;
			case ORANGE -> Blocks.ORANGE_CONCRETE;
			case MAGENTA -> Blocks.MAGENTA_CONCRETE;
			case LIGHT_BLUE -> Blocks.LIGHT_BLUE_CONCRETE;
			case YELLOW -> Blocks.YELLOW_CONCRETE;
			case LIME -> Blocks.LIME_CONCRETE;
			case PINK -> Blocks.PINK_CONCRETE;
			case GRAY -> Blocks.GRAY_CONCRETE;
			case LIGHT_GRAY -> Blocks.LIGHT_GRAY_CONCRETE;
			case CYAN -> Blocks.CYAN_CONCRETE;
			case PURPLE -> Blocks.PURPLE_CONCRETE;
			case BLUE -> Blocks.BLUE_CONCRETE;
			case BROWN -> Blocks.BROWN_CONCRETE;
			case GREEN -> Blocks.GREEN_CONCRETE;
			case RED -> Blocks.RED_CONCRETE;
			case BLACK -> Blocks.BLACK_CONCRETE;
		};
	}

	static void init() {
	}
}
