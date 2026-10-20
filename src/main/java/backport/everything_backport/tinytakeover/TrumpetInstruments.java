package backport.everything_backport.tinytakeover;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.jetbrains.annotations.Nullable;

/**
 * Copper blocks whose properties 26.1 copies from a copper block that plays a trumpet. 1.21.7 gives them the default
 * harp, so the instrument is swapped when a block state is asked for it.
 */
public final class TrumpetInstruments {
	public static final NoteBlockInstrument TRUMPET = NoteBlockInstrument.valueOf("TRUMPET");
	public static final NoteBlockInstrument TRUMPET_EXPOSED = NoteBlockInstrument.valueOf("TRUMPET_EXPOSED");
	public static final NoteBlockInstrument TRUMPET_WEATHERED = NoteBlockInstrument.valueOf("TRUMPET_WEATHERED");
	public static final NoteBlockInstrument TRUMPET_OXIDIZED = NoteBlockInstrument.valueOf("TRUMPET_OXIDIZED");

	private static final Map<Block, NoteBlockInstrument> BY_BLOCK = new IdentityHashMap<>();

	static {
		put(TRUMPET, List.of(
			Blocks.COPPER_BLOCK, Blocks.CUT_COPPER, Blocks.CHISELED_COPPER, Blocks.CUT_COPPER_STAIRS, Blocks.CUT_COPPER_SLAB,
			Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_CHISELED_COPPER, Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_CUT_COPPER_SLAB
		));
		put(TRUMPET_EXPOSED, List.of(
			Blocks.EXPOSED_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.EXPOSED_CHISELED_COPPER, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_SLAB,
			Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS,
			Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB
		));
		put(TRUMPET_WEATHERED, List.of(
			Blocks.WEATHERED_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.WEATHERED_CHISELED_COPPER, Blocks.WEATHERED_CUT_COPPER_STAIRS,
			Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_WEATHERED_CHISELED_COPPER,
			Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB
		));
		put(TRUMPET_OXIDIZED, List.of(
			Blocks.OXIDIZED_COPPER, Blocks.OXIDIZED_CUT_COPPER, Blocks.OXIDIZED_CHISELED_COPPER, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
			Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CHISELED_COPPER,
			Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
		));
	}

	private TrumpetInstruments() {
	}

	private static void put(NoteBlockInstrument instrument, List<Block> blocks) {
		for (Block block : blocks) {
			BY_BLOCK.put(block, instrument);
		}
	}

	public static @Nullable NoteBlockInstrument forBlock(Block block) {
		return BY_BLOCK.get(block);
	}
}
