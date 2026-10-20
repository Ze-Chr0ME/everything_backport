package backport.everything_backport.tinytakeover;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** The golden dandelion's age lock, as {@code AgeableMob} does it in 26.1. */
public final class AgeLocking {
	public static final int AGE_LOCK_COOLDOWN_TICKS = 40;
	public static final float AGE_LOCK_DOWNWARDS_MOVING_PARTICLE_Y_OFFSET = 0.2F;
	public static final int BABY_START_AGE = -24000;
	public static final int SNIFFER_BABY_START_AGE = -48000;

	private AgeLocking() {
	}

	public static boolean canUseGoldenDandelion(ItemStack itemInHand, boolean isBaby, int cooldown, Mob mob) {
		return itemInHand.is(TinyItems.GOLDEN_DANDELION) && isBaby && cooldown == 0 && !mob.getType().is(TinyTags.CANNOT_BE_AGE_LOCKED);
	}

	public static void setAgeLocked(Mob mob, AgeLockable lockable, Player player, ItemStack itemInHand) {
		lockable.everythingBackport$toggleAgeLock();
		itemInHand.consume(1, player);
		boolean locked = lockable.everythingBackport$isAgeLocked();
		if (locked) {
			mob.setPersistenceRequired();
		}

		mob.level().playSound(null, mob.blockPosition(), locked ? TinySounds.GOLDEN_DANDELION_USE : TinySounds.GOLDEN_DANDELION_UNUSE, SoundSource.PLAYERS, 1.0F, 1.0F);
	}

	public static int makeAgeLockedParticle(Level level, Mob mob, int timer, boolean locked) {
		if (timer > 0) {
			if (level.isClientSide && timer % 2 == 0) {
				float yOffset = locked ? AGE_LOCK_DOWNWARDS_MOVING_PARTICLE_Y_OFFSET : 0.0F;
				Vec3 pos = new Vec3(mob.getRandomX(1.0), mob.getY((2.0 * mob.getRandom().nextDouble() - 1.0) * 0.2) + mob.getBbHeight() + yOffset, mob.getRandomZ(1.0));
				level.addParticle(locked ? TinyParticles.PAUSE_MOB_GROWTH : TinyParticles.RESET_MOB_GROWTH, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
			}

			timer--;
		}

		return timer;
	}

	public static boolean isAgeLocked(Object mob) {
		return mob instanceof AgeLockable lockable && lockable.everythingBackport$isAgeLocked();
	}

	/** 26.1's {@code AgeableMob.canAgeUp}: undead horse foals never grow up and locked babies stay young. */
	public static boolean canAgeUp(AgeableMob mob) {
		if (mob instanceof ZombieHorse || mob instanceof SkeletonHorse) {
			return false;
		}

		return mob.isBaby() && !isAgeLocked(mob);
	}

	public static int babyStartAge(AgeableMob mob) {
		return mob instanceof Sniffer ? SNIFFER_BABY_START_AGE : BABY_START_AGE;
	}
}
