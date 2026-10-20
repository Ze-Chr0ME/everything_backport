package backport.everything_backport.tinytakeover;

import backport.everything_backport.registry.Reg;
import net.minecraft.core.particles.SimpleParticleType;

public final class TinyParticles {
	public static final SimpleParticleType PAUSE_MOB_GROWTH = Reg.particle("pause_mob_growth", false);
	public static final SimpleParticleType RESET_MOB_GROWTH = Reg.particle("reset_mob_growth", false);

	private TinyParticles() {
	}

	static void init() {
	}
}
