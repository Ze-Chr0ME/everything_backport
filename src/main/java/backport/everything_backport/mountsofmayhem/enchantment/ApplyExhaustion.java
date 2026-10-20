package backport.everything_backport.mountsofmayhem.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record ApplyExhaustion(LevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<ApplyExhaustion> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(LevelBasedValue.CODEC.fieldOf("amount").forGetter(ApplyExhaustion::amount)).apply(instance, ApplyExhaustion::new)
	);

	@Override
	public void apply(ServerLevel serverLevel, int level, EnchantedItemInUse item, Entity entity, Vec3 origin) {
		if (entity instanceof Player player) {
			player.causeFoodExhaustion(this.amount.calculate(level));
		}
	}

	@Override
	public MapCodec<ApplyExhaustion> codec() {
		return CODEC;
	}
}
