package backport.everything_backport.client.tinytakeover.model;

import backport.everything_backport.client.tinytakeover.animation.BabyAxolotlAnimation;
import backport.everything_backport.tinytakeover.TinyAnimationStates;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
import net.minecraft.world.entity.AnimationState;

/**
 * 26.1's baby axolotl, animated entirely by keyframes. It shares no parts with the 1.21.7 axolotl model, so it is set
 * on the renderer directly instead of being built as one.
 */
public class BabyAxolotlModel extends EntityModel<AxolotlRenderState> {
	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation walkUnderwaterAnimation;
	private final KeyframeAnimation swimAnimation;
	private final KeyframeAnimation idleOnGroundAnimation;
	private final KeyframeAnimation idleOnGroundUnderWaterAnimation;
	private final KeyframeAnimation idleUnderWaterAnimation;
	private final KeyframeAnimation playDeadAnimation;

	public BabyAxolotlModel(ModelPart root) {
		super(root);
		this.swimAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_SWIM.bake(root);
		this.walkAnimation = BabyAxolotlAnimation.AXOLOTL_WALK_FLOOR.bake(root);
		this.walkUnderwaterAnimation = BabyAxolotlAnimation.WALK_FLOOR_UNDERWATER.bake(root);
		this.idleUnderWaterAnimation = BabyAxolotlAnimation.IDLE_UNDERWATER.bake(root);
		this.idleOnGroundUnderWaterAnimation = BabyAxolotlAnimation.IDLE_FLOOR_UNDERWATER.bake(root);
		this.idleOnGroundAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_IDLE_FLOOR.bake(root);
		this.playDeadAnimation = BabyAxolotlAnimation.BABY_AXOLOTL_PLAY_DEAD.bake(root);
	}

	@Override
	public void setupAnim(AxolotlRenderState state) {
		super.setupAnim(state);
		AnimationState[] states = ((TinyAnimationStates)state).everythingBackport$animationStates();
		AnimationState walk = states[TinyAnimationStates.AXOLOTL_WALK];
		if (walk.isStarted()) {
			this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 15.0F, 30.0F);
		}

		this.swimAnimation.apply(states[TinyAnimationStates.AXOLOTL_SWIM], state.ageInTicks);
		// 26.1 drives the underwater walk from the land walk state too.
		this.walkUnderwaterAnimation.apply(walk, state.ageInTicks);
		this.idleOnGroundAnimation.apply(states[TinyAnimationStates.AXOLOTL_IDLE_ON_GROUND], state.ageInTicks);
		this.idleUnderWaterAnimation.apply(states[TinyAnimationStates.AXOLOTL_IDLE_UNDER_WATER], state.ageInTicks);
		this.idleOnGroundUnderWaterAnimation.apply(states[TinyAnimationStates.AXOLOTL_IDLE_UNDER_WATER_ON_GROUND], state.ageInTicks);
		this.playDeadAnimation.apply(states[TinyAnimationStates.AXOLOTL_PLAY_DEAD], state.ageInTicks);
	}
}
