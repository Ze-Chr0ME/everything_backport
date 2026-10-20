package backport.everything_backport.tinytakeover;

import net.minecraft.world.entity.AnimationState;

/**
 * Keyframe animation states 26.1 added to entities that 1.21.7 animates procedurally (baby axolotls, rabbits). The
 * entity ticks them on the client and its render state copies them, in the order of the constants on each user.
 */
public interface TinyAnimationStates {
	AnimationState[] everythingBackport$animationStates();

	int AXOLOTL_SWIM = 0;
	int AXOLOTL_WALK = 1;
	int AXOLOTL_WALK_UNDER_WATER = 2;
	int AXOLOTL_IDLE_UNDER_WATER = 3;
	int AXOLOTL_IDLE_UNDER_WATER_ON_GROUND = 4;
	int AXOLOTL_IDLE_ON_GROUND = 5;
	int AXOLOTL_PLAY_DEAD = 6;
	int AXOLOTL_COUNT = 7;

	int RABBIT_HOP = 0;
	int RABBIT_IDLE_HEAD_TILT = 1;
	int RABBIT_COUNT = 2;

	static AnimationState[] create(int count) {
		AnimationState[] states = new AnimationState[count];
		for (int i = 0; i < count; i++) {
			states[i] = new AnimationState();
		}

		return states;
	}

	/** Copies every state from an entity into its render state. */
	static void copy(Object from, Object to) {
		if (from instanceof TinyAnimationStates source && to instanceof TinyAnimationStates target) {
			AnimationState[] sourceStates = source.everythingBackport$animationStates();
			AnimationState[] targetStates = target.everythingBackport$animationStates();
			for (int i = 0; i < sourceStates.length; i++) {
				targetStates[i].copyFrom(sourceStates[i]);
			}
		}
	}
}
