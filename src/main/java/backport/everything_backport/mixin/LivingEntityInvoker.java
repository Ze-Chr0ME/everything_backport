package backport.everything_backport.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {
	@Invoker("getKnockback")
	float everythingBackport$getKnockback(Entity entity, DamageSource damageSource);

	@Accessor("attackStrengthTicker")
	int everythingBackport$getAttackStrengthTicker();

	@Accessor("attackStrengthTicker")
	void everythingBackport$setAttackStrengthTicker(int ticks);
}
