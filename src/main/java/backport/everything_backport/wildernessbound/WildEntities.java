package backport.everything_backport.wildernessbound;

import backport.everything_backport.registry.Reg;
import backport.everything_backport.wildernessbound.entity.Cushion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;

public final class WildEntities {
	public static final EntityType<Cushion> CUSHION = Reg.entity(
		"cushion",
		EntityType.Builder.<Cushion>of(Cushion::new, MobCategory.MISC)
			.noLootTable()
			.sized(1.0F, 0.25F)
			.clientTrackingRange(10)
			.updateInterval(Integer.MAX_VALUE)
	);
	public static final EntityType<Boat> POPLAR_BOAT = Reg.entity(
		"poplar_boat",
		EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, () -> WildItems.POPLAR_BOAT), MobCategory.MISC)
			.noLootTable()
			.sized(1.375F, 0.5625F)
			.eyeHeight(0.5625F)
			.clientTrackingRange(10)
	);
	public static final EntityType<ChestBoat> POPLAR_CHEST_BOAT = Reg.entity(
		"poplar_chest_boat",
		EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, () -> WildItems.POPLAR_CHEST_BOAT), MobCategory.MISC)
			.noLootTable()
			.sized(1.375F, 0.5625F)
			.eyeHeight(0.5625F)
			.clientTrackingRange(10)
	);

	private WildEntities() {
	}

	static void init() {
	}
}
