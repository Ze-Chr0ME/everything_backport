package backport.everything_backport.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.2 gives every living entity its bounciness, friction, air drag and name tag distance attributes. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributesMixin {
	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void everythingBackport$addChaosCubedAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue()
			.add(ChaosAttributes.BOUNCINESS)
			.add(ChaosAttributes.AIR_DRAG_MODIFIER)
			.add(ChaosAttributes.FRICTION_MODIFIER)
			.add(ChaosAttributes.NAME_TAG_DISTANCE)
			.add(ChaosAttributes.BELOW_NAME_DISTANCE);
	}
}
