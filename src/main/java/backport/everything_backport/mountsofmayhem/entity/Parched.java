package backport.everything_backport.mountsofmayhem.entity;

import backport.everything_backport.mountsofmayhem.MountsSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** A desert skeleton whose arrows inflict Weakness (1.21.11). It does not burn in daylight. */
public class Parched extends AbstractSkeleton {
	public Parched(EntityType<? extends AbstractSkeleton> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected AbstractArrow getArrow(ItemStack itemStack, float velocity, @Nullable ItemStack weapon) {
		AbstractArrow arrow = super.getArrow(itemStack, velocity, weapon);
		if (arrow instanceof Arrow plainArrow) {
			plainArrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
		}

		return arrow;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0);
	}

	@Override
	protected boolean isSunBurnTick() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return MountsSounds.PARCHED_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return MountsSounds.PARCHED_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return MountsSounds.PARCHED_DEATH;
	}

	@Override
	public SoundEvent getStepSound() {
		return MountsSounds.PARCHED_STEP;
	}

	@Override
	protected int getHardAttackInterval() {
		return 50;
	}

	@Override
	protected int getAttackInterval() {
		return 70;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance mobEffectInstance) {
		return mobEffectInstance.getEffect() == MobEffects.WEAKNESS ? false : super.canBeAffected(mobEffectInstance);
	}
}
