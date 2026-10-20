package backport.everything_backport.client.mountsofmayhem;

import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

/**
 * Render state fields 1.21.11 added for spears, attached to {@link ArmedEntityRenderState} by ArmedEntityRenderStateMixin.
 * The spear arm flags stand in for 1.21.11's {@code ArmPose.SPEAR}, which 1.21.7's arm pose enum can't take.
 */
public interface SpearRenderState {
	SwingAnimationType everythingBackport$getSwingAnimationType();

	void everythingBackport$setSwingAnimationType(SwingAnimationType type);

	ItemStack everythingBackport$getHandItemStack(HumanoidArm arm);

	void everythingBackport$setHandItemStacks(ItemStack right, ItemStack left);

	float everythingBackport$getTicksSinceKineticHitFeedback();

	void everythingBackport$setTicksSinceKineticHitFeedback(float ticks);

	/** 1.21.11 interpolates the use ticks; 1.21.7's render state only keeps whole ticks. */
	float everythingBackport$getTicksUsingItem();

	void everythingBackport$setTicksUsingItem(float ticks);

	boolean everythingBackport$isSpearArm(HumanoidArm arm);

	void everythingBackport$setSpearArm(HumanoidArm arm, boolean spear);

	/** Zombies and skeletons pose both arms from their main hand item (1.21.11's {@code UndeadRenderState}). */
	boolean everythingBackport$isUndead();

	void everythingBackport$setUndead(boolean undead);

	static SpearRenderState of(ArmedEntityRenderState state) {
		return (SpearRenderState)state;
	}

	default ItemStack everythingBackport$getUseItemStackForArm(ArmedEntityRenderState state, HumanoidArm arm) {
		return this.everythingBackport$getHandItemStack(this.everythingBackport$isUndead() ? state.mainArm : arm);
	}
}
