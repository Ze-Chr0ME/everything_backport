package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinyAnimationStates;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.BinaryAnimator;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/** 26.1 animates baby axolotls with keyframes, picked each client tick from what the axolotl is doing. */
@Mixin(Axolotl.class)
public abstract class AxolotlBabyAnimationMixin extends Animal implements TinyAnimationStates {
	@Unique
	private final AnimationState[] everythingBackport$animationStates = TinyAnimationStates.create(AXOLOTL_COUNT);

	@Shadow
	@Final
	public BinaryAnimator movingAnimator;

	protected AxolotlBabyAnimationMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Shadow
	public abstract boolean isPlayingDead();

	@WrapOperation(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;tickAnimations()V"))
	private void everythingBackport$tickBabyAnimations(Axolotl self, Operation<Void> original) {
		if (!this.isBaby()) {
			original.call(self);
			return;
		}

		boolean inWater = this.isInWater();
		boolean onGround = this.onGround();
		boolean moving = this.walkAnimation.isMoving() || this.getXRot() != this.xRotO || this.getYRot() != this.yRotO;
		this.movingAnimator.tick(moving);
		if (this.isPlayingDead()) {
			this.everythingBackport$solo(AXOLOTL_PLAY_DEAD);
		} else if (moving) {
			if (inWater && !onGround) {
				this.everythingBackport$solo(AXOLOTL_SWIM);
			} else if (!inWater && onGround) {
				this.everythingBackport$solo(AXOLOTL_WALK);
			} else {
				this.everythingBackport$solo(AXOLOTL_WALK_UNDER_WATER);
			}
		} else if (inWater && !onGround) {
			this.everythingBackport$solo(AXOLOTL_IDLE_UNDER_WATER);
		} else if (inWater) {
			this.everythingBackport$solo(AXOLOTL_IDLE_UNDER_WATER_ON_GROUND);
		} else {
			this.everythingBackport$solo(AXOLOTL_IDLE_ON_GROUND);
		}
	}

	@Unique
	private void everythingBackport$solo(int index) {
		for (int i = 0; i < AXOLOTL_COUNT; i++) {
			if (i == index) {
				this.everythingBackport$animationStates[i].startIfStopped(this.tickCount);
			} else {
				this.everythingBackport$animationStates[i].stop();
			}
		}
	}

	@Override
	public AnimationState[] everythingBackport$animationStates() {
		return this.everythingBackport$animationStates;
	}
}
