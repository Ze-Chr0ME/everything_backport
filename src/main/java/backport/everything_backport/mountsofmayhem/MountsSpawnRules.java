package backport.everything_backport.mountsofmayhem;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

/** 1.21.11's monster spawn rules, which accept any mob (zombie horses and camel husks aren't {@link Monster}s). */
public final class MountsSpawnRules {
	private MountsSpawnRules() {
	}

	public static boolean checkMonsterSpawnRules(
		EntityType<? extends Mob> entityType, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random
	) {
		return level.getDifficulty() != Difficulty.PEACEFUL
			&& (EntitySpawnReason.ignoresLightRequirements(spawnReason) || Monster.isDarkEnoughToSpawn(level, pos, random))
			&& Mob.checkMobSpawnRules(entityType, level, spawnReason, pos, random);
	}

	public static boolean checkSurfaceMonstersSpawnRules(
		EntityType<? extends Mob> entityType, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random
	) {
		return checkMonsterSpawnRules(entityType, level, spawnReason, pos, random) && (EntitySpawnReason.isSpawner(spawnReason) || level.canSeeSky(pos));
	}
}
