package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyAnimationStates;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/** 26.1's redesigned rabbits hop and tilt their heads with keyframe animations, run on the client. */
@Mixin(Rabbit.class)
public abstract class RabbitAnimationMixin extends Animal implements TinyAnimationStates {
	@Unique
	private final AnimationState[] everythingBackport$animationStates = TinyAnimationStates.create(RABBIT_COUNT);
	@Unique
	private int everythingBackport$idleAnimationTimeout = this.random.nextInt(40) + 180;

	@Shadow
	private int jumpTicks;

	protected RabbitAnimationMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.level().isClientSide()) {
			AnimationState hop = this.everythingBackport$animationStates[RABBIT_HOP];
			AnimationState idleHeadTilt = this.everythingBackport$animationStates[RABBIT_IDLE_HEAD_TILT];
			if (this.everythingBackport$idleAnimationTimeout <= 0 && !this.isLeashed() && !this.isNoAi()) {
				this.everythingBackport$idleAnimationTimeout = this.random.nextInt(40) + 180;
				idleHeadTilt.start(this.tickCount);
			} else if (this.jumpTicks > 0) {
				hop.startIfStopped(this.tickCount);
				idleHeadTilt.stop();
			} else {
				this.everythingBackport$idleAnimationTimeout--;
				hop.stop();
			}
		}
	}

	@Override
	public void setLeashData(@Nullable Leashable.LeashData leashData) {
		super.setLeashData(leashData);
		this.everythingBackport$animationStates[RABBIT_IDLE_HEAD_TILT].stop();
	}

	@Override
	public AnimationState[] everythingBackport$animationStates() {
		return this.everythingBackport$animationStates;
	}
}
