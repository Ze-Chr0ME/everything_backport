package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyLayers;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Swaps the 26.1 baby meshes into the model layers every renderer bakes from. */
@Mixin(LayerDefinitions.class)
public abstract class LayerDefinitionsMixin {
	@Inject(method = "createRoots", at = @At("RETURN"), cancellable = true)
	private static void everythingBackport$babyLayers(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir) {
		Map<ModelLayerLocation, LayerDefinition> roots = new HashMap<>(cir.getReturnValue());
		BabyLayers.apply(roots);
		cir.setReturnValue(ImmutableMap.copyOf(roots));
	}
}
