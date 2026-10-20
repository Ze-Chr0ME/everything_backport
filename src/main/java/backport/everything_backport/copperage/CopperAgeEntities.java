package backport.everything_backport.copperage;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.copperage.block.WeatherStates;
import backport.everything_backport.copperage.entity.CopperGolem;
import backport.everything_backport.copperage.entity.CopperGolemState;
import backport.everything_backport.registry.Reg;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.WeatheringCopper;

public final class CopperAgeEntities {
	public static final EntityDataSerializer<WeatheringCopper.WeatherState> WEATHERING_COPPER_STATE = EntityDataSerializer.forValueType(WeatherStates.STREAM_CODEC);
	public static final EntityDataSerializer<CopperGolemState> COPPER_GOLEM_STATE = EntityDataSerializer.forValueType(CopperGolemState.STREAM_CODEC);

	public static final EntityType<CopperGolem> COPPER_GOLEM = Reg.entity(
		"copper_golem",
		EntityType.Builder.of(CopperGolem::new, MobCategory.MISC)
			.sized(0.49F, 0.98F)
			.eyeHeight(0.8125F)
			.clientTrackingRange(10)
	);

	private CopperAgeEntities() {
	}

	static void init() {
		FabricTrackedDataRegistry.register(EverythingBackport.id("weathering_copper_state"), WEATHERING_COPPER_STATE);
		FabricTrackedDataRegistry.register(EverythingBackport.id("copper_golem_state"), COPPER_GOLEM_STATE);
		FabricDefaultAttributeRegistry.register(COPPER_GOLEM, CopperGolem.createAttributes());
	}
}
