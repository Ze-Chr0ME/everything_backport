package backport.everything_backport.client.chaoscubed.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class NoxiousGasParticle extends BaseAshSmokeParticle {
	private final float fadeOutStartingPoint;

	protected NoxiousGasParticle(
		ClientLevel level,
		double x,
		double y,
		double z,
		double xa,
		double ya,
		double za,
		float scale,
		SpriteSet sprites
	) {
		super(level, x, y, z, 0.1F, 0.1F, 0.1F, xa, ya, za, scale, sprites, 0.3F, 5, -0.02F, true);
		this.rCol = 1.0F;
		this.gCol = 1.0F;
		this.bCol = 1.0F;
		this.lifetime = (int)(6.0 / (this.random.nextFloat() * 0.5 + 0.5) * scale);
		this.fadeOutStartingPoint = this.lifetime / 2.0F;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age > this.fadeOutStartingPoint) {
			float framesSinceFadeOutStart = this.age - this.fadeOutStartingPoint;
			this.setAlpha((this.lifetime - framesSinceFadeOutStart) / this.lifetime);
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(
			SimpleParticleType options,
			ClientLevel level,
			double x,
			double y,
			double z,
			double xAux,
			double yAux,
			double zAux
		) {
			return new NoxiousGasParticle(level, x, y, z, xAux, yAux, zAux, 3.0F, this.sprites);
		}
	}
}
