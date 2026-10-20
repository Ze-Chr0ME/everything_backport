package backport.everything_backport.client.tinytakeover;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

/** 26.1's golden dandelion particles: a short glint drifting down (growth paused) or up (growth reset). */
public class SimpleVerticalParticle extends TextureSheetParticle {
	private SimpleVerticalParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites, boolean upwards) {
		super(level, x, y, z, xa, ya, za);
		this.pickSprite(sprites);
		this.xd = xa;
		this.zd = za;
		this.yd = ya;
		this.gravity = 0.0F;
		this.yd += upwards ? 0.03 : -0.03;
		this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.5F);
		this.lifetime = 8;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public record PauseMobGrowthProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
			return new SimpleVerticalParticle(level, x, y, z, xa, ya, za, this.sprites, false);
		}
	}

	public record ResetMobGrowthProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
			return new SimpleVerticalParticle(level, x, y, z, xa, ya, za, this.sprites, true);
		}
	}
}
