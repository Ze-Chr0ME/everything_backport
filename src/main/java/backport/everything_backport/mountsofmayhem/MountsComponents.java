package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import backport.everything_backport.mountsofmayhem.component.PiercingWeapon;
import backport.everything_backport.mountsofmayhem.component.SwingAnimation;
import backport.everything_backport.mountsofmayhem.component.UseEffects;
import backport.everything_backport.mountsofmayhem.entity.ZombieNautilusVariant;
import java.util.function.UnaryOperator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;

/** Item components added in 1.21.11 for spears. */
public final class MountsComponents {
	public static final DataComponentType<UseEffects> USE_EFFECTS = register(
		"use_effects", builder -> builder.persistent(UseEffects.CODEC).networkSynchronized(UseEffects.STREAM_CODEC)
	);
	public static final DataComponentType<Float> MINIMUM_ATTACK_CHARGE = register(
		"minimum_attack_charge", builder -> builder.persistent(ExtraCodecs.floatRange(0.0F, 1.0F)).networkSynchronized(ByteBufCodecs.FLOAT)
	);
	/** Stored as a plain key; 1.21.11 also accepts an inline damage type, which datapacks rarely use. */
	public static final DataComponentType<ResourceKey<DamageType>> DAMAGE_TYPE = register(
		"damage_type", builder -> builder.persistent(ResourceKey.codec(Registries.DAMAGE_TYPE)).networkSynchronized(ResourceKey.streamCodec(Registries.DAMAGE_TYPE))
	);
	public static final DataComponentType<AttackRange> ATTACK_RANGE = register(
		"attack_range", builder -> builder.persistent(AttackRange.CODEC).networkSynchronized(AttackRange.STREAM_CODEC).cacheEncoding()
	);
	public static final DataComponentType<PiercingWeapon> PIERCING_WEAPON = register(
		"piercing_weapon", builder -> builder.persistent(PiercingWeapon.CODEC).networkSynchronized(PiercingWeapon.STREAM_CODEC).cacheEncoding()
	);
	public static final DataComponentType<KineticWeapon> KINETIC_WEAPON = register(
		"kinetic_weapon", builder -> builder.persistent(KineticWeapon.CODEC).networkSynchronized(KineticWeapon.STREAM_CODEC).cacheEncoding()
	);
	public static final DataComponentType<SwingAnimation> SWING_ANIMATION = register(
		"swing_animation", builder -> builder.persistent(SwingAnimation.CODEC).networkSynchronized(SwingAnimation.STREAM_CODEC)
	);
	public static final DataComponentType<Holder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = register(
		"zombie_nautilus/variant", builder -> builder.persistent(ZombieNautilusVariant.CODEC).networkSynchronized(ZombieNautilusVariant.STREAM_CODEC)
	);

	private MountsComponents() {
	}

	private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, EverythingBackport.vanilla(name), builder.apply(DataComponentType.builder()).build());
	}

	static void init() {
	}
}
