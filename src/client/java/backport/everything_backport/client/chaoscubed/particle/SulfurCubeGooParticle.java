package backport.everything_backport.client.chaoscubed.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * 26.2's sulfur cube goo: a {@code BreakingItemParticle} showing a corner of the goo sprite, as slime and snowball bits do. 1.21.7's
 * breaking particles only take item models, so this rebuilds one on the particle atlas.
 */
public class SulfurCubeGooParticle extends TextureSheetParticle {
	private final float uo;
	private final float vo;

	private SulfurCubeGooParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
		super(level, x, y, z, 0.0, 0.0, 0.0);
		this.setSprite(sprite);
		this.gravity = 1.0F;
		this.quadSize /= 2.0F;
		this.uo = this.random.nextFloat() * 3.0F;
		this.vo = this.random.nextFloat() * 3.0F;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	protected float getU0() {
		return this.sprite.getU((this.uo + 1.0F) / 4.0F);
	}

	@Override
	protected float getU1() {
		return this.sprite.getU(this.uo / 4.0F);
	}

	@Override
	protected float getV0() {
		return this.sprite.getV(this.vo / 4.0F);
	}

	@Override
	protected float getV1() {
		return this.sprite.getV((this.vo + 1.0F) / 4.0F);
	}

	public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
			return new SulfurCubeGooParticle(level, x, y, z, this.sprites.get(0, 1));
		}
	}
}
