package backport.everything_backport.tinytakeover;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/** Hitboxes 26.1 gives the redesigned babies, which are bigger or smaller than half an adult. */
public final class BabyDimensions {
	private static final Map<EntityType<?>, EntityDimensions> BY_TYPE = new HashMap<>();

	static {
		put(EntityType.AXOLOTL, EntityDimensions.scalable(0.5F, 0.25F).withEyeHeight(0.2F));
		put(EntityType.CHICKEN, EntityDimensions.scalable(0.3F, 0.4F).withEyeHeight(0.28F));
		put(EntityType.FOX, EntityType.FOX.getDimensions().scale(0.6F).withEyeHeight(0.2975F));
		put(EntityType.RABBIT, EntityDimensions.scalable(0.24F, 0.4F).withEyeHeight(0.39F));
		EntityDimensions squid = EntityDimensions.scalable(0.5F, 0.63F).withEyeHeight(0.37F);
		put(EntityType.SQUID, squid);
		put(EntityType.GLOW_SQUID, squid);
		put(EntityType.VILLAGER, EntityDimensions.scalable(0.49F, 0.99F).withEyeHeight(0.63F));
		put(EntityType.ZOMBIE, smallHumanoid(0.775F, 0.1875F));
		put(EntityType.DROWNED, smallHumanoid(0.775F, 0.1875F));
		put(EntityType.HUSK, smallHumanoid(0.825F, 0.1875F));
		put(EntityType.ZOMBIE_VILLAGER, smallHumanoid(0.67F, 0.125F));
		put(EntityType.PIGLIN, smallHumanoid(0.78F, 0.1875F));
		put(EntityType.ZOMBIFIED_PIGLIN, smallHumanoid(0.78F, 0.1875F));
		put(EntityType.HORSE, foal(EntityType.HORSE, EntityType.HORSE.getHeight() - 0.125F, 0.0F, 0.7F));
		put(EntityType.SKELETON_HORSE, foal(EntityType.SKELETON_HORSE, EntityType.SKELETON_HORSE.getHeight() - 0.25F, 0.0F, 0.7F));
		put(EntityType.ZOMBIE_HORSE, foal(EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_HORSE.getHeight() - 0.25F, 0.0F, 0.7F));
		put(EntityType.DONKEY, foal(EntityType.DONKEY, EntityType.DONKEY.getHeight() + 0.03125F, -0.3125F, 0.5F));
		put(EntityType.MULE, foal(EntityType.MULE, EntityType.MULE.getHeight() + 0.03125F, -0.3125F, 0.5F));
		EntityDimensions llama = foal(EntityType.LLAMA, EntityType.LLAMA.getHeight() - 0.25F, -0.3F, 0.5F);
		put(EntityType.LLAMA, llama);
		put(EntityType.TRADER_LLAMA, llama);
	}

	private BabyDimensions() {
	}

	private static void put(EntityType<?> type, EntityDimensions dimensions) {
		BY_TYPE.put(type, dimensions);
	}

	private static EntityDimensions smallHumanoid(float eyeHeight, float vehicleY) {
		return EntityDimensions.scalable(0.49F, 0.99F)
			.withEyeHeight(eyeHeight)
			.withAttachments(EntityAttachments.builder().attach(EntityAttachment.VEHICLE, 0.0F, vehicleY, 0.0F));
	}

	private static EntityDimensions foal(EntityType<?> type, float passengerY, float passengerZ, float scale) {
		return type.getDimensions()
			.withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, passengerY, passengerZ))
			.scale(scale);
	}

	public static @Nullable EntityDimensions forBaby(EntityType<?> type) {
		return BY_TYPE.get(type);
	}
}
