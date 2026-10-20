package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.MountsSounds;
import backport.everything_backport.mountsofmayhem.MountsTags;
import backport.everything_backport.mountsofmayhem.Undead;
import java.util.function.DoubleSupplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 1.21.11 reworks zombie horses: they spawn at night ridden by spear zombies, can be tamed with red mushrooms, wear
 * armor and saddles, burn in daylight and can no longer be bred.
 */
@Mixin(ZombieHorse.class)
public abstract class ZombieHorseMixin extends AbstractHorse {
	private ZombieHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$pathing(EntityType<? extends ZombieHorse> entityType, Level level, CallbackInfo ci) {
		this.setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
		this.setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
	}

	/**
	 * @author Everything Backport
	 * @reason 1.21.11 zombie horses have 25 health and a random speed.
	 */
	@Overwrite
	public static AttributeSupplier.Builder createAttributes() {
		return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 25.0);
	}

	/**
	 * @author Everything Backport
	 * @reason 1.21.11 zombie horses roll speed as well as jump strength.
	 */
	@Overwrite
	protected void randomizeAttributes(RandomSource randomSource) {
		this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(everythingBackport$jumpStrength(randomSource::nextDouble));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(everythingBackport$speed(randomSource::nextDouble));
	}

	private static double everythingBackport$jumpStrength(DoubleSupplier random) {
		return 0.5 + random.getAsDouble() * 0.06666666666666667 + random.getAsDouble() * 0.06666666666666667 + random.getAsDouble() * 0.06666666666666667;
	}

	private static double everythingBackport$speed(DoubleSupplier random) {
		return (9.0 + random.getAsDouble() + random.getAsDouble() + random.getAsDouble()) / 42.16F;
	}

	/**
	 * @author Everything Backport
	 * @reason Zombie horses can no longer be bred in 1.21.11.
	 */
	@Overwrite
	@Nullable
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return null;
	}

	/**
	 * @author Everything Backport
	 * @reason 1.21.11 zombie horses are tamed and fed like other horses. They also become persistent once interacted with,
	 * which 1.21.11 does in Mob#interact (final in 1.21.7).
	 */
	@Overwrite
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		this.setPersistenceRequired();
		boolean openInventory = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
		if (!this.isVehicle() && !openInventory) {
			ItemStack itemStack = player.getItemInHand(interactionHand);
			if (!itemStack.isEmpty()) {
				if (this.isFood(itemStack)) {
					return this.fedFood(player, itemStack);
				}

				if (!this.isTamed()) {
					this.makeMad();
					return InteractionResult.SUCCESS;
				}
			}
		}

		return super.mobInteract(player, interactionHand);
	}

	/**
	 * @author Everything Backport
	 * @reason 1.21.11 zombie horses swim and follow red mushrooms.
	 */
	@Overwrite
	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, itemStack -> itemStack.is(MountsTags.Items.ZOMBIE_HORSE_FOOD), false));
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return true;
	}

	@Override
	protected SoundEvent getAngrySound() {
		return MountsSounds.ZOMBIE_HORSE_ANGRY;
	}

	@Override
	protected SoundEvent getEatingSound() {
		return MountsSounds.ZOMBIE_HORSE_EAT;
	}

	@Override
	public boolean canFallInLove() {
		return false;
	}

	@Override
	public boolean isFood(ItemStack itemStack) {
		return itemStack.is(MountsTags.Items.ZOMBIE_HORSE_FOOD);
	}

	@Override
	public boolean canUseSlot(EquipmentSlot equipmentSlot) {
		return true;
	}

	@Override
	public boolean canBeLeashed() {
		return this.isTamed() || !(this.getFirstPassenger() instanceof Mob);
	}

	@Override
	public Vec3[] getQuadLeashOffsets() {
		return Leashable.createQuadLeashOffsets(this, 0.04, 0.41, 0.18, 0.73);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		Undead.burnInDaylight(this, EquipmentSlot.BODY);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(
		ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData
	) {
		if (entitySpawnReason == EntitySpawnReason.NATURAL) {
			Zombie zombie = EntityType.ZOMBIE.create(this.level(), EntitySpawnReason.JOCKEY);
			if (zombie != null) {
				zombie.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
				zombie.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, null);
				zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(MountsItems.IRON_SPEAR));
				zombie.startRiding(this, false);
			}
		}

		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
	}
}
