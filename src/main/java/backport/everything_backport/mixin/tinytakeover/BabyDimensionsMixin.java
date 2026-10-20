package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.BabyDimensions;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.1 resizes the hitboxes of the redesigned babies. Every class that picks its own baby size in 1.21.7 is hooked, plus
 * {@code LivingEntity} for the mobs that only got one in 26.1.
 */
@Mixin({
	LivingEntity.class, Zombie.class, ZombifiedPiglin.class, Piglin.class, Chicken.class, Fox.class, Horse.class, SkeletonHorse.class,
	ZombieHorse.class, AbstractChestedHorse.class, Llama.class
})
public abstract class BabyDimensionsMixin {
	@Inject(method = "getDefaultDimensions", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$babyDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
		LivingEntity self = (LivingEntity)(Object)this;
		if (self.isBaby()) {
			EntityDimensions dimensions = BabyDimensions.forBaby(self.getType());
			if (dimensions != null) {
				cir.setReturnValue(dimensions);
			}
		}
	}
}
