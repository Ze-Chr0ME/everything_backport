package backport.everything_backport.tinytakeover;

import backport.everything_backport.registry.Reg;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class TinyBlocks {
	public static final Block GOLDEN_DANDELION = Reg.block(
		"golden_dandelion",
		properties -> new FlowerBlock(MobEffects.SATURATION, 0.35F, properties),
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.PLANT)
			.noCollission()
			.instabreak()
			.sound(SoundType.GRASS)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.pushReaction(PushReaction.DESTROY)
	);
	public static final Block POTTED_GOLDEN_DANDELION = Reg.block(
		"potted_golden_dandelion", properties -> new FlowerPotBlock(GOLDEN_DANDELION, properties), Blocks.flowerPotProperties()
	);

	private TinyBlocks() {
	}

	static void init() {
	}
}
