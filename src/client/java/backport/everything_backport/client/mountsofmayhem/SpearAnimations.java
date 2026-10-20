package backport.everything_backport.client.mountsofmayhem;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

/** 1.21.11's spear animations ({@code net.minecraft.client.model.effects.SpearAnimations}). */
public final class SpearAnimations {
	private SpearAnimations() {
	}

	static float progress(float value, float from, float to) {
		return Mth.clamp(Mth.inverseLerp(value, from, to), 0.0F, 1.0F);
	}

	public static void thirdPersonHandUse(ModelPart arm, ModelPart head, boolean rightArm, ItemStack itemStack, HumanoidRenderState state) {
		int side = rightArm ? 1 : -1;
		arm.yRot = -0.1F * side + head.yRot;
		arm.xRot = (float)(-Math.PI / 2) + head.xRot + 0.8F;
		if (state.isFallFlying || state.swimAmount > 0.0F) {
			arm.xRot -= 0.9599311F;
		}

		arm.yRot = (float)(Math.PI / 180.0) * Math.clamp((180.0F / (float)Math.PI) * arm.yRot, -60.0F, 60.0F);
		arm.xRot = (float)(Math.PI / 180.0) * Math.clamp((180.0F / (float)Math.PI) * arm.xRot, -120.0F, 30.0F);
		float ticksUsingItem = SpearRenderState.of(state).everythingBackport$getTicksUsingItem();
		if (!(ticksUsingItem <= 0.0F) && (!state.isUsingItem || state.useItemHand == (rightArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))) {
			KineticWeapon kineticWeapon = itemStack.get(MountsComponents.KINETIC_WEAPON);
			if (kineticWeapon != null) {
				UseParams params = UseParams.fromKineticWeapon(kineticWeapon, ticksUsingItem);
				arm.yRot = arm.yRot + -side * params.swayScaleFast() * (float)(Math.PI / 180.0) * params.swayIntensity() * 1.0F;
				arm.zRot = arm.zRot + -side * params.swayScaleSlow() * (float)(Math.PI / 180.0) * params.swayIntensity() * 0.5F;
				arm.xRot = arm.xRot
					+ (float)(Math.PI / 180.0)
						* (
							-40.0F * params.raiseProgressStart()
								+ 30.0F * params.raiseProgressMiddle()
								+ -20.0F * params.raiseProgressEnd()
								+ 20.0F * params.lowerProgress()
								+ 10.0F * params.raiseBackProgress()
								+ 0.6F * params.swayScaleSlow() * params.swayIntensity()
						);
			}
		}
	}

	public static void thirdPersonUseItem(ArmedEntityRenderState state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack itemStack) {
		KineticWeapon kineticWeapon = itemStack.get(MountsComponents.KINETIC_WEAPON);
		if (kineticWeapon != null && ticksUsingItem != 0.0F) {
			float attackTime = attackTime(state);
			float swing = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
			float recover = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
			UseParams params = UseParams.fromKineticWeapon(kineticWeapon, ticksUsingItem);
			int side = arm == HumanoidArm.RIGHT ? 1 : -1;
			float raise = 1.0F - Ease.outBack(1.0F - params.raiseProgress());
			float feedback = hitFeedbackAmount(SpearRenderState.of(state).everythingBackport$getTicksSinceKineticHitFeedback());
			poseStack.translate(0.0, -feedback * 0.4, (double)(-kineticWeapon.forwardMovement() * (raise - params.raiseBackProgress()) + feedback));
			poseStack.rotateAround(
				Axis.XN.rotationDegrees(70.0F * (params.raiseProgress() - params.raiseBackProgress()) - 40.0F * (swing - recover)), 0.0F, -0.03125F, 0.125F
			);
			poseStack.rotateAround(
				Axis.YP.rotationDegrees(side * 90 * (params.raiseProgress() - params.swayProgress() + 3.0F * recover + swing)), 0.0F, 0.0F, 0.125F
			);
		}
	}

	public static <T extends HumanoidRenderState> void thirdPersonAttackHand(HumanoidModel<T> model, T state) {
		float attackTime = state.attackTime;
		HumanoidArm arm = state.attackArm;
		model.rightArm.yRot = model.rightArm.yRot - model.body.yRot;
		model.leftArm.yRot = model.leftArm.yRot - model.body.yRot;
		model.leftArm.xRot = model.leftArm.xRot - model.body.yRot;
		float windUp = Ease.inOutSine(progress(attackTime, 0.0F, 0.05F));
		float thrust = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
		float recover = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
		(arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm).xRot += (90.0F * windUp - 120.0F * thrust + 30.0F * recover) * (float)(Math.PI / 180.0);
	}

	public static void thirdPersonAttackItem(ArmedEntityRenderState state, PoseStack poseStack) {
		float attackTime = attackTime(state);
		if (!(attackTime <= 0.0F)) {
			SpearRenderState spearState = SpearRenderState.of(state);
			KineticWeapon kineticWeapon = spearState.everythingBackport$getHandItemStack(state.mainArm).get(MountsComponents.KINETIC_WEAPON);
			float forward = kineticWeapon != null ? kineticWeapon.forwardMovement() : 0.0F;
			float thrust = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
			float recover = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
			poseStack.rotateAround(Axis.XN.rotationDegrees(70.0F * (thrust - recover)), 0.0F, -0.125F, 0.125F);
			poseStack.translate(0.0F, forward * (thrust - recover), 0.0F);
		}
	}

	private static float hitFeedbackAmount(float ticks) {
		return 0.4F * (Ease.outQuart(progress(ticks, 1.0F, 3.0F)) - Ease.inOutSine(progress(ticks, 3.0F, 10.0F)));
	}

	public static void firstPersonUse(float ticksSinceHitFeedback, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack itemStack) {
		KineticWeapon kineticWeapon = itemStack.get(MountsComponents.KINETIC_WEAPON);
		if (kineticWeapon != null) {
			UseParams params = UseParams.fromKineticWeapon(kineticWeapon, ticksUsingItem);
			int side = arm == HumanoidArm.RIGHT ? 1 : -1;
			poseStack.translate(
				(double)(side * (params.raiseProgress() * 0.15F + params.raiseProgressEnd() * -0.05F + params.swayProgress() * -0.1F + params.swayScaleSlow() * 0.005F)),
				(double)(params.raiseProgress() * -0.075F + params.raiseProgressMiddle() * 0.075F + params.swayScaleFast() * 0.01F),
				params.raiseProgressStart() * 0.05 + params.raiseProgressEnd() * -0.05 + params.swayScaleSlow() * 0.005F
			);
			poseStack.rotateAround(
				Axis.XP
					.rotationDegrees(
						-65.0F * Ease.inOutBack(params.raiseProgress())
							- 35.0F * params.lowerProgress()
							+ 100.0F * params.raiseBackProgress()
							+ -0.5F * params.swayScaleFast()
					),
				0.0F,
				0.1F,
				0.0F
			);
			poseStack.rotateAround(
				Axis.YN.rotationDegrees(side * (-90.0F * progress(params.raiseProgress(), 0.5F, 0.55F) + 90.0F * params.swayProgress() + 2.0F * params.swayScaleSlow())),
				side * 0.15F,
				0.0F,
				0.0F
			);
			poseStack.translate(0.0F, -hitFeedbackAmount(ticksSinceHitFeedback), 0.0F);
		}
	}

	public static void firstPersonAttack(float attackTime, PoseStack poseStack, int side, HumanoidArm arm) {
		float windUp = Ease.inOutSine(progress(attackTime, 0.0F, 0.05F));
		float thrust = Ease.outBack(progress(attackTime, 0.05F, 0.2F));
		float recover = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
		poseStack.translate(side * 0.1F * (windUp - thrust), -0.075F * (windUp - recover), 0.65F * (windUp - thrust));
		poseStack.mulPose(Axis.XP.rotationDegrees(-70.0F * (windUp - recover)));
		poseStack.translate(0.0, 0.0, -0.25 * (recover - thrust));
	}

	/** 1.21.7 keeps the attack time on humanoid render states only. */
	private static float attackTime(ArmedEntityRenderState state) {
		return state instanceof HumanoidRenderState humanoid ? humanoid.attackTime : 0.0F;
	}

	record UseParams(
		float raiseProgress,
		float raiseProgressStart,
		float raiseProgressMiddle,
		float raiseProgressEnd,
		float swayProgress,
		float lowerProgress,
		float raiseBackProgress,
		float swayIntensity,
		float swayScaleSlow,
		float swayScaleFast
	) {
		static UseParams fromKineticWeapon(KineticWeapon kineticWeapon, float ticks) {
			int delay = kineticWeapon.delayTicks();
			int dismountEnd = kineticWeapon.dismountConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + delay;
			int swayStart = dismountEnd - 20;
			int knockbackEnd = kineticWeapon.knockbackConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + delay;
			int swayEnd = knockbackEnd - 40;
			int damageEnd = kineticWeapon.damageConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + delay;
			float raise = progress(ticks, 0.0F, delay);
			float raiseStart = progress(raise, 0.0F, 0.5F);
			float raiseMiddle = progress(raise, 0.5F, 0.8F);
			float raiseEnd = progress(raise, 0.8F, 1.0F);
			float sway = progress(ticks, swayStart, swayEnd);
			float lower = Ease.outCubic(Ease.inOutElastic(progress(ticks - 20.0F, swayEnd, knockbackEnd)));
			float raiseBack = progress(ticks, damageEnd - 5, damageEnd);
			float intensity = 2.0F * Ease.outCirc(sway) - 2.0F * Ease.inCirc(raiseBack);
			float slow = Mth.sin(ticks * 19.0F * (float)(Math.PI / 180.0)) * intensity;
			float fast = Mth.sin(ticks * 30.0F * (float)(Math.PI / 180.0)) * intensity;
			return new UseParams(raise, raiseStart, raiseMiddle, raiseEnd, sway, lower, raiseBack, intensity, slow, fast);
		}
	}
}
