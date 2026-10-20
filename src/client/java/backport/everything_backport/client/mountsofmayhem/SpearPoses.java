package backport.everything_backport.client.mountsofmayhem;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.MountsTags;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.SwingAnimation;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Picks 1.21.11's spear arm pose. The pose itself is kept as {@code ITEM} (1.21.7 has no {@code SPEAR} constant) and
 * flagged on the render state; HumanoidModelMixin then poses the flagged arm.
 */
public final class SpearPoses {
	private SpearPoses() {
	}

	/** The render state fields every humanoid gets (1.21.11's {@code ArmedEntityRenderState} and {@code HumanoidRenderState}). */
	public static void extract(LivingEntity entity, HumanoidRenderState state, float partialTick) {
		SpearRenderState spearState = SpearRenderState.of(state);
		spearState.everythingBackport$setSwingAnimationType(swingAnimation(entity.getMainHandItem()).type());
		spearState.everythingBackport$setHandItemStacks(entity.getItemHeldByArm(HumanoidArm.RIGHT).copy(), entity.getItemHeldByArm(HumanoidArm.LEFT).copy());
		spearState.everythingBackport$setTicksSinceKineticHitFeedback(((SpearCombat.StabTracker)entity).everythingBackport$getTicksSinceLastKineticHitFeedback(partialTick));
		spearState.everythingBackport$setTicksUsingItem(entity.isUsingItem() ? entity.getTicksUsingItem() + partialTick : 0.0F);
		spearState.everythingBackport$setUndead(entity instanceof Zombie || entity instanceof AbstractSkeleton);
		spearState.everythingBackport$setSpearArm(HumanoidArm.RIGHT, false);
		spearState.everythingBackport$setSpearArm(HumanoidArm.LEFT, false);
	}

	/** 1.21.11's {@code AvatarRenderer#getArmPose}: a spear in the main hand also lowers the off hand. */
	public static void applyToPlayer(Player player, HumanoidRenderState state) {
		HumanoidArm mainArm = player.getMainArm();
		HumanoidArm offArm = mainArm.getOpposite();
		ItemStack offHand = player.getOffhandItem();
		if (isPlayerSpearPose(player, player.getMainHandItem(), InteractionHand.MAIN_HAND)) {
			setSpear(state, mainArm);
			setPose(state, offArm, offHand.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM);
		} else if (!getPose(state, mainArm).isTwoHanded() && isPlayerSpearPose(player, offHand, InteractionHand.OFF_HAND)) {
			setSpear(state, offArm);
		}
	}

	private static boolean isPlayerSpearPose(Player player, ItemStack itemStack, InteractionHand hand) {
		if (itemStack.isEmpty()) {
			return false;
		} else if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0 && itemStack.has(MountsComponents.KINETIC_WEAPON)) {
			return true;
		} else {
			return swingAnimation(itemStack).type() == SwingAnimationType.STAB && player.swinging || itemStack.is(MountsTags.Items.SPEARS);
		}
	}

	/** 1.21.11's {@code HumanoidMobRenderer#getArmPose}, and the zombie override that poses by the other hand's item. */
	public static void applyToMob(Mob mob, HumanoidRenderState state, boolean zombie) {
		for (HumanoidArm arm : HumanoidArm.values()) {
			boolean spear;
			if (zombie) {
				spear = swingAnimation(mob.getItemHeldByArm(arm.getOpposite())).type() == SwingAnimationType.STAB;
			} else {
				ItemStack itemStack = mob.getItemHeldByArm(arm);
				spear = swingAnimation(itemStack).type() == SwingAnimationType.STAB && mob.swinging || itemStack.is(MountsTags.Items.SPEARS);
			}

			if (spear) {
				setSpear(state, arm);
			}
		}
	}

	private static void setSpear(HumanoidRenderState state, HumanoidArm arm) {
		SpearRenderState.of(state).everythingBackport$setSpearArm(arm, true);
		setPose(state, arm, HumanoidModel.ArmPose.ITEM);
	}

	private static HumanoidModel.ArmPose getPose(HumanoidRenderState state, HumanoidArm arm) {
		return arm == HumanoidArm.RIGHT ? state.rightArmPose : state.leftArmPose;
	}

	private static void setPose(HumanoidRenderState state, HumanoidArm arm, HumanoidModel.ArmPose pose) {
		if (arm == HumanoidArm.RIGHT) {
			state.rightArmPose = pose;
		} else {
			state.leftArmPose = pose;
		}
	}

	public static SwingAnimation swingAnimation(ItemStack itemStack) {
		return itemStack.getOrDefault(MountsComponents.SWING_ANIMATION, SwingAnimation.DEFAULT);
	}
}
