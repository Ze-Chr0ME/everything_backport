package backport.everything_backport.client.chaoscubed.particle;

import backport.everything_backport.chaoscubed.particle.GeyserBaseParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class GeyserBaseParticle extends BaseAshSmokeParticle {
	private static final float BURST_IMPULSE_FACTOR = 0.25F;
	private static final float PARTICLE_SIZE_BASE = 3.0F;
	private static final float PARTICLE_SIZE_FACTOR = 0.125F;
	private static final float SPAWN_OFFSET_Y = 0.2F;
	private static final float RANDOM_SPAWN_SPREAD = 0.5F;
	private static final int MAX_LIFETIME = 25;
	private static final float FRICTION = 0.725F;

	private GeyserBaseParticle(
		ClientLevel level,
		double x,
		double y,
		double z,
		double xAux,
		double yAux,
		double zAux,
		int waterBlocks,
		float burstImpulseBase,
		SpriteSet sprites
	) {
		super(
			level,
			x,
			y,
			z,
			burstImpulse(burstImpulseBase, waterBlocks),
			burstImpulse(burstImpulseBase, waterBlocks),
			burstImpulse(burstImpulseBase, waterBlocks),
			xAux,
			yAux,
			zAux,
			3.0F + 0.125F * waterBlocks,
			sprites,
			0.0F,
			0,
			0.0F,
			true
		);
		this.friction = 0.725F;
		this.rCol = 1.0F;
		this.gCol = 1.0F;
		this.bCol = 1.0F;
		this.yd = Math.abs(this.yd);
		float lifetimeFactor = 0.8F + 0.2F * level.getRandom().nextFloat();
		this.lifetime = (int)(25.0F * lifetimeFactor);
	}

	private static float burstImpulse(float burstImpulseBase, int waterBlocks) {
		return burstImpulseBase + 0.25F * waterBlocks;
	}

	public static class Provider implements ParticleProvider<GeyserBaseParticleOptions> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(
			GeyserBaseParticleOptions options,
			ClientLevel level,
			double x,
			double y,
			double z,
			double xAux,
			double yAux,
			double zAux
		) {
			double randomX = x + (level.random.nextFloat() - 0.5F) * 0.5F;
			double randomY = y + (level.random.nextFloat() - 0.5F) * 0.5F + 0.2F;
			double randomZ = z + (level.random.nextFloat() - 0.5F) * 0.5F;
			return new GeyserBaseParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options.waterBlocks(), options.burstImpulseBase(), this.sprites);
		}
	}
}
