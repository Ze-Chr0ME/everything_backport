package backport.everything_backport.mountsofmayhem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 1.21.11's {@code Mob#burnUndead}, which burns every mob in the {@code burn_in_daylight} tag. Vanilla 1.21.7 mobs keep
 * their own burning code, so only the mobs new to burning (zombie horses and zombie nautiluses) call this.
 */
public final class Undead {
	private Undead() {
	}

	/** Sets the mob on fire in sunlight unless the item in {@code protectionSlot} shields it, wearing that item down. */
	public static void burnInDaylight(Mob mob, EquipmentSlot protectionSlot) {
		if (mob.isAlive() && isSunBurnTick(mob)) {
			ItemStack itemStack = mob.getItemBySlot(protectionSlot);
			if (!itemStack.isEmpty()) {
				if (itemStack.isDamageableItem()) {
					Item item = itemStack.getItem();
					itemStack.setDamageValue(itemStack.getDamageValue() + mob.getRandom().nextInt(2));
					if (itemStack.getDamageValue() >= itemStack.getMaxDamage()) {
						mob.onEquippedItemBroken(item, protectionSlot);
						mob.setItemSlot(protectionSlot, ItemStack.EMPTY);
					}
				}
			} else {
				mob.igniteForSeconds(8.0F);
			}
		}
	}

	/** Same check as 1.21.7's protected {@code Mob#isSunBurnTick}. */
	private static boolean isSunBurnTick(Mob mob) {
		if (mob.level().isBrightOutside() && !mob.level().isClientSide()) {
			float brightness = mob.getLightLevelDependentMagicValue();
			BlockPos eyePos = BlockPos.containing(mob.getX(), mob.getEyeY(), mob.getZ());
			boolean wet = mob.isInWaterOrRain() || mob.isInPowderSnow || mob.wasInPowderSnow;
			return brightness > 0.5F && mob.getRandom().nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F && !wet && mob.level().canSeeSky(eyePos);
		}

		return false;
	}
}
