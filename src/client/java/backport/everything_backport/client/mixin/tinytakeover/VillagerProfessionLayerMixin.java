package backport.everything_backport.client.mixin.tinytakeover;

import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Baby villagers and zombie villagers wear their 26.1 biome outfit from the {@code baby} folder. */
@Mixin(VillagerProfessionLayer.class)
public abstract class VillagerProfessionLayerMixin {
	@ModifyArg(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/entity/layers/VillagerProfessionLayer;getResourceLocation(Ljava/lang/String;Lnet/minecraft/core/Holder;)Lnet/minecraft/resources/ResourceLocation;",
			ordinal = 0
		),
		index = 0
	)
	private String everythingBackport$babyFolder(String folder, @Local(argsOnly = true) LivingEntityRenderState state) {
		return state.isBaby ? "baby" : folder;
	}
}
