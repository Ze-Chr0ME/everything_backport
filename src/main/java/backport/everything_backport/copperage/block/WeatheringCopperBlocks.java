package backport.everything_backport.copperage.block;

import backport.everything_backport.registry.Reg;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record WeatheringCopperBlocks(
	Block unaffected, Block exposed, Block weathered, Block oxidized, Block waxed, Block waxedExposed, Block waxedWeathered, Block waxedOxidized
) {
	public static <W extends Block & WeatheringCopper> WeatheringCopperBlocks create(
		String name,
		Function<BlockBehaviour.Properties, ? extends Block> waxedFactory,
		BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, W> weatheringFactory,
		Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> properties
	) {
		return new WeatheringCopperBlocks(
			Reg.block(name, p -> weatheringFactory.apply(WeatheringCopper.WeatherState.UNAFFECTED, p), properties.apply(WeatheringCopper.WeatherState.UNAFFECTED)),
			Reg.block("exposed_" + name, p -> weatheringFactory.apply(WeatheringCopper.WeatherState.EXPOSED, p), properties.apply(WeatheringCopper.WeatherState.EXPOSED)),
			Reg.block(
				"weathered_" + name, p -> weatheringFactory.apply(WeatheringCopper.WeatherState.WEATHERED, p), properties.apply(WeatheringCopper.WeatherState.WEATHERED)
			),
			Reg.block("oxidized_" + name, p -> weatheringFactory.apply(WeatheringCopper.WeatherState.OXIDIZED, p), properties.apply(WeatheringCopper.WeatherState.OXIDIZED)),
			Reg.block("waxed_" + name, waxedFactory::apply, properties.apply(WeatheringCopper.WeatherState.UNAFFECTED)),
			Reg.block("waxed_exposed_" + name, waxedFactory::apply, properties.apply(WeatheringCopper.WeatherState.EXPOSED)),
			Reg.block("waxed_weathered_" + name, waxedFactory::apply, properties.apply(WeatheringCopper.WeatherState.WEATHERED)),
			Reg.block("waxed_oxidized_" + name, waxedFactory::apply, properties.apply(WeatheringCopper.WeatherState.OXIDIZED))
		);
	}

	public ImmutableBiMap<Block, Block> weatheringMapping() {
		return ImmutableBiMap.of(this.unaffected, this.exposed, this.exposed, this.weathered, this.weathered, this.oxidized);
	}

	public ImmutableBiMap<Block, Block> waxedMapping() {
		return ImmutableBiMap.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
	}

	public ImmutableList<Block> asList() {
		return ImmutableList.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
	}

	public void forEach(Consumer<Block> consumer) {
		consumer.accept(this.unaffected);
		consumer.accept(this.exposed);
		consumer.accept(this.weathered);
		consumer.accept(this.oxidized);
		consumer.accept(this.waxed);
		consumer.accept(this.waxedExposed);
		consumer.accept(this.waxedWeathered);
		consumer.accept(this.waxedOxidized);
	}
}
