package backport.everything_backport.mountsofmayhem.enchantment;

import backport.everything_backport.EverythingBackport;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * The {@code is_fall_flying} and {@code is_in_water} entity flags 1.21.11 added to {@code minecraft:entity_properties}.
 * 1.21.7's entity flag predicate can't take new fields, so the Lunge enchantment checks them with this condition.
 */
public record EntityFlagsCondition(LootContext.EntityTarget entityTarget, Optional<Boolean> isFallFlying, Optional<Boolean> isInWater)
	implements LootItemCondition {
	public static final MapCodec<EntityFlagsCondition> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(EntityFlagsCondition::entityTarget),
				Codec.BOOL.optionalFieldOf("is_fall_flying").forGetter(EntityFlagsCondition::isFallFlying),
				Codec.BOOL.optionalFieldOf("is_in_water").forGetter(EntityFlagsCondition::isInWater)
			)
			.apply(instance, EntityFlagsCondition::new)
	);
	public static final LootItemConditionType TYPE = Registry.register(
		BuiltInRegistries.LOOT_CONDITION_TYPE, EverythingBackport.id("entity_flags"), new LootItemConditionType(CODEC)
	);

	@Override
	public LootItemConditionType getType() {
		return TYPE;
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Set.of(this.entityTarget.getParam());
	}

	@Override
	public boolean test(LootContext lootContext) {
		Entity entity = lootContext.getOptionalParameter(this.entityTarget.getParam());
		if (entity == null) {
			return false;
		}

		boolean fallFlying = entity instanceof LivingEntity livingEntity && livingEntity.isFallFlying();
		return this.isFallFlying.map(expected -> expected == fallFlying).orElse(true)
			&& this.isInWater.map(expected -> expected == entity.isInWater()).orElse(true);
	}

	static void init() {
	}
}
