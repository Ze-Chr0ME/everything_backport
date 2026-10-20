package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.model.BabyCatModel;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** The kitten collar follows the 26.1 kitten pose. */
@Mixin(CatCollarLayer.class)
public abstract class CatCollarLayerMixin {
	@Shadow
	@Final
	@Mutable
	private CatModel babyModel;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$babyCollar(CallbackInfo ci) {
		this.babyModel = new BabyCatModel(this.babyModel.root());
	}
}
