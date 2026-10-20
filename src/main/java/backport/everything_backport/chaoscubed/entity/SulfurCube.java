package backport.everything_backport.chaoscubed.entity;

import backport.everything_backport.chaoscubed.ChaosComponents;
import backport.everything_backport.chaoscubed.ChaosEntities;
import backport.everything_backport.chaoscubed.ChaosItems;
import backport.everything_backport.chaoscubed.ChaosParticles;
import backport.everything_backport.chaoscubed.ChaosRegistries;
import backport.everything_backport.chaoscubed.ChaosSounds;
import backport.everything_backport.chaoscubed.ChaosTags;
import backport.everything_backport.mixin.chaoscubed.PrimedTntAccessor;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.tinytakeover.AgeLockable;
import backport.everything_backport.tinytakeover.AgeLocking;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * 26.2's sulfur cube. It swallows a block item into its body slot; the archetype matching that item sets its attributes,
 * buoyancy, explosion, contact damage, knockback and sounds. Babies grow up when fed slime balls.
 */
public class SulfurCube extends AbstractCubeMob implements Bucketable, Shearable {
	public static final int MAX_SIZE = 2;
	public static final int PICKUP_TIMER_DURATION = 100;
	private int pickupTimer = 0;
	private int pushSoundCooldown = 0;
	private boolean floatsInLiquids = false;
	private Optional<SulfurCubeArchetype.ExplosionData> explosionData = Optional.empty();
	private SulfurCubeArchetype.KnockbackModifiers knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
	private SulfurCubeArchetype.SoundSettings soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;
	private int fuse = -1;
	private final List<SulfurCubeArchetype.ContactDamage> contactDamages = new ArrayList<>();
	/** The body item the archetype was last applied for; 26.2 does this in {@code collectEquipmentChanges}, which is private in 1.21.7. */
	private ItemStack appliedBodyItem = ItemStack.EMPTY;
	/** The hit being handled, so {@link #knockback} can scale by its damage as 26.2's knockback overload does. */
	private @Nullable DamageSource hurtSource;
	private float hurtDamage;
	private static final EntityDataAccessor<Integer> MAX_FUSE = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.BOOLEAN);
	private static final Predicate<ItemEntity> ALLOWED_ITEMS = e -> !e.hasPickUpDelay() && e.isAlive() && isSwallowableItem(e.getItem());

	public SulfurCube(EntityType<? extends SulfurCube> type, Level level) {
		super(type, level);
		this.lookControl = new SulfurCube.SulfurCubeLookControl();
		this.moveControl = new SulfurCube.SulfurCubeMobMoveControl(this);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(FROM_BUCKET, false);
		entityData.define(MAX_FUSE, -1);
	}

	@Override
	protected void addBehaviourGoals() {
		this.goalSelector
			.addGoal(2, new SulfurCube.SulfurCubeTemptGoal(this, 1.0, itemStack -> this.isBaby() ? itemStack.is(ChaosTags.SULFUR_CUBE_FOOD) : isSwallowableItem(itemStack), false, 1.0));
		this.goalSelector.addGoal(3, new SulfurCube.SulfurCubeSearchForItemsGoal(this));
	}

	@Override
	public boolean fromBucket() {
		return this.entityData.get(FROM_BUCKET);
	}

	public int getFuse() {
		return this.fuse;
	}

	public boolean isPrimed() {
		return this.getFuse() >= 0;
	}

	private void setFuse(int fuse) {
		this.fuse = fuse;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (MAX_FUSE.equals(accessor)) {
			this.setFuse(this.entityData.get(MAX_FUSE));
		}

		super.onSyncedDataUpdated(accessor);
	}

	@Override
	public void setFromBucket(boolean fromBucket) {
		this.entityData.set(FROM_BUCKET, fromBucket);
	}

	@Override
	public SoundEvent getPickupSound() {
		return ChaosSounds.BUCKET_FILL_SULFUR_CUBE;
	}

	@Override
	public void saveToBucketTag(ItemStack bucket) {
		Bucketable.saveDefaultDataToBucketTag(this, bucket);
		bucket.copyFrom(ChaosComponents.SULFUR_CUBE_CONTENT, this);
		CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
			tag.putInt("age", this.getAge());
			tag.putBoolean("age_locked", AgeLocking.isAgeLocked(this));
		});
	}

	@Override
	public boolean canBreatheUnderwater() {
		return this.hasBodyItem() || super.canBreatheUnderwater();
	}

	/** 26.2 lifts a cube holding a buoyant block after {@code travelInFluid}; 1.21.7 keeps that method private, so it happens here. */
	@Override
	public void travel(Vec3 input) {
		boolean inFluid = (this.isInWater() || this.isInLava()) && this.isAffectedByFluids();
		super.travel(input);
		if (inFluid && this.hasBodyItem() && this.floatsInLiquids) {
			float vibeAmount = 0.2F * Mth.sin(this.tickCount * 0.4F);
			double immersion = this.getFluidHeight(this.isInWater() ? FluidTags.WATER : FluidTags.LAVA) - this.getFluidJumpThreshold() + vibeAmount;
			if (immersion > 0.0) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, Math.min(1.0, immersion) * 0.04F, 0.0));
			}
		}
	}

	@Override
	public double getFluidJumpThreshold() {
		return this.getBbHeight() * 0.2;
	}

	@Override
	public void loadFromBucketTag(CompoundTag tag) {
		Bucketable.loadDefaultDataFromBucketTag(this, tag);
		this.setAge(tag.getIntOr("age", 0));
		if (this instanceof AgeLockable lockable) {
			lockable.everythingBackport$setAgeLocked(tag.getBooleanOr("age_locked", false));
		}
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(ChaosItems.SULFUR_CUBE_BUCKET);
	}

	@Override
	protected void addTargetingGoals() {
	}

	@Override
	public float getLightLevelDependentMagicValue() {
		return 1.0F;
	}

	@Override
	protected boolean isDealsDamage() {
		return false;
	}

	public static boolean checkSulfurCubeSpawnRules(
		EntityType<SulfurCube> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random
	) {
		return true;
	}

	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.hasBodyItem() || this.fromBucket();
	}

	@Override
	public boolean canBeLeashed() {
		return this.hasBodyItem();
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		if (this.hasBodyItem()) {
			if (this.canExplode() && !this.isPrimed()) {
				Entity sourceEntity = source.getDirectEntity();
				if (source.is(DamageTypeTags.IS_FIRE) || sourceEntity instanceof AbstractArrow projectile && projectile.isOnFire()) {
					this.primeTime(false);
				} else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
					this.primeTime(true);
				}
			}

			if (source.is(ChaosTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO)) {
				if (!source.is(DamageTypeTags.NO_KNOCKBACK)) {
					this.dealDefaultKnockback(source, damage);
				}

				return true;
			}
		}

		this.hurtSource = source;
		this.hurtDamage = damage;
		try {
			return super.hurtServer(level, source, damage);
		} finally {
			this.hurtSource = null;
		}
	}

	/** 26.2's {@code LivingEntity.dealDefaultKnockback}: the knockback a hit gives, without the hit. */
	private void dealDefaultKnockback(DamageSource source, float damage) {
		double xd = 0.0;
		double zd = 0.0;
		if (source.getDirectEntity() instanceof Projectile projectile) {
			DoubleDoubleImmutablePair knockbackDirection = projectile.calculateHorizontalHurtKnockbackDirection(this, source);
			xd = -knockbackDirection.leftDouble();
			zd = -knockbackDirection.rightDouble();
		} else if (source.getSourcePosition() != null) {
			xd = source.getSourcePosition().x() - this.getX();
			zd = source.getSourcePosition().z() - this.getZ();
		}

		this.hurtSource = source;
		this.hurtDamage = damage;
		try {
			this.knockback(0.4F, xd, zd);
		} finally {
			this.hurtSource = null;
		}

		this.indicateDamage(xd, zd);
	}

	public boolean hasBodyItem() {
		return !this.getItemBySlot(EquipmentSlot.BODY).isEmpty();
	}

	public boolean canExplode() {
		return this.explosionData.isPresent() && this.isAlive() && !this.isPrimed();
	}

	public List<SulfurCubeArchetype> matchingArchetypes(ItemStack stack) {
		return this.level()
			.registryAccess()
			.lookupOrThrow(ChaosRegistries.SULFUR_CUBE_ARCHETYPE)
			.stream()
			.filter(arch -> stack.is(arch.items()))
			.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void tick() {
		if (!this.level().isClientSide) {
			this.updateArchetype();
		}

		this.tickFuse();
		this.primeWhenOnPoweredPosition();
		super.tick();
	}

	private void tickFuse() {
		if (this.fuse > 0) {
			this.fuse--;
		}

		if (this.explosionData.isPresent() && this.fuse == 0) {
			this.dropLeash();
			this.dead = true;
			if (this.level() instanceof ServerLevel level) {
				if (level.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)) {
					Level.ExplosionInteraction explosionInteraction = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
						? Level.ExplosionInteraction.TNT
						: Level.ExplosionInteraction.NONE;
					level.explode(
						this,
						Explosion.getDefaultDamageSource(this.level(), this),
						this.getPortalCooldown() > 0 ? PrimedTntAccessor.everythingBackport$getUsedPortalDamageCalculator() : null,
						this.getX(),
						this.getY(0.0625),
						this.getZ(),
						this.explosionData.get().power(),
						this.explosionData.get().causesFire(),
						explosionInteraction
					);
				}

				this.triggerOnDeathMobEffects(level, Entity.RemovalReason.KILLED);
			}

			this.discard();
		}
	}

	private void primeWhenOnPoweredPosition() {
		if (this.level() instanceof ServerLevel level && this.canExplode()) {
			BlockPos here = BlockPos.containing(this.position());
			if (getBestOwnOrNeighbourSignal(level, here) != 0) {
				this.primeTime(false);
			}
		}
	}

	/** 26.2's {@code SignalGetter.getBestOwnOrNeighbourSignal}: a cube sitting in a powered pressure plate or rail counts too. */
	private static int getBestOwnOrNeighbourSignal(ServerLevel level, BlockPos pos) {
		int signal = level.getBestNeighborSignal(pos);
		BlockState state = level.getBlockState(pos);
		if (state.isSignalSource()) {
			for (Direction direction : Direction.values()) {
				signal = Math.max(signal, state.getSignal(level, pos, direction));
			}
		}

		return signal;
	}

	public boolean primeTime(boolean imminent) {
		if (this.explosionData.isPresent()
			&& this.isAlive()
			&& this.level() instanceof ServerLevel serverLevel
			&& serverLevel.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)
			&& !this.isPrimed()) {
			int fuse = this.explosionData.get().fuse();
			// 26.2's PrimedTnt.getRandomShortFuse.
			int fuseTime = imminent ? this.getRandom().nextInt(Math.max(1, fuse / 4)) + fuse / 8 : fuse;
			this.setInvulnerable(true);
			this.setFuse(fuseTime);
			this.entityData.set(MAX_FUSE, fuseTime);
			this.makeSound(SoundEvents.TNT_PRIMED);
			this.gameEvent(GameEvent.PRIME_FUSE);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);
		if (this.pickupTimer > 0) {
			this.pickupTimer--;
		}

		if (this.pushSoundCooldown > 0) {
			this.pushSoundCooldown--;
		}
	}

	/** Applies the archetype of the body item once it changes, as 26.2's {@code collectEquipmentChanges} override does. */
	private void updateArchetype() {
		ItemStack previous = this.appliedBodyItem;
		ItemStack current = this.getItemBySlot(EquipmentSlot.BODY);
		if (ItemStack.matches(previous, current)) {
			return;
		}

		this.appliedBodyItem = current.copy();
		if (!current.isEmpty()) {
			this.removeAllGoals(g -> true);
			this.setSpeed(0.0F);
		} else {
			this.registerGoals();
		}

		for (SulfurCubeArchetype archetype : this.matchingArchetypes(previous)) {
			for (SulfurCubeArchetype.AttributeEntry mod : archetype.attributeModifiers()) {
				AttributeInstance attr = this.getAttribute(mod.attribute());
				if (attr != null) {
					attr.removeModifier(mod.modifier());
				}
			}
		}

		this.floatsInLiquids = false;
		this.explosionData = Optional.empty();
		this.contactDamages.clear();
		this.knockbackModifier = SulfurCubeArchetype.DEFAULT_KNOCKBACK_MODIFIERS;
		this.soundSettings = SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS;

		for (SulfurCubeArchetype archetype : this.matchingArchetypes(current)) {
			if (archetype.buoyant()) {
				this.floatsInLiquids = true;
			}

			if (archetype.explosion().isPresent()) {
				this.explosionData = archetype.explosion();
			}

			if (archetype.contactDamage().isPresent()) {
				this.contactDamages.add(archetype.contactDamage().get());
			}

			this.knockbackModifier = archetype.knockbackModifiers();
			this.soundSettings = archetype.soundSettings();

			for (SulfurCubeArchetype.AttributeEntry mod : archetype.attributeModifiers()) {
				AttributeInstance attr = this.getAttribute(mod.attribute());
				if (attr != null) {
					attr.addOrUpdateTransientModifier(mod.modifier());
				}
			}
		}
	}

	@Override
	public float maxUpStep() {
		return this.hasBodyItem() ? 0.0F : super.maxUpStep();
	}

	/** 26.2's {@code omnidirectionalAirMover}: a cube holding a block keeps its air drag on every axis. */
	public boolean omnidirectionalAirMover() {
		return this.hasBodyItem();
	}

	@Override
	public boolean canFreeze() {
		return this.hasBodyItem() ? false : super.canFreeze();
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack heldItem = player.getItemInHand(hand);
		if (this.isBaby()) {
			if (this.isFood(heldItem) && AgeLocking.canAgeUp(this)) {
				int age = this.getAge();
				heldItem.consume(1, player);
				this.ageUp(getSpeedUpSecondsWhenFeeding(-age), true);
				this.playEatingSound();
				return InteractionResult.SUCCESS;
			} else {
				return super.mobInteract(player, hand);
			}
		} else {
			if (this.isPrimed()) {
				return InteractionResult.PASS;
			}

			if (!this.canExplode() || !heldItem.is(Items.FLINT_AND_STEEL) && !heldItem.is(Items.FIRE_CHARGE)) {
				if (heldItem.is(Items.SHEARS) && this.readyForShearing()) {
					if (this.level() instanceof ServerLevel level) {
						ItemStack itemStackToShear = this.getItemBySlot(EquipmentSlot.BODY);
						this.shear(level, SoundSource.PLAYERS, heldItem);
						this.gameEvent(GameEvent.SHEAR, player);
						heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
						CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer)player, itemStackToShear, this);
					}

					return InteractionResult.SUCCESS;
				} else if (isSwallowableItem(heldItem)) {
					boolean itWorked = this.equipItem(heldItem);
					if (itWorked) {
						heldItem.consume(1, player);
						this.gameEvent(GameEvent.ENTITY_INTERACT);
					}

					return itWorked ? InteractionResult.SUCCESS_SERVER : InteractionResult.PASS;
				} else {
					return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
				}
			} else if (this.level() instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(GameRules.RULE_TNT_EXPLODES)) {
				player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
				return InteractionResult.PASS;
			} else {
				this.primeTime(false);
				if (heldItem.is(Items.FLINT_AND_STEEL)) {
					heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
				} else {
					heldItem.consume(1, player);
				}

				player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
				return InteractionResult.SUCCESS_SERVER;
			}
		}
	}

	public boolean equipItem(ItemStack heldItem) {
		if (this.isBaby()) {
			return false;
		}

		if (this.hasBodyItem()) {
			Item swallowedItem = this.getItemBySlot(EquipmentSlot.BODY).getItem();
			if (heldItem.is(swallowedItem)) {
				return false;
			}

			Vec3 equipmentSpawnOffset = this.getAttachments().getAverage(EntityAttachment.PASSENGER);
			if (this.level() instanceof ServerLevel serverLevel) {
				this.spawnAtLocation(serverLevel, this.getItemBySlot(EquipmentSlot.BODY), equipmentSpawnOffset);
			}
		}

		if (!this.level().isClientSide) {
			this.setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, heldItem.copyWithCount(1));
			this.updateArchetype();
		}

		this.playSound(this.getAbsorbSound());
		return true;
	}

	private void applyContactDamage(Entity entity) {
		if (this.level() instanceof ServerLevel serverLevel) {
			for (SulfurCubeArchetype.ContactDamage damage : this.contactDamages) {
				entity.hurtServer(serverLevel, new DamageSource(damage.damageType(), damage.attributeToSource() ? this : null), damage.amount().sample(this.getRandom()));
			}
		}
	}

	protected void playEatingSound() {
		this.makeSound(ChaosSounds.SULFUR_CUBE_SMALL_EAT);
	}

	@Override
	public SoundSource getSoundSource() {
		return SoundSource.NEUTRAL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTiny() ? ChaosSounds.SULFUR_CUBE_SMALL_HURT : ChaosSounds.SULFUR_CUBE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTiny() ? ChaosSounds.SULFUR_CUBE_SMALL_DEATH : ChaosSounds.SULFUR_CUBE_DEATH;
	}

	@Override
	protected SoundEvent getSquishSound() {
		if (this.isTiny()) {
			return ChaosSounds.SULFUR_CUBE_SMALL_SQUISH;
		} else {
			return this.hasBodyItem() ? ChaosSounds.SULFUR_CUBE_BOUNCE : ChaosSounds.SULFUR_CUBE_SQUISH;
		}
	}

	@Override
	protected SoundEvent getJumpSound() {
		return this.isTiny() ? ChaosSounds.SULFUR_CUBE_SMALL_JUMP : ChaosSounds.SULFUR_CUBE_JUMP;
	}

	private SoundEvent getAbsorbSound() {
		return ChaosSounds.SULFUR_CUBE_ABSORB;
	}

	private SoundEvent getEjectSound() {
		return ChaosSounds.SULFUR_CUBE_EJECT;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockState) {
		if (!this.hasBodyItem()) {
			super.playStepSound(pos, blockState);
		}
	}

	@Override
	protected @Nullable ParticleOptions getParticleType() {
		return ChaosParticles.SULFUR_CUBE_GOO;
	}

	public static AttributeSupplier.Builder createSulfurCubeAttributes() {
		return Mob.createMobAttributes().add(Attributes.TEMPT_RANGE, 8.0);
	}

	@Override
	public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
		Vec3 equipmentSpawnOffset = this.getAttachments().getAverage(EntityAttachment.PASSENGER);
		ItemStack itemStackToShear = this.getItemBySlot(EquipmentSlot.BODY);
		this.setItemSlot(EquipmentSlot.BODY, ItemStack.EMPTY);
		this.spawnAtLocation(level, itemStackToShear, equipmentSpawnOffset);
		this.playSound(this.getEjectSound());
		this.pickupTimer = PICKUP_TIMER_DURATION;
	}

	@Override
	public boolean readyForShearing() {
		return this.hasBodyItem();
	}

	@Override
	public boolean canPickUpLoot() {
		return !this.hasBodyItem();
	}

	private static boolean isSwallowableItem(ItemStack itemStack) {
		return itemStack.is(ChaosTags.SULFUR_CUBE_SWALLOWABLE);
	}

	@Override
	public boolean canUseSlot(EquipmentSlot slot) {
		return slot != EquipmentSlot.BODY ? super.canUseSlot(slot) : this.isAlive() && !this.isBaby();
	}

	@Override
	protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
		return slot == EquipmentSlot.BODY;
	}

	@Override
	public boolean canHoldItem(ItemStack itemStack) {
		ItemStack heldItemStack = this.getItemBySlot(EquipmentSlot.BODY);
		return heldItemStack.isEmpty() && isSwallowableItem(itemStack) && !this.isBaby();
	}

	@Override
	protected void pickUpItem(ServerLevel level, ItemEntity entity) {
		ItemStack itemStack = entity.getItem();
		if (this.canHoldItem(itemStack) && this.pickupTimer <= 0) {
			this.onItemPickup(entity);
			this.setItemSlot(EquipmentSlot.BODY, itemStack.split(1));
			this.playSound(this.getAbsorbSound());
			this.setGuaranteedDrop(EquipmentSlot.BODY);
			this.take(entity, 1);
			if (itemStack.isEmpty()) {
				entity.discard();
			}
		}
	}

	@Override
	protected int getBaseExperienceReward(ServerLevel level) {
		return this.isBaby() ? 0 : 1 + this.random.nextInt(2);
	}

	@Override
	protected int getSplitCount() {
		return this.isPrimed() ? 0 : 2;
	}

	@Override
	protected void setSpawnSize(ServerLevelAccessor level, DifficultyInstance difficulty) {
		if (this.isBaby()) {
			this.setSize(1, true);
		} else {
			this.setSize(MAX_SIZE, true);
		}
	}

	@Override
	public void setSize(int size, boolean updateHealth) {
		super.setSize(size, updateHealth);
		if (updateHealth && size == 1 && !this.isBaby()) {
			this.setBaby(true);
		}
	}

	@Override
	protected void setUpSplitCube(AbstractCubeMob cubeMob, int halfSize, float xd, float zd) {
		super.setUpSplitCube(cubeMob, halfSize, xd, zd);
		cubeMob.setBaby(true);
	}

	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
		SulfurCube sulfurCube = ChaosEntities.SULFUR_CUBE.create(level, EntitySpawnReason.BREEDING);
		if (sulfurCube != null) {
			sulfurCube.setSize(1, true);
		}

		return sulfurCube;
	}

	private boolean isFood(ItemStack itemStack) {
		return itemStack.is(ChaosTags.SULFUR_CUBE_FOOD);
	}

	@Override
	protected void ageBoundaryReached() {
		super.ageBoundaryReached();
		if (!this.isBaby()) {
			this.setSize(MAX_SIZE, true);
		}
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.putInt("pickup_timer", this.pickupTimer);
		output.putBoolean("from_bucket", this.fromBucket());
		output.putInt("fuse", this.getFuse());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		this.pickupTimer = input.getIntOr("pickup_timer", 0);
		this.setFromBucket(input.getBooleanOr("from_bucket", false));
		this.setFuse(input.getIntOr("fuse", -1));
		this.entityData.set(MAX_FUSE, this.getFuse());
		super.readAdditionalSaveData(input);
	}

	@Override
	protected void doPush(Entity entity) {
		super.doPush(entity);
		this.applyContactDamage(entity);
	}

	@Override
	public void playerTouch(Player player) {
		super.playerTouch(player);
		this.playerPush(player);
	}

	private void playerPush(Player player) {
		if (this.hasBodyItem()) {
			Entity pusher = player.isPassenger() ? player.getRootVehicle() : player;
			Vec3 cubeToPusher = this.position().subtract(pusher.position());
			double pusherFeetPosition = pusher.getY();
			double sulfurCubeBottomPosition = this.getY();
			double sulfurCubeTopPosition = sulfurCubeBottomPosition + this.getBbHeight();
			double pusherTopPosition = pusherFeetPosition + pusher.getBbHeight();
			if (cubeToPusher.horizontalDistance() < 1.3F && pusherFeetPosition <= sulfurCubeTopPosition && pusherTopPosition > sulfurCubeBottomPosition) {
				double knockback = Math.max(0.0, 1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
				Vec3 pushDirection = cubeToPusher.horizontal().normalize().scale(knockback);
				float pushSpeedScale = player.isPassenger() ? 0.16F : 0.3F;
				double playerSpeed = SpearCombat.getKnownSpeed(player).length() * 2.0 * pushSpeedScale;
				playerSpeed = Mth.clamp(playerSpeed, 0.0, 0.5);
				Vec3 pushVelocity = new Vec3(pushDirection.x, this.onGround() ? knockback * 0.3F : 0.0, pushDirection.z).scale(playerSpeed);
				this.hasImpulse = true;
				float pushSoundThreshold = this.soundSettings.pushSoundImpulseThreshold();
				if (pushVelocity.lengthSqr() > pushSoundThreshold * pushSoundThreshold && this.pushSoundCooldown <= 0) {
					this.pushSoundCooldown = (int)(this.soundSettings.pushSoundCooldown() * 20.0F);
					this.playSound(this.soundSettings.pushSound().value());
				}

				this.addDeltaMovement(pushVelocity);
				this.applyContactDamage(player);
			}
		}
	}

	private static Vec2 rotate(Vec2 vector, float angle) {
		float cos = Mth.cos(angle);
		float sin = Mth.sin(angle);
		return new Vec2(vector.x * cos - vector.y * sin, vector.x * sin + vector.y * cos);
	}

	private Vec2 applyHorizontalHitAngleScale(
		float horizontalAngleScale, Vec2 originalAngle, Vec3 attackerPosition, Vec3 attackerAimDirection, Vec3 targetCenter
	) {
		Vec3 attackerToTarget = targetCenter.subtract(attackerPosition).normalize();
		float angleDiff = (float)Math.atan2(
			attackerAimDirection.x * attackerToTarget.z - attackerAimDirection.z * attackerToTarget.x,
			attackerAimDirection.x * attackerToTarget.x + attackerAimDirection.z * attackerToTarget.z
		);
		return rotate(originalAngle, angleDiff * horizontalAngleScale);
	}

	private Vec2 applyVerticalHitAnglePowerTransfer(
		float verticalHitAngleScale,
		float horizontalPower,
		float verticalPower,
		Vec3 attackerPosition,
		Vec3 attackerAimDirection,
		Vec3 targetCenteredPosition,
		float targetHeight
	) {
		float targetHalfHeight = 0.5F * targetHeight;
		Vec3 targetTopPos = targetCenteredPosition.add(0.0, targetHalfHeight, 0.0);
		Vec3 targetBottomPos = targetCenteredPosition.add(0.0, -targetHalfHeight, 0.0);
		Vec3 attackerToTargetTop = targetTopPos.subtract(attackerPosition).normalize();
		Vec3 attackerToTargetBottom = targetBottomPos.subtract(attackerPosition).normalize();
		float verticalHitAngleFactor = (float)Mth.clampedMap(attackerAimDirection.y, attackerToTargetTop.y, attackerToTargetBottom.y, -1.0, 1.0);
		float transferredPowerRatio = Math.abs(verticalHitAngleFactor * verticalHitAngleScale);
		if (verticalHitAngleFactor < 0.0F) {
			transferredPowerRatio = -transferredPowerRatio;
		}

		float px = horizontalPower * (1.0F - transferredPowerRatio);
		float py = verticalPower * (1.0F + transferredPowerRatio);
		return new Vec2(px, py);
	}

	private Vec2 applyVerticalPositionAnglePowerRotation(
		float verticalPositionAngleScale,
		float horizontalPower,
		float verticalPower,
		float originalHorizontalPower,
		float originalVerticalPower,
		Vec3 attackerFeetPosition,
		Vec3 targetFeetPosition
	) {
		Vec3 attackerFeetToTargetFeet = targetFeetPosition.subtract(attackerFeetPosition);
		float verticalPositionAngle = (float)Math.atan2(-attackerFeetToTargetFeet.y, attackerFeetToTargetFeet.horizontalDistance());
		Vec2 powerBeforeRotation = new Vec2(horizontalPower, verticalPower);
		Vec2 rotatedPower = rotate(powerBeforeRotation, -verticalPositionAngle * verticalPositionAngleScale);
		float horizontalRatio = originalHorizontalPower > 0.0F ? Mth.abs(rotatedPower.x) / originalHorizontalPower : 0.0F;
		float verticalRatio = originalVerticalPower > 0.0F ? Mth.abs(rotatedPower.y) / originalVerticalPower : 0.0F;
		float maxRatio = Math.max(horizontalRatio, verticalRatio);
		if (maxRatio > 1.0F) {
			rotatedPower = rotatedPower.scale(1.0F / maxRatio);
		}

		return rotatedPower;
	}

	/** 26.2's knockback for a cube holding a block: aimed hits steer it like a ball. Other knockback is vanilla. */
	@Override
	public void knockback(double power, double xd, double zd) {
		DamageSource source = this.hurtSource;
		if (source != null && source.getEntity() != null && this.hasBodyItem()) {
			Entity attacker = source.getEntity();
			float horizontalPower = this.knockbackModifier.horizontalPower();
			float verticalPower = this.knockbackModifier.verticalPower();
			float originalHorizontalPower = horizontalPower;
			float originalVerticalPower = verticalPower;
			Holder<SoundEvent> hitSound = this.soundSettings.hitSound();
			Vec2 originalAngle = new Vec2((float)xd, (float)zd);
			Vec2 newAngle = this.applyHorizontalHitAngleScale(
				1.6F, originalAngle, attacker.getEyePosition(), attacker.getLookAngle().normalize(), this.getBoundingBox().getCenter()
			);
			Vec2 newPower = this.applyVerticalHitAnglePowerTransfer(
				0.5F, horizontalPower, verticalPower, attacker.getEyePosition(), attacker.getLookAngle().normalize(), this.getBoundingBox().getCenter(), this.getBbHeight()
			);
			horizontalPower = newPower.x;
			verticalPower = newPower.y;
			newPower = this.applyVerticalPositionAnglePowerRotation(
				0.8F, horizontalPower, verticalPower, originalHorizontalPower, originalVerticalPower, attacker.position(), this.position()
			);
			horizontalPower = newPower.x;
			verticalPower = newPower.y;
			xd = newAngle.x;
			zd = newAngle.y;
			float powerMultiplier = Mth.sqrt(this.hurtDamage);
			horizontalPower *= powerMultiplier;
			verticalPower *= powerMultiplier;
			double knockBackResistance = this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			horizontalPower *= (float)(1.0 - knockBackResistance);
			verticalPower *= (float)(1.0 - knockBackResistance);
			this.hasImpulse = true;
			Vec3 deltaMovement = this.getDeltaMovement();
			horizontalPower *= 0.4F;
			horizontalPower = Mth.clamp(horizontalPower, -128.0F, 128.0F);
			verticalPower = Mth.clamp(verticalPower, -128.0F, 128.0F);
			Vec3 horizontalKnockback = new Vec3(xd, 0.0, zd).normalize().scale(horizontalPower);
			this.setDeltaMovement(deltaMovement.x - horizontalKnockback.x, deltaMovement.y + verticalPower * 1.2, deltaMovement.z - horizontalKnockback.z);
			this.playSound(hitSound.value());
		} else {
			super.knockback(power, xd, zd);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> @Nullable T get(DataComponentType<? extends T> type) {
		return type == ChaosComponents.SULFUR_CUBE_CONTENT
			? castComponentValue((DataComponentType<T>)type, getSulfurCubeContent(this.getBodyArmorItem()))
			: super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		this.applyImplicitComponentIfPresent(components, ChaosComponents.SULFUR_CUBE_CONTENT);
		super.applyImplicitComponents(components);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == ChaosComponents.SULFUR_CUBE_CONTENT) {
			this.setSulfurCubeContent(castComponentValue(ChaosComponents.SULFUR_CUBE_CONTENT, value));
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}

	private static @Nullable SulfurCubeContent getSulfurCubeContent(ItemStack itemStack) {
		return itemStack.isEmpty() ? null : SulfurCubeContent.ofNonEmpty(itemStack);
	}

	private void setSulfurCubeContent(SulfurCubeContent sulfurCubeContent) {
		this.setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, sulfurCubeContent.absorbedBlockItemStack());
	}

	@Override
	public Vec3 getLeashOffset() {
		return new Vec3(0.0, this.getBbHeight() / 2.0F, 0.0);
	}

	@Override
	protected void setCubeMobHealth(int actualSize) {
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4 * actualSize);
	}

	private class SulfurCubeLookControl extends LookControl {
		private SulfurCubeLookControl() {
			super(SulfurCube.this);
		}

		@Override
		public void tick() {
			if (!SulfurCube.this.hasBodyItem()) {
				super.tick();
			} else {
				// 26.2's Mth.wrapDegrees90: snap the body to the nearest right angle.
				float yRot = SulfurCube.this.getYRot();
				float closeAngle = yRot - Math.round(yRot / 90.0F) * 90.0F;
				SulfurCube.this.setYRot(yRot - closeAngle);
				SulfurCube.this.setYHeadRot(SulfurCube.this.getYRot());
			}
		}
	}

	protected static class SulfurCubeMobMoveControl extends AbstractCubeMob.CubeMobMoveControl {
		private final SulfurCube sulfurCube;

		public SulfurCubeMobMoveControl(SulfurCube cubeMob) {
			super(cubeMob);
			this.sulfurCube = cubeMob;
		}

		@Override
		public void tick() {
			if (!this.sulfurCube.hasBodyItem()) {
				super.tick();
			}
		}
	}

	private class SulfurCubeSearchForItemsGoal extends Goal {
		private final SulfurCube sulfurCube;
		private @Nullable ItemEntity targetItem;

		public SulfurCubeSearchForItemsGoal(SulfurCube sulfurCube) {
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
			this.sulfurCube = sulfurCube;
		}

		@Override
		public boolean canUse() {
			if (!this.sulfurCube.isBaby() && this.sulfurCube.pickupTimer <= 0) {
				ItemEntity nearest = null;
				double nearestDistance = Double.MAX_VALUE;
				for (ItemEntity item : this.sulfurCube.level().getEntitiesOfClass(ItemEntity.class, this.sulfurCube.getBoundingBox().inflate(8.0, 8.0, 8.0), SulfurCube.ALLOWED_ITEMS)) {
					double distance = item.distanceToSqr(this.sulfurCube);
					if (distance < nearestDistance) {
						nearestDistance = distance;
						nearest = item;
					}
				}

				this.targetItem = nearest;
				return this.targetItem != null;
			} else {
				return false;
			}
		}

		@Override
		public void tick() {
			if (this.targetItem != null) {
				SulfurCube.this.lookAt(this.targetItem, 10.0F, 10.0F);
			}

			if (SulfurCube.this.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl) {
				cubeMobMoveControl.setDirection(SulfurCube.this.getYRot(), true);
			}
		}
	}

	private static class SulfurCubeTemptGoal extends TemptGoal.ForNonPathfinders {
		public SulfurCubeTemptGoal(Mob mob, double speedModifier, Predicate<ItemStack> items, boolean canScare, double stopDistance) {
			super(mob, speedModifier, items, canScare, stopDistance);
			this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		@Override
		protected void stopNavigation() {
			if (this.mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl) {
				cubeMobMoveControl.setWantedMovement(0.0);
			}
		}

		@Override
		protected void navigateTowards(Player player) {
			this.mob.lookAt(player, 10.0F, 10.0F);
			if (this.mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl) {
				cubeMobMoveControl.setDirection(this.mob.getYRot(), true);
			}
		}
	}
}
