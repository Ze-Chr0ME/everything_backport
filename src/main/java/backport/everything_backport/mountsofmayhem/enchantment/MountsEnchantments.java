package backport.everything_backport.mountsofmayhem.enchantment;

import backport.everything_backport.EverythingBackport;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/** The Lunge enchantment's effect component and effects (1.21.11). */
public final class MountsEnchantments {
	public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> POST_PIERCING_ATTACK = Registry.register(
		BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
		EverythingBackport.vanilla("post_piercing_attack"),
		DataComponentType.<List<ConditionalEffect<EnchantmentEntityEffect>>>builder()
			.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf())
			.build()
	);

	private MountsEnchantments() {
	}

	public static void init() {
		registerEntityEffect(EverythingBackport.vanilla("apply_impulse"), ApplyEntityImpulse.CODEC);
		registerEntityEffect(EverythingBackport.vanilla("apply_exhaustion"), ApplyExhaustion.CODEC);
		registerEntityEffect(EverythingBackport.id("play_level_sound"), PlayLevelSoundEffect.CODEC);
	}

	private static void registerEntityEffect(ResourceLocation id, MapCodec<? extends EnchantmentEntityEffect> codec) {
		Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id, codec);
		Registry.register(BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, id, codec);
	}

	/** 1.21.11's {@code EnchantmentHelper#doLungeEffects}, run after a piercing attack. */
	public static void doLungeEffects(ServerLevel serverLevel, LivingEntity entity) {
		ItemStack weapon = entity.getMainHandItem();
		ItemEnchantments enchantments = weapon.get(DataComponents.ENCHANTMENTS);
		if (!weapon.isEmpty() && enchantments != null && !enchantments.isEmpty()) {
			EnchantedItemInUse item = new EnchantedItemInUse(weapon, EquipmentSlot.MAINHAND, entity);

			for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
				Enchantment enchantment = entry.getKey().value();
				int level = entry.getIntValue();
				if (enchantment.matchingSlot(EquipmentSlot.MAINHAND)) {
					Vec3 origin = entity.position();
					LootContext context = new LootContext.Builder(
							new LootParams.Builder(serverLevel)
								.withParameter(LootContextParams.THIS_ENTITY, entity)
								.withParameter(LootContextParams.ENCHANTMENT_LEVEL, level)
								.withParameter(LootContextParams.ORIGIN, origin)
								.create(LootContextParamSets.ENCHANTED_ENTITY)
						)
						.create(Optional.empty());

					for (ConditionalEffect<EnchantmentEntityEffect> effect : enchantment.getEffects(POST_PIERCING_ATTACK)) {
						if (effect.matches(context)) {
							effect.effect().apply(serverLevel, level, item, entity, origin);
						}
					}
				}
			}
		}
	}
}
