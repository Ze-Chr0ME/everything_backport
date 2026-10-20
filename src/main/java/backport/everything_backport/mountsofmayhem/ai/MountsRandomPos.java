package backport.everything_backport.mountsofmayhem.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** 1.21.11's {@code LandRandomPos#getPosAway} overload with a minimum distance, used by spear-wielding mobs to back off. */
public final class MountsRandomPos {
	private MountsRandomPos() {
	}

	@Nullable
	public static Vec3 getPosAway(PathfinderMob mob, double minDistance, double maxDistance, int maxYDistance, Vec3 awayFrom) {
		Vec3 direction = mob.position().subtract(awayFrom);
		if (direction.length() == 0.0) {
			direction = new Vec3(mob.getRandom().nextDouble() - 0.5, 0.0, mob.getRandom().nextDouble() - 0.5);
		}

		boolean restricted = mob.hasHome()
			&& mob.getHomePosition().closerToCenterThan(mob.position(), mob.getHomeRadius() + maxDistance + 1.0);
		Vec3 finalDirection = direction;
		return RandomPos.generateRandomPos(mob, () -> {
			BlockPos offset = generateRandomDirectionWithinRadians(
				mob.getRandom(), minDistance, maxDistance, maxYDistance, 0, finalDirection.x, finalDirection.z, (float)(Math.PI / 2)
			);
			if (offset == null) {
				return null;
			} else {
				BlockPos target = generateRandomPosTowardDirection(mob, maxDistance, restricted, offset);
				return target == null ? null : LandRandomPos.movePosUpOutOfSolid(mob, target);
			}
		});
	}

	@Nullable
	private static BlockPos generateRandomDirectionWithinRadians(
		RandomSource random, double minDistance, double maxDistance, int maxYDistance, int yOffset, double x, double z, double maxAngle
	) {
		double angle = Mth.atan2(z, x) - (float)(Math.PI / 2) + (2.0F * random.nextFloat() - 1.0F) * maxAngle;
		double distance = Mth.lerp(Math.sqrt(random.nextDouble()), minDistance, maxDistance) * Mth.SQRT_OF_TWO;
		double dx = -distance * Math.sin(angle);
		double dz = distance * Math.cos(angle);
		if (!(Math.abs(dx) > maxDistance) && !(Math.abs(dz) > maxDistance)) {
			int dy = random.nextInt(2 * maxYDistance + 1) - maxYDistance + yOffset;
			return BlockPos.containing(dx, dy, dz);
		} else {
			return null;
		}
	}

	@Nullable
	private static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, double distance, boolean restricted, BlockPos offset) {
		double x = offset.getX();
		double z = offset.getZ();
		RandomSource random = mob.getRandom();
		if (mob.hasHome() && distance > 1.0) {
			BlockPos home = mob.getHomePosition();
			if (mob.getX() > home.getX()) {
				x -= random.nextDouble() * distance / 2.0;
			} else {
				x += random.nextDouble() * distance / 2.0;
			}

			if (mob.getZ() > home.getZ()) {
				z -= random.nextDouble() * distance / 2.0;
			} else {
				z += random.nextDouble() * distance / 2.0;
			}
		}

		BlockPos target = BlockPos.containing(x + mob.getX(), offset.getY() + mob.getY(), z + mob.getZ());
		return !GoalUtils.isOutsideLimits(target, mob) && !GoalUtils.isRestricted(restricted, mob, target) && !GoalUtils.isNotStable(mob.getNavigation(), target)
			? target
			: null;
	}
}
