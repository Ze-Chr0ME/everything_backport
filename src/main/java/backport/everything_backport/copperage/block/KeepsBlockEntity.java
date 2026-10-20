package backport.everything_backport.copperage.block;

import net.minecraft.world.level.block.state.BlockState;

/** 1.21.9's {@code Block#shouldChangedStateKeepBlockEntity}, applied by LevelChunkMixin. */
public interface KeepsBlockEntity {
	boolean shouldChangedStateKeepBlockEntity(BlockState newState);
}
