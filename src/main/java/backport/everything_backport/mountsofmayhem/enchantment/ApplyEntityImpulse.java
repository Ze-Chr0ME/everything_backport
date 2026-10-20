package backport.everything_backport.mountsofmayhem.enchantment;

import backport.everything_backport.mixin.PlayerAccessor;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ApplyEntityImpulse(Vec3 direction, Vec3 coordinateScale, LevelBasedValue magnitude) implements EnchantmentEntityEffect {
	public static final MapCodec<ApplyEntityImpulse> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Vec3.CODEC.fieldOf("direction").forGetter(ApplyEntityImpulse::direction),
				Vec3.CODEC.fieldOf("coordinate_scale").forGetter(ApplyEntityImpulse::coordinateScale),
				LevelBasedValue.CODEC.fieldOf("magnitude").forGetter(ApplyEntityImpulse::magnitude)
			)
			.apply(instance, ApplyEntityImpulse::new)
	);
	private static final int POST_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS = 10;

	@Override
	public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse item, Entity entity, Vec3 origin) {
		Vec3 impulse = addLocalCoordinates(entity.getLookAngle(), this.direction).multiply(this.coordinateScale).scale(this.magnitude.calculate(level));
		entity.addDeltaMovement(impulse);
		entity.hurtMarked = true;
		if (entity instanceof Player player) {
			PlayerAccessor accessor = (PlayerAccessor)player;
			accessor.everythingBackport$setImpulseGraceTime(Math.max(accessor.everythingBackport$getImpulseGraceTime(), POST_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS));
		}
	}

	@Override
	public MapCodec<ApplyEntityImpulse> codec() {
		return CODEC;
	}

	/** 1.21.11's {@code Vec3#addLocalCoordinates}: {@code local} expressed relative to the direction of {@code look}. */
	private static Vec3 addLocalCoordinates(Vec3 look, Vec3 local) {
		float yRot = (float)Math.atan2(-look.x, look.z) * (180.0F / (float)Math.PI);
		float xRot = (float)Math.asin(-look.y / Math.sqrt(look.x * look.x + look.y * look.y + look.z * look.z)) * (180.0F / (float)Math.PI);
		float f = Mth.cos((yRot + 90.0F) * (float) (Math.PI / 180.0));
		float g = Mth.sin((yRot + 90.0F) * (float) (Math.PI / 180.0));
		float h = Mth.cos(-xRot * (float) (Math.PI / 180.0));
		float i = Mth.sin(-xRot * (float) (Math.PI / 180.0));
		float j = Mth.cos((-xRot + 90.0F) * (float) (Math.PI / 180.0));
		float k = Mth.sin((-xRot + 90.0F) * (float) (Math.PI / 180.0));
		Vec3 forward = new Vec3(f * h, i, g * h);
		Vec3 up = new Vec3(f * j, k, g * j);
		Vec3 left = forward.cross(up).scale(-1.0);
		double x = forward.x * local.z + up.x * local.y + left.x * local.x;
		double y = forward.y * local.z + up.y * local.y + left.y * local.x;
		double z = forward.z * local.z + up.z * local.y + left.z * local.x;
		return new Vec3(x, y, z);
	}
}
