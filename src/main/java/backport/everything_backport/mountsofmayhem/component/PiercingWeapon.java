package backport.everything_backport.mountsofmayhem.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import backport.everything_backport.mountsofmayhem.SpearCombat;

public record PiercingWeapon(boolean dealsKnockback, boolean dismounts, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) {
	public static final Codec<PiercingWeapon> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				Codec.BOOL.optionalFieldOf("deals_knockback", true).forGetter(PiercingWeapon::dealsKnockback),
				Codec.BOOL.optionalFieldOf("dismounts", false).forGetter(PiercingWeapon::dismounts),
				SoundEvent.CODEC.optionalFieldOf("sound").forGetter(PiercingWeapon::sound),
				SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(PiercingWeapon::hitSound)
			)
			.apply(instance, PiercingWeapon::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, PiercingWeapon> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL,
		PiercingWeapon::dealsKnockback,
		ByteBufCodecs.BOOL,
		PiercingWeapon::dismounts,
		SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional),
		PiercingWeapon::sound,
		SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional),
		PiercingWeapon::hitSound,
		PiercingWeapon::new
	);

	public void makeSound(Entity entity) {
		this.sound.ifPresent(holder -> entity.level().playSound(entity, entity.getX(), entity.getY(), entity.getZ(), holder, entity.getSoundSource(), 1.0F, 1.0F));
	}

	public void makeHitSound(Entity entity) {
		this.hitSound.ifPresent(holder -> entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), holder, entity.getSoundSource(), 1.0F, 1.0F));
	}

	public static boolean canHitEntity(Entity entity, Entity entity2) {
		if (entity2.isInvulnerable() || !entity2.isAlive()) {
			return false;
		} else if (entity2 instanceof Interaction) {
			return true;
		} else if (!entity2.canBeHitByProjectile()) {
			return false;
		} else {
			return entity2 instanceof Player player && entity instanceof Player player2 && !player2.canHarmPlayer(player)
				? false
				: !entity.isPassengerOfSameVehicle(entity2);
		}
	}

	public void attack(LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
		float f = (float)livingEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
		AttackRange attackRange = SpearCombat.attackRange(livingEntity);
		boolean bl = false;

		for (EntityHitResult entityHitResult : SpearCombat.getHitEntitiesAlong(
				livingEntity, attackRange, entity -> canHitEntity(livingEntity, entity), ClipContext.Block.COLLIDER
			).map(blockHitResult -> List.<EntityHitResult>of(), collection -> collection)) {
			bl |= SpearCombat.stabAttack(livingEntity, equipmentSlot, entityHitResult.getEntity(), f, true, this.dealsKnockback, this.dismounts);
		}

		SpearCombat.onAttack(livingEntity);
		SpearCombat.lungeForwardMaybe(livingEntity);
		if (bl) {
			this.makeHitSound(livingEntity);
		}

		this.makeSound(livingEntity);
		livingEntity.swing(InteractionHand.MAIN_HAND, false);
	}
}
