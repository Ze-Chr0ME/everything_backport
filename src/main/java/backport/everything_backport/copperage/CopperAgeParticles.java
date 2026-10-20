package backport.everything_backport.copperage;

import backport.everything_backport.registry.Reg;
import net.minecraft.core.particles.SimpleParticleType;

public final class CopperAgeParticles {
	public static final SimpleParticleType COPPER_FIRE_FLAME = Reg.particle("copper_fire_flame", false);

	private CopperAgeParticles() {
	}

	static void init() {
	}
}
