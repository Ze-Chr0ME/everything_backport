package backport.everything_backport.mixin.chaoscubed;

import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PrimedTnt.class)
public interface PrimedTntAccessor {
	@Accessor("USED_PORTAL_DAMAGE_CALCULATOR")
	static ExplosionDamageCalculator everythingBackport$getUsedPortalDamageCalculator() {
		throw new AssertionError();
	}
}
