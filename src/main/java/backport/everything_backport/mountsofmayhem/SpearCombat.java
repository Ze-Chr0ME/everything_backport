package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.mixin.LivingEntityInvoker;
import backport.everything_backport.mixin.MobInvoker;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import backport.everything_backport.mountsofmayhem.enchantment.MountsEnchantments;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** Spear combat helpers from 1.21.11's {@code LivingEntity}, {@code Entity} and {@code ProjectileUtil}. */
public final class SpearCombat {
	private SpearCombat() {
	}

	public static Vec3 getKnownSpeed(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			Entity vehicle = serverPlayer.getVehicle();
			return vehicle != null && vehicle.getControllingPassenger() != serverPlayer ? getKnownSpeed(vehicle) : serverPlayer.getKnownMovement();
		} else if (entity.getControllingPassenger() instanceof Player player && entity.isAlive()) {
			return getKnownSpeed(player);
		} else {
			return ((KnownSpeed)entity).everythingBackport$getLastKnownSpeed();
		}
	}

	public static Vec3 getHeadLookAngle(Entity entity) {
		return Vec3.directionFromRotation(entity.getXRot(), entity.getYHeadRot());
	}

	public static ItemStack getActiveItem(LivingEntity entity) {
		return entity.isUsingItem() ? entity.getUseItem() : entity.getMainHandItem();
	}

	public static AttackRange attackRange(LivingEntity entity) {
		AttackRange attackRange = getActiveItem(entity).get(MountsComponents.ATTACK_RANGE);
		return attackRange != null ? attackRange : AttackRange.defaultFor(entity);
	}

	public static boolean isWithinAttackRange(Player player, AABB box, double tolerance) {
		return attackRange(player).isInRange(player, box, tolerance);
	}

	public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
		Entity entity, AttackRange attackRange, Predicate<Entity> predicate, ClipContext.Block block
	) {
		Vec3 look = getHeadLookAngle(entity);
		Vec3 eye = entity.getEyePosition();
		Vec3 from = eye.add(look.scale(attackRange.effectiveMinRange(entity)));
		double forwardSpeed = entity.getKnownMovement().dot(look);
		Vec3 to = eye.add(look.scale(attackRange.effectiveMaxRange(entity) + Math.max(0.0, forwardSpeed)));
		return getHitEntitiesAlong(entity, eye, from, predicate, to, attackRange.hitboxMargin(), block);
	}

	private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
		Entity entity, Vec3 eye, Vec3 from, Predicate<Entity> predicate, Vec3 to, float margin, ClipContext.Block block
	) {
		Level level = entity.level();
		BlockHitResult blockHitResult = level.clipIncludingBorder(new ClipContext(eye, to, block, ClipContext.Fluid.NONE, entity));
		if (blockHitResult.getType() != HitResult.Type.MISS) {
			to = blockHitResult.getLocation();
			if (eye.distanceToSqr(to) < eye.distanceToSqr(from)) {
				return Either.left(blockHitResult);
			}
		}

		AABB area = AABB.ofSize(from, margin, margin, margin).expandTowards(to.subtract(from)).inflate(1.0);
		Collection<EntityHitResult> hits = getManyEntityHitResult(level, entity, from, to, area, predicate, margin, block, true);
		return !hits.isEmpty() ? Either.right(hits) : Either.left(blockHitResult);
	}

	public static Collection<EntityHitResult> getManyEntityHitResult(
		Level level, Entity entity, Vec3 from, Vec3 to, AABB area, Predicate<Entity> predicate, float margin, ClipContext.Block block, boolean includeInside
	) {
		List<EntityHitResult> list = new ArrayList<>();

		for (Entity target : level.getEntities(entity, area, predicate)) {
			AABB box = target.getBoundingBox();
			if (includeInside && box.contains(from)) {
				list.add(new EntityHitResult(target, from));
			} else {
				Optional<Vec3> hit = box.clip(from, to);
				if (hit.isPresent()) {
					list.add(new EntityHitResult(target, hit.get()));
				} else if (!(margin <= 0.0)) {
					Optional<Vec3> marginHit = box.inflate(margin).clip(from, to);
					if (marginHit.isPresent()) {
						Vec3 start = marginHit.get();
						Vec3 center = box.getCenter();
						BlockHitResult blockHit = level.clipIncludingBorder(new ClipContext(start, center, block, ClipContext.Fluid.NONE, entity));
						if (blockHit.getType() != HitResult.Type.MISS) {
							center = blockHit.getLocation();
						}

						Optional<Vec3> inner = target.getBoundingBox().clip(start, center);
						inner.ifPresent(vec3 -> list.add(new EntityHitResult(target, vec3)));
					}
				}
			}
		}

		return list;
	}

	public static boolean wasRecentlyStabbed(LivingEntity attacker, Entity entity, int cooldownTicks) {
		return ((StabTracker)attacker).everythingBackport$wasRecentlyStabbed(entity, cooldownTicks);
	}

	public static void rememberStabbedEntity(LivingEntity attacker, Entity entity) {
		((StabTracker)attacker).everythingBackport$rememberStabbedEntity(entity);
	}

	public static int stabbedEntities(LivingEntity attacker, Predicate<Entity> predicate) {
		return ((StabTracker)attacker).everythingBackport$stabbedEntities(predicate);
	}

	/** The item's {@code damage_type} component, else the item's own damage source, else the fallback. */
	public static DamageSource getDamageSource(ItemStack itemStack, LivingEntity attacker, Supplier<DamageSource> fallback) {
		var damageType = itemStack.get(MountsComponents.DAMAGE_TYPE);
		if (damageType != null) {
			Optional<Holder.Reference<DamageType>> holder = attacker.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).get(damageType);
			if (holder.isPresent()) {
				return new DamageSource(holder.get(), attacker);
			}
		}

		DamageSource itemSource = itemStack.getItem().getDamageSource(attacker);
		return itemSource != null ? itemSource : fallback.get();
	}

	public static boolean stabAttack(
		LivingEntity attacker, EquipmentSlot slot, Entity entity, float damage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts
	) {
		if (!(attacker.level() instanceof ServerLevel serverLevel)) {
			return false;
		} else {
			ItemStack itemStack = attacker.getItemBySlot(slot);
			DamageSource damageSource = getDamageSource(itemStack, attacker, () -> attacker.damageSources().mobAttack(attacker));
			float modified = EnchantmentHelper.modifyDamage(serverLevel, itemStack, entity, damageSource, damage);
			Vec3 targetMovement = entity.getDeltaMovement();
			boolean hurt = dealsDamage && entity.hurtServer(serverLevel, damageSource, modified);
			boolean affected = dealsKnockback | hurt;
			if (dealsKnockback) {
				causeExtraKnockback(attacker, entity, 0.4F + ((LivingEntityInvoker)attacker).everythingBackport$getKnockback(entity, damageSource), targetMovement);
			}

			if (dismounts && entity.isPassenger()) {
				affected = true;
				entity.stopRiding();
			}

			if (entity instanceof LivingEntity livingEntity) {
				itemStack.hurtEnemy(livingEntity, attacker);
			}

			if (hurt) {
				EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
			}

			if (!affected) {
				return false;
			} else {
				attacker.setLastHurtMob(entity);
				// 1.21.11 moved playAttackSound up to LivingEntity; in 1.21.7 only mobs have one.
				if (attacker instanceof Mob mob) {
					((MobInvoker)mob).everythingBackport$playAttackSound();
				}
				return true;
			}
		}
	}

	public static void causeExtraKnockback(LivingEntity attacker, Entity entity, float strength, Vec3 targetMovement) {
		if (strength > 0.0F && entity instanceof LivingEntity livingEntity) {
			livingEntity.knockback(strength, Mth.sin(attacker.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(attacker.getYRot() * (float) (Math.PI / 180.0)));
			attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6, 1.0, 0.6));
		}
	}

	public static void onAttack(LivingEntity attacker) {
		if (attacker instanceof Player player) {
			((LivingEntityInvoker)player).everythingBackport$setAttackStrengthTicker(0);
		}
	}

	public static void lungeForwardMaybe(LivingEntity attacker) {
		if (attacker.level() instanceof ServerLevel serverLevel) {
			MountsEnchantments.doLungeEffects(serverLevel, attacker);
		}
	}

	/** 1.21.11's {@code Player#cannotAttackWithItem}: the attack charge needed by {@code minimum_attack_charge}. */
	public static boolean cannotAttackWithItem(Player player, ItemStack itemStack, int ticks) {
		float minimum = itemStack.getOrDefault(MountsComponents.MINIMUM_ATTACK_CHARGE, 0.0F);
		float charge = (((LivingEntityInvoker)player).everythingBackport$getAttackStrengthTicker() + ticks) / player.getCurrentItemAttackStrengthDelay();
		return minimum > 0.0F && charge < minimum;
	}

	/** Added to {@code Entity} by EntityMixin: the per-tick position change used by kinetic weapons. */
	public interface KnownSpeed {
		Vec3 everythingBackport$getLastKnownSpeed();
	}

	/** Added to {@code LivingEntity} by LivingEntityMixin. */
	public interface StabTracker {
		boolean everythingBackport$wasRecentlyStabbed(Entity entity, int cooldownTicks);

		void everythingBackport$rememberStabbedEntity(Entity entity);

		int everythingBackport$stabbedEntities(Predicate<Entity> predicate);

		float everythingBackport$getTicksSinceLastKineticHitFeedback(float partialTick);
	}
}
