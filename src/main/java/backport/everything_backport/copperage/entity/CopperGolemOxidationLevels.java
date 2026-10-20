package backport.everything_backport.copperage.entity;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import backport.everything_backport.copperage.CopperAgeSounds;
import net.minecraft.world.level.block.WeatheringCopper;

public class CopperGolemOxidationLevels {
	private static final CopperGolemOxidationLevel UNAFFECTED = new CopperGolemOxidationLevel(
		CopperAgeSounds.COPPER_GOLEM_SPIN,
		CopperAgeSounds.COPPER_GOLEM_HURT,
		CopperAgeSounds.COPPER_GOLEM_DEATH,
		CopperAgeSounds.COPPER_GOLEM_STEP,
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/copper_golem.png"),
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/copper_golem_eyes.png")
	);
	private static final CopperGolemOxidationLevel EXPOSED = new CopperGolemOxidationLevel(
		CopperAgeSounds.COPPER_GOLEM_SPIN,
		CopperAgeSounds.COPPER_GOLEM_HURT,
		CopperAgeSounds.COPPER_GOLEM_DEATH,
		CopperAgeSounds.COPPER_GOLEM_STEP,
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/exposed_copper_golem.png"),
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/exposed_copper_golem_eyes.png")
	);
	private static final CopperGolemOxidationLevel WEATHERED = new CopperGolemOxidationLevel(
		CopperAgeSounds.COPPER_GOLEM_WEATHERED_SPIN,
		CopperAgeSounds.COPPER_GOLEM_WEATHERED_HURT,
		CopperAgeSounds.COPPER_GOLEM_WEATHERED_DEATH,
		CopperAgeSounds.COPPER_GOLEM_WEATHERED_STEP,
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/weathered_copper_golem.png"),
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/weathered_copper_golem_eyes.png")
	);
	private static final CopperGolemOxidationLevel OXIDIZED = new CopperGolemOxidationLevel(
		CopperAgeSounds.COPPER_GOLEM_OXIDIZED_SPIN,
		CopperAgeSounds.COPPER_GOLEM_OXIDIZED_HURT,
		CopperAgeSounds.COPPER_GOLEM_OXIDIZED_DEATH,
		CopperAgeSounds.COPPER_GOLEM_OXIDIZED_STEP,
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/oxidized_copper_golem.png"),
		ResourceLocation.withDefaultNamespace("textures/entity/copper_golem/oxidized_copper_golem_eyes.png")
	);
	private static final Map<WeatheringCopper.WeatherState, CopperGolemOxidationLevel> WEATHERED_STATES = Map.of(
		WeatheringCopper.WeatherState.UNAFFECTED,
		UNAFFECTED,
		WeatheringCopper.WeatherState.EXPOSED,
		EXPOSED,
		WeatheringCopper.WeatherState.WEATHERED,
		WEATHERED,
		WeatheringCopper.WeatherState.OXIDIZED,
		OXIDIZED
	);

	public static CopperGolemOxidationLevel getOxidationLevel(WeatheringCopper.WeatherState weatherState) {
		return (CopperGolemOxidationLevel)WEATHERED_STATES.get(weatherState);
	}
}
