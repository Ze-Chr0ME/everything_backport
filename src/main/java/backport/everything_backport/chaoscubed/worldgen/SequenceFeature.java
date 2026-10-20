package backport.everything_backport.chaoscubed.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SequenceFeature extends Feature<CompositeFeatureConfiguration> {
	public SequenceFeature(Codec<CompositeFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<CompositeFeatureConfiguration> context) {
		for (Holder<PlacedFeature> feature : context.config().features()) {
			if (!feature.value().place(context.level(), context.chunkGenerator(), context.random(), context.origin())) {
				return false;
			}
		}

		return true;
	}
}
