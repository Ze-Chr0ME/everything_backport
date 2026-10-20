package backport.everything_backport.chaoscubed;

import backport.everything_backport.chaoscubed.block.PotentSulfurBlock;
import backport.everything_backport.chaoscubed.block.SulfurSpikeBlock;
import backport.everything_backport.registry.Reg;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ChaosBlocks {
	public static final Block SULFUR = Reg.block(
		"sulfur",
		BlockBehaviour.Properties.of()
			.sound(ChaosSounds.SULFUR_SOUND)
			.mapColor(MapColor.COLOR_YELLOW)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
			.strength(1.5F, 6.0F)
	);
	public static final Block POTENT_SULFUR = Reg.block(
		"potent_sulfur", PotentSulfurBlock::new, BlockBehaviour.Properties.ofFullCopy(SULFUR).sound(ChaosSounds.POTENT_SULFUR_SOUND).mapColor(MapColor.GOLD)
	);
	public static final Block SULFUR_SLAB = slab("sulfur_slab", SULFUR);
	public static final Block SULFUR_STAIRS = stair("sulfur_stairs", SULFUR);
	public static final Block SULFUR_WALL = wall("sulfur_wall", SULFUR);
	public static final Block POLISHED_SULFUR = Reg.block("polished_sulfur", BlockBehaviour.Properties.ofLegacyCopy(SULFUR));
	public static final Block POLISHED_SULFUR_SLAB = slab("polished_sulfur_slab", POLISHED_SULFUR);
	public static final Block POLISHED_SULFUR_STAIRS = stair("polished_sulfur_stairs", POLISHED_SULFUR);
	public static final Block POLISHED_SULFUR_WALL = wall("polished_sulfur_wall", POLISHED_SULFUR);
	public static final Block SULFUR_BRICKS = Reg.block("sulfur_bricks", BlockBehaviour.Properties.ofLegacyCopy(SULFUR));
	public static final Block SULFUR_BRICK_SLAB = slab("sulfur_brick_slab", SULFUR_BRICKS);
	public static final Block SULFUR_BRICK_STAIRS = stair("sulfur_brick_stairs", SULFUR_BRICKS);
	public static final Block SULFUR_BRICK_WALL = wall("sulfur_brick_wall", SULFUR_BRICKS);
	public static final Block CHISELED_SULFUR = Reg.block("chiseled_sulfur", BlockBehaviour.Properties.ofLegacyCopy(SULFUR));
	public static final Block CINNABAR = Reg.block(
		"cinnabar",
		BlockBehaviour.Properties.of()
			.sound(ChaosSounds.CINNABAR_SOUND)
			.mapColor(MapColor.COLOR_RED)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
			.strength(1.5F, 6.0F)
	);
	public static final Block CINNABAR_SLAB = slab("cinnabar_slab", CINNABAR);
	public static final Block CINNABAR_STAIRS = stair("cinnabar_stairs", CINNABAR);
	public static final Block CINNABAR_WALL = wall("cinnabar_wall", CINNABAR);
	public static final Block POLISHED_CINNABAR = Reg.block("polished_cinnabar", BlockBehaviour.Properties.ofLegacyCopy(CINNABAR));
	public static final Block POLISHED_CINNABAR_SLAB = slab("polished_cinnabar_slab", POLISHED_CINNABAR);
	public static final Block POLISHED_CINNABAR_STAIRS = stair("polished_cinnabar_stairs", POLISHED_CINNABAR);
	public static final Block POLISHED_CINNABAR_WALL = wall("polished_cinnabar_wall", POLISHED_CINNABAR);
	public static final Block CINNABAR_BRICKS = Reg.block("cinnabar_bricks", BlockBehaviour.Properties.ofLegacyCopy(CINNABAR));
	public static final Block CINNABAR_BRICK_SLAB = slab("cinnabar_brick_slab", CINNABAR_BRICKS);
	public static final Block CINNABAR_BRICK_STAIRS = stair("cinnabar_brick_stairs", CINNABAR_BRICKS);
	public static final Block CINNABAR_BRICK_WALL = wall("cinnabar_brick_wall", CINNABAR_BRICKS);
	public static final Block CHISELED_CINNABAR = Reg.block("chiseled_cinnabar", BlockBehaviour.Properties.ofLegacyCopy(CINNABAR));
	public static final Block SULFUR_SPIKE = Reg.block(
		"sulfur_spike",
		properties -> new SulfurSpikeBlock(SULFUR.defaultBlockState(), properties),
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_YELLOW)
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASEDRUM)
			.noOcclusion()
			.sound(ChaosSounds.SULFUR_SPIKE_SOUND)
			.randomTicks()
			.strength(1.5F, 3.0F)
			.dynamicShape()
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.pushReaction(PushReaction.DESTROY)
			.isRedstoneConductor(Blocks::never)
	);

	private ChaosBlocks() {
	}

	private static Block stair(String name, Block base) {
		return Reg.block(name, properties -> new StairBlock(base.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(base));
	}

	private static Block slab(String name, Block base) {
		return Reg.block(name, SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(base));
	}

	private static Block wall(String name, Block base) {
		return Reg.block(name, WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(base).forceSolidOn());
	}

	static void init() {
	}
}
