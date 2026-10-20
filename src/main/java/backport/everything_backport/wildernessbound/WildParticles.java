package backport.everything_backport.wildernessbound;

import backport.everything_backport.registry.Reg;
import net.minecraft.core.particles.SimpleParticleType;

public final class WildParticles {
	public static final SimpleParticleType RED_POPLAR_LEAVES = Reg.particle("red_poplar_leaves", false);
	public static final SimpleParticleType ORANGE_POPLAR_LEAVES = Reg.particle("orange_poplar_leaves", false);
	public static final SimpleParticleType YELLOW_POPLAR_LEAVES = Reg.particle("yellow_poplar_leaves", false);

	private WildParticles() {
	}

	static void init() {
	}
}
