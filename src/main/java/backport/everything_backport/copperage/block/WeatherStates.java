package backport.everything_backport.copperage.block;

import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.level.block.WeatheringCopper;

/** {@link WeatheringCopper.WeatherState} helpers added in 1.21.9. */
public final class WeatherStates {
	public static final IntFunction<WeatheringCopper.WeatherState> BY_ID = ByIdMap.continuous(
		Enum::ordinal, WeatheringCopper.WeatherState.values(), ByIdMap.OutOfBoundsStrategy.CLAMP
	);
	public static final StreamCodec<ByteBuf, WeatheringCopper.WeatherState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

	private WeatherStates() {
	}

	public static WeatheringCopper.WeatherState next(WeatheringCopper.WeatherState state) {
		return BY_ID.apply(state.ordinal() + 1);
	}

	public static WeatheringCopper.WeatherState previous(WeatheringCopper.WeatherState state) {
		return BY_ID.apply(state.ordinal() - 1);
	}
}
