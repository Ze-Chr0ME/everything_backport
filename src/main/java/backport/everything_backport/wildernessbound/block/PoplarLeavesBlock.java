package backport.everything_backport.wildernessbound.block;

import backport.everything_backport.wildernessbound.WildSounds;
import backport.everything_backport.wildernessbound.WildTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** 26.3's poplar leaves: untinted falling leaves that rustle now and then when they grow next to a log. */
public class PoplarLeavesBlock extends UntintedParticleLeavesBlock {
	// 26.3's AmbientLeavesBlockSoundPlayer defaults.
	private static final int AMBIENT_SOUND_CHANCE = 300;
	private static final int NEARBY_LOGS_REQUIRED = 1;
	private static final int NEARBY_SAME_LEAVES_REQUIRED = 3;

	public PoplarLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
		super(leafParticleChance, leafParticle, properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(AMBIENT_SOUND_CHANCE) == 0) {
			int nearbyLogs = 0;
			int nearbyLeaves = 0;

			for (Direction direction : Direction.Plane.HORIZONTAL) {
				BlockState neighbor = level.getBlockState(pos.relative(direction));
				if (neighbor.is(WildTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE)) {
					nearbyLogs++;
				}

				if (neighbor.is(this)) {
					nearbyLeaves++;
				}

				if (nearbyLogs == NEARBY_LOGS_REQUIRED && nearbyLeaves == NEARBY_SAME_LEAVES_REQUIRED) {
					level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), WildSounds.POPLAR_LEAVES_AMBIENT.value(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
					return;
				}
			}
		}
	}
}
