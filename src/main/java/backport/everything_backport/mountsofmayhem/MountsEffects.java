package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public final class MountsEffects {
	/** Given to nautilus riders: they can't drown, but their air doesn't refill either. */
	public static final Holder<MobEffect> BREATH_OF_THE_NAUTILUS = Registry.registerForHolder(
		BuiltInRegistries.MOB_EFFECT, EverythingBackport.vanilla("breath_of_the_nautilus"), new MobEffect(MobEffectCategory.BENEFICIAL, 65518) {
		}
	);

	private MountsEffects() {
	}

	static void init() {
	}
}
