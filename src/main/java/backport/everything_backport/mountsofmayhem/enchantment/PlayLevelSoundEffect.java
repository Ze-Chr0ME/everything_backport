package backport.everything_backport.mountsofmayhem.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * 1.21.11's {@code minecraft:play_sound}, which takes one sound per enchantment level. The 1.21.7 effect only takes a
 * single sound, so the Lunge enchantment uses this effect instead.
 */
public record PlayLevelSoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements EnchantmentEntityEffect {
	public static final MapCodec<PlayLevelSoundEffect> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255)).fieldOf("sound").forGetter(PlayLevelSoundEffect::soundEvents),
				FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlayLevelSoundEffect::volume),
				FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlayLevelSoundEffect::pitch)
			)
			.apply(instance, PlayLevelSoundEffect::new)
	);

	@Override
	public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse item, Entity entity, Vec3 origin) {
		if (!entity.isSilent() && !this.soundEvents.isEmpty()) {
			int index = Mth.clamp(level - 1, 0, this.soundEvents.size() - 1);
			serverLevel.playSound(
				null,
				origin.x(),
				origin.y(),
				origin.z(),
				this.soundEvents.get(index),
				entity.getSoundSource(),
				this.volume.sample(entity.getRandom()),
				this.pitch.sample(entity.getRandom())
			);
		}
	}

	@Override
	public MapCodec<PlayLevelSoundEffect> codec() {
		return CODEC;
	}
}
