package backport.everything_backport.mountsofmayhem.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;

/** 1.21.11's {@code Mob#chargeSpeedModifier}: how much faster a mount carries a spear rider into a charge. */
public interface MountedCharger {
	float chargeSpeedModifier();

	static float of(Entity entity) {
		if (entity instanceof MountedCharger charger) {
			return charger.chargeSpeedModifier();
		} else {
			return entity instanceof ZombieHorse ? 1.4F : 1.0F;
		}
	}
}
