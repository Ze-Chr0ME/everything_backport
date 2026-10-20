package backport.everything_backport.client.tinytakeover.model;

import backport.everything_backport.client.tinytakeover.animation.AdultRabbitAnimation;
import backport.everything_backport.client.tinytakeover.animation.BabyRabbitAnimation;
import backport.everything_backport.tinytakeover.TinyAnimationStates;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.world.entity.AnimationState;

/**
 * 26.1's redesigned rabbit, adult or kit, which hops and tilts its head with keyframe animations. The meshes share
 * nothing with 1.21.7's rabbit model, so the renderer gets these set directly.
 */
public class KeyframeRabbitModel extends EntityModel<RabbitRenderState> {
	private final KeyframeAnimation hopAnimation;
	private final KeyframeAnimation idleHeadTiltAnimation;
	private final ModelPart head;

	private KeyframeRabbitModel(ModelPart root, AnimationDefinition hop, AnimationDefinition idleHeadTilt) {
		super(root);
		this.head = root.getChild("body").getChild("head");
		this.hopAnimation = hop.bake(root);
		this.idleHeadTiltAnimation = idleHeadTilt.bake(root);
	}

	public static KeyframeRabbitModel adult(ModelPart root) {
		return new KeyframeRabbitModel(root, AdultRabbitAnimation.HOP, AdultRabbitAnimation.IDLE_HEAD_TILT);
	}

	public static KeyframeRabbitModel baby(ModelPart root) {
		return new KeyframeRabbitModel(root, BabyRabbitAnimation.HOP, BabyRabbitAnimation.IDLE_HEAD_TILT);
	}

	@Override
	public void setupAnim(RabbitRenderState state) {
		super.setupAnim(state);
		AnimationState[] states = ((TinyAnimationStates)state).everythingBackport$animationStates();
		AnimationState idleHeadTilt = states[TinyAnimationStates.RABBIT_IDLE_HEAD_TILT];
		if (!idleHeadTilt.isStarted()) {
			this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
			this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
		}

		this.hopAnimation.apply(states[TinyAnimationStates.RABBIT_HOP], state.ageInTicks);
		this.idleHeadTiltAnimation.apply(idleHeadTilt, state.ageInTicks);
	}
}
