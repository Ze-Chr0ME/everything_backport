package backport.everything_backport.mountsofmayhem.entity;

import backport.everything_backport.mountsofmayhem.MountsSounds;
import backport.everything_backport.mountsofmayhem.MountsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The undead camel ridden by husks (1.21.11). Its dash, sit and stand sounds are swapped in by CamelMixin, since
 * 1.21.7's camel plays those sounds directly.
 */
public class CamelHusk extends Camel implements MountedCharger {
	public CamelHusk(EntityType<? extends Camel> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return true;
	}

	public boolean isMobControlled() {
		return this.getFirstPassenger() instanceof Mob;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
		// 1.21.11 does this in Mob#interact, which is final in 1.21.7.
		this.setPersistenceRequired();
		return super.mobInteract(player, interactionHand);
	}

	@Override
	public boolean canBeLeashed() {
		return !this.isMobControlled();
	}

	@Override
	public boolean isFood(ItemStack itemStack) {
		return itemStack.is(MountsTags.Items.CAMEL_HUSK_FOOD);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return MountsSounds.CAMEL_HUSK_AMBIENT;
	}

	@Override
	public boolean canMate(Animal animal) {
		return false;
	}

	@Nullable
	@Override
	public Camel getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return null;
	}

	@Override
	public boolean canFallInLove() {
		return false;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MountsSounds.CAMEL_HUSK_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return MountsSounds.CAMEL_HUSK_HURT;
	}

	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {
		if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
			this.playSound(MountsSounds.CAMEL_HUSK_STEP_SAND, 0.4F, 1.0F);
		} else {
			this.playSound(MountsSounds.CAMEL_HUSK_STEP, 0.4F, 1.0F);
		}
	}

	@Override
	protected SoundEvent getEatingSound() {
		return MountsSounds.CAMEL_HUSK_EAT;
	}

	@Override
	protected Holder<SoundEvent> getEquipSound(EquipmentSlot equipmentSlot, ItemStack itemStack, Equippable equippable) {
		return equipmentSlot == EquipmentSlot.SADDLE ? MountsSounds.CAMEL_HUSK_SADDLE : super.getEquipSound(equipmentSlot, itemStack, equippable);
	}

	@Override
	public float chargeSpeedModifier() {
		return 4.0F;
	}
}
