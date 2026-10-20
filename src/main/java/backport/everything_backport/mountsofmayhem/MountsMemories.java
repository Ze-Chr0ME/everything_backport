package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.mountsofmayhem.ai.SpearAttack;
import backport.everything_backport.mountsofmayhem.entity.NautilusAi;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.phys.Vec3;

public final class MountsMemories {
	public static final MemoryModuleType<Integer> CHARGE_COOLDOWN_TICKS = register("charge_cooldown_ticks");
	public static final MemoryModuleType<Integer> ATTACK_TARGET_COOLDOWN = register("attack_target_cooldown");
	public static final MemoryModuleType<Integer> SPEAR_FLEEING_TIME = register("spear_fleeing_time");
	public static final MemoryModuleType<Vec3> SPEAR_FLEEING_POSITION = register("spear_fleeing_position");
	public static final MemoryModuleType<Vec3> SPEAR_CHARGE_POSITION = register("spear_charge_position");
	public static final MemoryModuleType<Integer> SPEAR_ENGAGE_TIME = register("spear_engage_time");
	public static final MemoryModuleType<SpearAttack.SpearStatus> SPEAR_STATUS = register("spear_status");
	public static final SensorType<TemptingSensor> NAUTILUS_TEMPTATIONS = Registry.register(
		BuiltInRegistries.SENSOR_TYPE, EverythingBackport.vanilla("nautilus_temptations"), new SensorType<>(() -> new TemptingSensor(NautilusAi.getTemptations()))
	);

	private MountsMemories() {
	}

	private static <U> MemoryModuleType<U> register(String name) {
		return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, EverythingBackport.vanilla(name), new MemoryModuleType<>(Optional.empty()));
	}

	static void init() {
	}
}
