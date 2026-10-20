package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import backport.everything_backport.chaoscubed.entity.SulfurCube;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** 26.2's {@code travelInAir}: the friction and air drag modifier attributes scale how much speed ground and air take away. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityTravelMixin {
	@ModifyExpressionValue(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
	private float everythingBackport$modifyBlockFriction(float friction) {
		return ChaosAttributes.computeModifiedFriction(friction, (float)ChaosAttributes.get((LivingEntity)(Object)this, ChaosAttributes.FRICTION_MODIFIER));
	}

	@ModifyConstant(method = "travelInAir", constant = @Constant(floatValue = 0.91F))
	private float everythingBackport$modifyAirDrag(float airDrag) {
		return ChaosAttributes.computeModifiedFriction(airDrag, (float)ChaosAttributes.get((LivingEntity)(Object)this, ChaosAttributes.AIR_DRAG_MODIFIER));
	}

	/** A cube holding a block falls with the same drag it moves with, as 26.2's omnidirectional air movers do. */
	@ModifyConstant(method = "travelInAir", constant = @Constant(floatValue = 0.98F))
	private float everythingBackport$modifyVerticalAirDrag(float verticalDrag) {
		LivingEntity self = (LivingEntity)(Object)this;
		float modifier = (float)ChaosAttributes.get(self, ChaosAttributes.AIR_DRAG_MODIFIER);
		boolean omnidirectional = self instanceof SulfurCube cube && cube.omnidirectionalAirMover();
		return ChaosAttributes.computeModifiedFriction(omnidirectional ? 0.91F : verticalDrag, modifier);
	}
}
