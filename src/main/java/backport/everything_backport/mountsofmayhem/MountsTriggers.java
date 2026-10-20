package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

public final class MountsTriggers {
	public static final SpearMobsTrigger SPEAR_MOBS = Registry.register(
		BuiltInRegistries.TRIGGER_TYPES, EverythingBackport.vanilla("spear_mobs"), new SpearMobsTrigger()
	);

	private MountsTriggers() {
	}

	static void init() {
	}

	/** Hitting at least {@code count} mobs with one spear charge. */
	public static class SpearMobsTrigger extends SimpleCriterionTrigger<SpearMobsTrigger.TriggerInstance> {
		@Override
		public Codec<SpearMobsTrigger.TriggerInstance> codec() {
			return SpearMobsTrigger.TriggerInstance.CODEC;
		}

		public void trigger(ServerPlayer serverPlayer, int count) {
			this.trigger(serverPlayer, triggerInstance -> triggerInstance.matches(count));
		}

		public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<Integer> count) implements SimpleCriterionTrigger.SimpleInstance {
			public static final Codec<SpearMobsTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(SpearMobsTrigger.TriggerInstance::player),
						ExtraCodecs.POSITIVE_INT.optionalFieldOf("count").forGetter(SpearMobsTrigger.TriggerInstance::count)
					)
					.apply(instance, SpearMobsTrigger.TriggerInstance::new)
			);

			public boolean matches(int count) {
				return this.count.isEmpty() || count >= this.count.get();
			}
		}
	}
}
