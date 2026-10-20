package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyAnimationStates;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.world.entity.AnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/** Render-state copies of the 26.1 axolotl and rabbit animation states. */
@Mixin({AxolotlRenderState.class, RabbitRenderState.class})
public abstract class TinyRenderStateMixin implements TinyAnimationStates {
	@Unique
	private final AnimationState[] everythingBackport$animationStates = TinyAnimationStates.create(
		Math.max(TinyAnimationStates.AXOLOTL_COUNT, TinyAnimationStates.RABBIT_COUNT)
	);

	@Override
	public AnimationState[] everythingBackport$animationStates() {
		return this.everythingBackport$animationStates;
	}
}
