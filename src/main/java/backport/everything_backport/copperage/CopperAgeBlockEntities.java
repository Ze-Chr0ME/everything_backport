package backport.everything_backport.copperage;

import backport.everything_backport.copperage.block.entity.CopperGolemStatueBlockEntity;
import backport.everything_backport.copperage.block.entity.ShelfBlockEntity;
import backport.everything_backport.registry.Reg;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class CopperAgeBlockEntities {
	public static final BlockEntityType<CopperGolemStatueBlockEntity> COPPER_GOLEM_STATUE = Reg.blockEntity(
		"copper_golem_statue", CopperGolemStatueBlockEntity::new, CopperAgeBlocks.COPPER_GOLEM_STATUES.toArray(Block[]::new)
	);
	public static final BlockEntityType<ShelfBlockEntity> SHELF = Reg.blockEntity("shelf", ShelfBlockEntity::new, CopperAgeBlocks.SHELVES.toArray(Block[]::new));

	private CopperAgeBlockEntities() {
	}

	static void init() {
		CopperAgeBlocks.COPPER_CHESTS.forEach(BlockEntityType.CHEST::addSupportedBlock);
	}
}
