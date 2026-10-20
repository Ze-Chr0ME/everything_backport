package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** A charged kinetic weapon damages what it runs into every tick (1.21.11). */
@Mixin(ItemStack.class)
public abstract class SpearItemStackMixin {
	@WrapOperation(
		method = "onUseTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/Item;onUseTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;I)V"
		)
	)
	private void everythingBackport$kineticDamage(Item item, Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingTicks, Operation<Void> original) {
		KineticWeapon kineticWeapon = itemStack.get(MountsComponents.KINETIC_WEAPON);
		if (kineticWeapon != null && !level.isClientSide()) {
			kineticWeapon.damageEntities(itemStack, remainingTicks, livingEntity, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));
		} else {
			original.call(item, level, livingEntity, itemStack, remainingTicks);
		}
	}
}
