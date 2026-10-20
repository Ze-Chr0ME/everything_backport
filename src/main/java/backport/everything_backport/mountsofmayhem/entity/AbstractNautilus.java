package backport.everything_backport.mountsofmayhem.entity;

import backport.everything_backport.mountsofmayhem.MountsEffects;
import backport.everything_backport.mountsofmayhem.MountsSounds;
import backport.everything_backport.mountsofmayhem.MountsTags;
import backport.everything_backport.mountsofmayhem.inventory.MountMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNautilus extends TamableAnimal implements HasCustomInventoryScreen, PlayerRideableJumping {
	public static final int INVENTORY_SLOT_OFFSET = 500;
	public static final int INVENTORY_ROWS = 3;
	public static final int SMALL_RESTRICTION_RADIUS = 16;
	public static final int LARGE_RESTRICTION_RADIUS = 32;
	public static final int RESTRICTION_RADIUS_BUFFER = 8;
	private static final int EFFECT_DURATION = 60;
	private static final int EFFECT_REFRESH_RATE = 40;
	private static final double NAUTILUS_WATER_RESISTANCE = 0.9;
	private static final float IN_WATER_SPEED_MODIFIER = 0.011F;
	private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325F;
	private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02F;
	private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
	private static final int DASH_COOLDOWN_TICKS = 40;
	private static final int DASH_MINIMUM_DURATION_TICKS = 5;
	private static final float DASH_MOMENTUM_IN_WATER = 1.2F;
	private static final float DASH_MOMENTUM_ON_LAND = 0.5F;
	private int dashCooldown = 0;
	protected float playerJumpPendingScale;
	protected SimpleContainer inventory;
	private static final double BUBBLE_SPREAD_FACTOR = 0.8;
	private static final double BUBBLE_DIRECTION_SCALE = 1.1;
	private static final double BUBBLE_Y_OFFSET = 0.25;
	private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0;
	private static final float BUBBLE_PROBABILITY_MIN = 0.15F;
	private static final float BUBBLE_PROBABILITY_MAX = 1.0F;

	protected AbstractNautilus(EntityType<? extends AbstractNautilus> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, IN_WATER_SPEED_MODIFIER, 0.0F, true);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.setPathfindingMalus(PathType.WATER, 0.0F);
		this.createInventory();
	}

	@Override
	public boolean isFood(ItemStack itemStack) {
		return !this.isTame() && !this.isBaby() ? itemStack.is(MountsTags.Items.NAUTILUS_TAMING_ITEMS) : itemStack.is(MountsTags.Items.NAUTILUS_FOOD);
	}

	@Override
	protected void usePlayerItem(Player player, InteractionHand interactionHand, ItemStack itemStack) {
		if (itemStack.is(MountsTags.Items.NAUTILUS_BUCKET_FOOD)) {
			player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.WATER_BUCKET)));
		} else {
			super.usePlayerItem(player, interactionHand, itemStack);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Animal.createAnimalAttributes()
			.add(Attributes.MAX_HEALTH, 15.0)
			.add(Attributes.MOVEMENT_SPEED, 1.0)
			.add(Attributes.ATTACK_DAMAGE, 3.0)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.3F);
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new WaterBoundPathNavigation(this, level);
	}

	@Override
	public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
		return 0.0F;
	}

	public static boolean checkNautilusSpawnRules(
		EntityType<? extends AbstractNautilus> entityType, LevelAccessor levelAccessor, EntitySpawnReason entitySpawnReason, BlockPos blockPos, RandomSource randomSource
	) {
		int seaLevel = levelAccessor.getSeaLevel();
		int minY = seaLevel - 25;
		return blockPos.getY() >= minY
			&& blockPos.getY() <= seaLevel - 5
			&& levelAccessor.getFluidState(blockPos.below()).is(FluidTags.WATER)
			&& levelAccessor.getBlockState(blockPos.above()).is(Blocks.WATER);
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader levelReader) {
		return levelReader.isUnobstructed(this);
	}

	@Override
	public boolean canUseSlot(EquipmentSlot equipmentSlot) {
		return equipmentSlot != EquipmentSlot.SADDLE && equipmentSlot != EquipmentSlot.BODY
			? super.canUseSlot(equipmentSlot)
			: this.isAlive() && !this.isBaby() && this.isTame();
	}

	@Override
	protected boolean canDispenserEquipIntoSlot(EquipmentSlot equipmentSlot) {
		return equipmentSlot == EquipmentSlot.BODY || equipmentSlot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(equipmentSlot);
	}

	@Override
	protected boolean canAddPassenger(Entity entity) {
		return !this.isVehicle();
	}

	@Nullable
	@Override
	public LivingEntity getControllingPassenger() {
		return (LivingEntity)(this.isSaddled() && this.getFirstPassenger() instanceof Player player ? player : super.getControllingPassenger());
	}

	@Override
	protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
		float strafe = player.xxa;
		float forward = 0.0F;
		float vertical = 0.0F;
		if (player.zza != 0.0F) {
			float cos = Mth.cos(player.getXRot() * (float) (Math.PI / 180.0));
			float sin = -Mth.sin(player.getXRot() * (float) (Math.PI / 180.0));
			if (player.zza < 0.0F) {
				cos *= -0.5F;
				sin *= -0.5F;
			}

			vertical = sin;
			forward = cos;
		}

		return new Vec3(strafe, vertical, forward);
	}

	protected Vec2 getRiddenRotation(LivingEntity livingEntity) {
		return new Vec2(livingEntity.getXRot() * 0.5F, livingEntity.getYRot());
	}

	@Override
	protected void tickRidden(Player player, Vec3 vec3) {
		super.tickRidden(player, vec3);
		Vec2 rotation = this.getRiddenRotation(player);
		float yRot = this.getYRot();
		float delta = Mth.wrapDegrees(rotation.y - yRot);
		yRot += delta * 0.5F;
		this.setRot(yRot, rotation.x);
		this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
		if (this.isLocalInstanceAuthoritative()) {
			if (this.playerJumpPendingScale > 0.0F && !this.isJumping()) {
				this.executeRidersJump(this.playerJumpPendingScale, player);
			}

			this.playerJumpPendingScale = 0.0F;
		}
	}

	/** 1.21.11's {@code travelInWater} override: nautiluses swim at their own speed with little drag. */
	@Override
	public void travel(Vec3 vec3) {
		if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(this.level().getFluidState(this.blockPosition()))) {
			this.moveRelative(this.getSpeed(), vec3);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(NAUTILUS_WATER_RESISTANCE));
		} else {
			super.travel(vec3);
		}
	}

	@Override
	protected float getRiddenSpeed(Player player) {
		return this.isInWater()
			? RIDDEN_SPEED_MODIFIER_IN_WATER * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED)
			: RIDDEN_SPEED_MODIFIER_ON_LAND * (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
	}

	protected void doPlayerRide(Player player) {
		if (!this.level().isClientSide()) {
			player.startRiding(this);
			if (!this.isVehicle()) {
				this.clearHome();
			}
		}
	}

	private int getNautilusRestrictionRadius() {
		return !this.isBaby() && this.getItemBySlot(EquipmentSlot.SADDLE).isEmpty() ? LARGE_RESTRICTION_RADIUS : SMALL_RESTRICTION_RADIUS;
	}

	protected void checkRestriction() {
		if (!this.isLeashed() && !this.isVehicle() && this.isTame()) {
			int radius = this.getNautilusRestrictionRadius();
			if (!this.hasHome()
				|| !this.getHomePosition().closerThan(this.blockPosition(), radius + RESTRICTION_RADIUS_BUFFER)
				|| radius != this.getHomeRadius()) {
				this.setHomeTo(this.blockPosition(), radius);
			}
		}
	}

	@Override
	protected void customServerAiStep(ServerLevel serverLevel) {
		this.checkRestriction();
		super.customServerAiStep(serverLevel);
	}

	private void applyEffects(Level level) {
		if (this.getFirstPassenger() instanceof Player player) {
			boolean hasEffect = player.hasEffect(MountsEffects.BREATH_OF_THE_NAUTILUS);
			boolean refresh = level.getGameTime() % EFFECT_REFRESH_RATE == 0L;
			if (!hasEffect || refresh) {
				player.addEffect(new MobEffectInstance(MountsEffects.BREATH_OF_THE_NAUTILUS, EFFECT_DURATION, 0, true, true, true));
			}
		}
	}

	private void spawnBubbles() {
		double speed = this.getDeltaMovement().length();
		double chance = Mth.clamp(speed * BUBBLE_PROBABILITY_MULTIPLIER, BUBBLE_PROBABILITY_MIN, BUBBLE_PROBABILITY_MAX);
		if (this.random.nextFloat() < chance) {
			float yRot = this.getYRot();
			float xRot = Mth.clamp(this.getXRot(), -10.0F, 10.0F);
			Vec3 view = Vec3.directionFromRotation(xRot, yRot);
			double spread = this.random.nextDouble() * BUBBLE_SPREAD_FACTOR * (1.0 + speed);
			double dx = (this.random.nextFloat() - 0.5) * spread;
			double dy = (this.random.nextFloat() - 0.5) * spread;
			double dz = (this.random.nextFloat() - 0.5) * spread;
			this.level()
				.addParticle(
					ParticleTypes.BUBBLE,
					this.getX() - view.x * BUBBLE_DIRECTION_SCALE,
					this.getY() - view.y + BUBBLE_Y_OFFSET,
					this.getZ() - view.z * BUBBLE_DIRECTION_SCALE,
					dx,
					dy,
					dz
				);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.applyEffects(this.level());
		}

		if (this.isDashing() && this.dashCooldown < DASH_COOLDOWN_TICKS - DASH_MINIMUM_DURATION_TICKS) {
			this.setDashing(false);
		}

		if (this.dashCooldown > 0) {
			this.dashCooldown--;
			if (this.dashCooldown == 0) {
				SoundEvent sound = this.getDashReadySound();
				if (sound != null) {
					this.makeSound(sound);
				}
			}
		}

		if (this.isInWater()) {
			this.spawnBubbles();
		}
	}

	@Override
	public boolean canJump() {
		return this.isSaddled();
	}

	@Override
	public void onPlayerJump(int power) {
		if (this.isSaddled() && this.dashCooldown <= 0) {
			this.playerJumpPendingScale = power >= 90 ? 1.0F : 0.4F + 0.4F * power / 90.0F;
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DASH, false);
	}

	public boolean isDashing() {
		return this.entityData.get(DASH);
	}

	public void setDashing(boolean dashing) {
		this.entityData.set(DASH, dashing);
	}

	protected void executeRidersJump(float scale, Player player) {
		this.addDeltaMovement(
			player.getLookAngle()
				.scale((this.isInWater() ? DASH_MOMENTUM_IN_WATER : DASH_MOMENTUM_ON_LAND) * scale * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * this.getBlockSpeedFactor())
		);
		this.dashCooldown = DASH_COOLDOWN_TICKS;
		this.setDashing(true);
		this.hasImpulse = true;
	}

	@Override
	public void handleStartJump(int power) {
		SoundEvent sound = this.getDashSound();
		if (sound != null) {
			this.makeSound(sound);
		}

		this.gameEvent(GameEvent.ENTITY_ACTION);
		this.setDashing(true);
	}

	@Override
	public int getJumpCooldown() {
		return this.dashCooldown;
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		if (!this.firstTick && DASH.equals(entityDataAccessor)) {
			this.dashCooldown = this.dashCooldown == 0 ? DASH_COOLDOWN_TICKS : this.dashCooldown;
		}

		super.onSyncedDataUpdated(entityDataAccessor);
	}

	@Override
	public void handleStopJump() {
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
	}

	@Nullable
	protected SoundEvent getDashSound() {
		return null;
	}

	@Nullable
	protected SoundEvent getDashReadySound() {
		return null;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		// 1.21.11 does this in Mob#interact, which is final in 1.21.7.
		this.setPersistenceRequired();
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (this.isBaby()) {
			return super.mobInteract(player, interactionHand);
		} else if (this.isTame() && player.isSecondaryUseActive()) {
			this.openCustomInventoryScreen(player);
			return InteractionResult.SUCCESS;
		} else {
			if (!itemStack.isEmpty()) {
				if (!this.level().isClientSide() && !this.isTame() && this.isFood(itemStack)) {
					this.usePlayerItem(player, interactionHand, itemStack);
					this.tryToTame(player);
					return InteractionResult.SUCCESS_SERVER;
				}

				if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
					FoodProperties food = itemStack.get(DataComponents.FOOD);
					this.heal(food != null ? 2 * food.nutrition() : 1.0F);
					this.usePlayerItem(player, interactionHand, itemStack);
					this.playEatingSound();
					return InteractionResult.SUCCESS;
				}

				InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, interactionHand);
				if (interactionResult.consumesAction()) {
					return interactionResult;
				}
			}

			if (this.isTame() && !player.isSecondaryUseActive() && !this.isFood(itemStack)) {
				this.doPlayerRide(player);
				return InteractionResult.SUCCESS;
			} else {
				return super.mobInteract(player, interactionHand);
			}
		}
	}

	private void tryToTame(Player player) {
		if (this.random.nextInt(3) == 0) {
			this.tame(player);
			this.navigation.stop();
			this.level().broadcastEntityEvent(this, (byte)7);
		} else {
			this.level().broadcastEntityEvent(this, (byte)6);
		}

		this.playEatingSound();
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return true;
	}

	@Override
	public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float amount) {
		boolean hurt = super.hurtServer(serverLevel, damageSource, amount);
		if (hurt && damageSource.getEntity() instanceof LivingEntity attacker) {
			NautilusAi.setAngerTarget(serverLevel, this, attacker);
		}

		return hurt;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
		return mobEffectInstance.getEffect() == MobEffects.POISON ? false : super.canBeAffected(mobEffectInstance);
	}

	@Override
	public SpawnGroupData finalizeSpawn(
		ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData
	) {
		NautilusAi.initMemories(this, serverLevelAccessor.getRandom());
		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
	}

	@Override
	protected Holder<SoundEvent> getEquipSound(EquipmentSlot equipmentSlot, ItemStack itemStack, Equippable equippable) {
		if (equipmentSlot == EquipmentSlot.SADDLE && this.isUnderWater()) {
			return MountsSounds.NAUTILUS_SADDLE_UNDERWATER_EQUIP;
		} else {
			return equipmentSlot == EquipmentSlot.SADDLE ? MountsSounds.NAUTILUS_SADDLE_EQUIP : super.getEquipSound(equipmentSlot, itemStack, equippable);
		}
	}

	public final int getInventorySize() {
		return MountMenus.getInventorySize(this.getInventoryColumns());
	}

	protected void createInventory() {
		SimpleContainer previous = this.inventory;
		this.inventory = new SimpleContainer(this.getInventorySize());
		if (previous != null) {
			int count = Math.min(previous.getContainerSize(), this.inventory.getContainerSize());

			for (int i = 0; i < count; i++) {
				ItemStack itemStack = previous.getItem(i);
				if (!itemStack.isEmpty()) {
					this.inventory.setItem(i, itemStack.copy());
				}
			}
		}
	}

	@Override
	public void openCustomInventoryScreen(Player player) {
		if (player instanceof ServerPlayer serverPlayer && (!this.isVehicle() || this.hasPassenger(player)) && this.isTame()) {
			MountMenus.openNautilusInventory(serverPlayer, this, this.inventory);
		}
	}

	@Nullable
	@Override
	public SlotAccess getSlot(int slot) {
		int index = slot - INVENTORY_SLOT_OFFSET;
		return index >= 0 && index < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, index) : super.getSlot(slot);
	}

	public boolean hasInventoryChanged(Container container) {
		return this.inventory != container;
	}

	public int getInventoryColumns() {
		return 0;
	}

	protected boolean isMobControlled() {
		return this.getFirstPassenger() instanceof Mob;
	}

	protected boolean isAggravated() {
		return this.getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || this.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
	}
}
