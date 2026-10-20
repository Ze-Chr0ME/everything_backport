package backport.everything_backport.wildernessbound.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 26.3's poplar sapling. Its tree grower picks red, orange or yellow poplars with equal weight, which 1.21.7's {@link TreeGrower}
 * (one tree and one secondary tree) can't express, so the sapling picks one of three growers itself.
 */
public class PoplarSaplingBlock extends SaplingBlock {
	private final List<TreeGrower> growers;

	public PoplarSaplingBlock(List<TreeGrower> growers, BlockBehaviour.Properties properties) {
		super(growers.getFirst(), properties);
		this.growers = growers;
	}

	@Override
	public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
		if (state.getValue(STAGE) == 0) {
			level.setBlock(pos, state.cycle(STAGE), 260);
		} else {
			this.growers.get(random.nextInt(this.growers.size())).growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
		}
	}
}
