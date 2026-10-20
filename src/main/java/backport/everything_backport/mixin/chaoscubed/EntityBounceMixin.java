package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import backport.everything_backport.chaoscubed.ChaosCubed;
import backport.everything_backport.chaoscubed.ChaosTags;
import backport.everything_backport.chaoscubed.entity.SulfurCube;
import backport.everything_backport.wildernessbound.block.ShelfMushroomBlock;
import backport.everything_backport.wildernessbound.block.StrawBedBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 26.2's {@code Entity.restituteMovementAfterCollisions} for living entities with a bounciness: instead of stopping on the axes they
 * hit, they bounce back. Entities with no bounciness keep 1.21.7's collision handling, slime blocks and beds included.
 */
@Mixin(Entity.class)
public abstract class EntityBounceMixin {
	@Unique
	private Vec3 everythingBackport$requestedMovement = Vec3.ZERO;
	@Unique
	private Vec3 everythingBackport$collidedMovement = Vec3.ZERO;
	@Unique
	private Vec3 everythingBackport$movementBeforeCollision = Vec3.ZERO;

	@WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 everythingBackport$captureCollision(Entity self, Vec3 delta, Operation<Vec3> original) {
		Vec3 movement = original.call(self, delta);
		this.everythingBackport$requestedMovement = delta;
		this.everythingBackport$collidedMovement = movement;
		this.everythingBackport$movementBeforeCollision = self.getDeltaMovement();
		return movement;
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getMovementEmission()Lnet/minecraft/world/entity/Entity$MovementEmission;"))
	private void everythingBackport$bounce(MoverType moverType, Vec3 vec3, CallbackInfo ci) {
		Entity self = (Entity)(Object)this;
		if (!(self instanceof LivingEntity living) || !self.canSimulateMovement()) {
			return;
		}

		double bounciness = ChaosAttributes.get(living, ChaosAttributes.BOUNCINESS);
		if (bounciness <= 0.0) {
			return;
		}

		Vec3 delta = this.everythingBackport$requestedMovement;
		Vec3 movement = this.everythingBackport$collidedMovement;
		boolean xCollision = !Mth.equal(delta.x, movement.x);
		boolean zCollision = !Mth.equal(delta.z, movement.z);
		boolean movedVertically = Math.abs(delta.y) > 0.0;
		if (!(movedVertically && self.verticalCollision || self.horizontalCollision)) {
			return;
		}

		BlockState effectState = self.level().getBlockState(self.getOnPosLegacy());
		double gravity = self.getGravity();
		double restitution = self.isShiftKeyDown() ? 0.0 : bounciness;
		Vec3 currentMovement = this.everythingBackport$movementBeforeCollision;
		Vec3 movementAfterBounce = currentMovement;
		if (xCollision) {
			movementAfterBounce = movementAfterBounce.with(Direction.Axis.X, -currentMovement.x * restitution);
		}

		if (zCollision) {
			movementAfterBounce = movementAfterBounce.with(Direction.Axis.Z, -currentMovement.z * restitution);
		}

		boolean bounced = restitution > 0.0 && (xCollision || zCollision);
		if (self.verticalCollision) {
			if (self.verticalCollisionBelow) {
				restitution = !(-currentMovement.y < gravity) && !self.isShiftKeyDown() && !effectState.is(ChaosTags.SUPPRESSES_BOUNCE)
					? Math.max(restitution, blockBounciness(effectState))
					: 0.0;
			}

			double gravityCompensation;
			double effectiveDrag;
			if (restitution > 0.0) {
				double portionWithMovement = movement.y / currentMovement.y;
				gravityCompensation = portionWithMovement * gravity;
				effectiveDrag = Mth.lerp(portionWithMovement, 1.0, airDrag(living));
				bounced = true;
			} else {
				gravityCompensation = 0.0;
				effectiveDrag = 1.0;
			}

			movementAfterBounce = movementAfterBounce.with(Direction.Axis.Y, (gravityCompensation - currentMovement.y) * effectiveDrag * restitution);
		}

		if (bounced) {
			self.gameEvent(ChaosCubed.BOUNCE_EVENT);
			self.hasImpulse = true;
		}

		self.setDeltaMovement(movementAfterBounce);
	}

	/** 26.3's block restitution: slime blocks bounce fully, beds and shelf mushrooms three quarters, straw beds not at all. */
	@Unique
	private static double blockBounciness(BlockState state) {
		if (state.is(Blocks.SLIME_BLOCK)) {
			return 1.0;
		} else if (state.getBlock() instanceof ShelfMushroomBlock) {
			return ShelfMushroomBlock.BOUNCE_RESTITUTION;
		} else {
			return state.getBlock() instanceof BedBlock && !(state.getBlock() instanceof StrawBedBlock) ? 0.75 : 0.0;
		}
	}

	/** 26.2's {@code LivingEntity.getAirDrag}. */
	@Unique
	private static double airDrag(LivingEntity living) {
		boolean omnidirectional = living instanceof SulfurCube cube && cube.omnidirectionalAirMover();
		return ChaosAttributes.computeModifiedFriction(omnidirectional ? 0.91F : 0.98F, (float)ChaosAttributes.get(living, ChaosAttributes.AIR_DRAG_MODIFIER));
	}
}
