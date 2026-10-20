package backport.everything_backport.tinytakeover;

import backport.everything_backport.registry.Reg;
import net.minecraft.world.item.Item;

public final class TinyItems {
	public static final Item GOLDEN_DANDELION = Reg.blockItem(TinyBlocks.GOLDEN_DANDELION);

	private TinyItems() {
	}

	static void init() {
	}
}
