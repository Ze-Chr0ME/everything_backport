package backport.everything_backport.client.mixin;

import backport.everything_backport.client.mountsofmayhem.SpearRenderState;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ArmedEntityRenderState.class)
public abstract class ArmedEntityRenderStateMixin implements SpearRenderState {
	@Unique
	private SwingAnimationType everythingBackport$swingAnimationType = SwingAnimationType.WHACK;
	@Unique
	private ItemStack everythingBackport$rightHandItemStack = ItemStack.EMPTY;
	@Unique
	private ItemStack everythingBackport$leftHandItemStack = ItemStack.EMPTY;
	@Unique
	private float everythingBackport$ticksSinceKineticHitFeedback;
	@Unique
	private float everythingBackport$ticksUsingItem;
	@Unique
	private boolean everythingBackport$spearRightArm;
	@Unique
	private boolean everythingBackport$spearLeftArm;
	@Unique
	private boolean everythingBackport$undead;

	@Override
	public SwingAnimationType everythingBackport$getSwingAnimationType() {
		return this.everythingBackport$swingAnimationType;
	}

	@Override
	public void everythingBackport$setSwingAnimationType(SwingAnimationType type) {
		this.everythingBackport$swingAnimationType = type;
	}

	@Override
	public ItemStack everythingBackport$getHandItemStack(HumanoidArm arm) {
		return arm == HumanoidArm.RIGHT ? this.everythingBackport$rightHandItemStack : this.everythingBackport$leftHandItemStack;
	}

	@Override
	public void everythingBackport$setHandItemStacks(ItemStack right, ItemStack left) {
		this.everythingBackport$rightHandItemStack = right;
		this.everythingBackport$leftHandItemStack = left;
	}

	@Override
	public float everythingBackport$getTicksSinceKineticHitFeedback() {
		return this.everythingBackport$ticksSinceKineticHitFeedback;
	}

	@Override
	public void everythingBackport$setTicksSinceKineticHitFeedback(float ticks) {
		this.everythingBackport$ticksSinceKineticHitFeedback = ticks;
	}

	@Override
	public float everythingBackport$getTicksUsingItem() {
		return this.everythingBackport$ticksUsingItem;
	}

	@Override
	public void everythingBackport$setTicksUsingItem(float ticks) {
		this.everythingBackport$ticksUsingItem = ticks;
	}

	@Override
	public boolean everythingBackport$isSpearArm(HumanoidArm arm) {
		return arm == HumanoidArm.RIGHT ? this.everythingBackport$spearRightArm : this.everythingBackport$spearLeftArm;
	}

	@Override
	public void everythingBackport$setSpearArm(HumanoidArm arm, boolean spear) {
		if (arm == HumanoidArm.RIGHT) {
			this.everythingBackport$spearRightArm = spear;
		} else {
			this.everythingBackport$spearLeftArm = spear;
		}
	}

	@Override
	public boolean everythingBackport$isUndead() {
		return this.everythingBackport$undead;
	}

	@Override
	public void everythingBackport$setUndead(boolean undead) {
		this.everythingBackport$undead = undead;
	}
}
