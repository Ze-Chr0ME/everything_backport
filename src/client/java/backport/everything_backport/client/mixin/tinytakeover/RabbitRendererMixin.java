package backport.everything_backport.client.mixin.tinytakeover;

import backport.everything_backport.client.tinytakeover.BabyLayers;
import backport.everything_backport.client.tinytakeover.model.KeyframeRabbitModel;
import backport.everything_backport.tinytakeover.TinyAnimationStates;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.1 redesigned the rabbit, adult and kit alike, with new meshes, textures and keyframe animations. */
@Mixin(RabbitRenderer.class)
public abstract class RabbitRendererMixin {
	@Unique
	private static final Map<Rabbit.Variant, String> EVERYTHING_BACKPORT$NAMES = new EnumMap<>(Map.of(
		Rabbit.Variant.BROWN, "brown",
		Rabbit.Variant.WHITE, "white",
		Rabbit.Variant.BLACK, "black",
		Rabbit.Variant.WHITE_SPLOTCHED, "white_splotched",
		Rabbit.Variant.GOLD, "gold",
		Rabbit.Variant.SALT, "salt",
		Rabbit.Variant.EVIL, "caerbannog"
	));

	@Unique
	private static ResourceLocation everythingBackport$texture(String name, boolean baby) {
		return ResourceLocation.withDefaultNamespace("textures/entity/rabbit/rabbit_" + name + (baby ? "_baby" : "") + ".png");
	}

	/** Neither 1.21.7 rabbit model fits the 26.1 meshes, so those meshes have their own layers and replace both models here. */
	@Inject(method = "<init>", at = @At("TAIL"))
	private void everythingBackport$keyframeModels(EntityRendererProvider.Context context, CallbackInfo ci) {
		KeyframeRabbitModel adult = KeyframeRabbitModel.adult(context.bakeLayer(BabyLayers.RABBIT));
		((AgeableMobRendererAccessor)this).everythingBackport$setAdultModel(adult);
		((AgeableMobRendererAccessor)this).everythingBackport$setBabyModel(KeyframeRabbitModel.baby(context.bakeLayer(BabyLayers.RABBIT_BABY)));
		((LivingEntityRendererAccessor)this).everythingBackport$setModel(adult);
	}

	@Inject(
		method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/RabbitRenderState;)Lnet/minecraft/resources/ResourceLocation;",
		at = @At("HEAD"),
		cancellable = true
	)
	private void everythingBackport$texture(RabbitRenderState state, CallbackInfoReturnable<ResourceLocation> cir) {
		String name = state.isToast ? "toast" : EVERYTHING_BACKPORT$NAMES.getOrDefault(state.variant, "brown");
		cir.setReturnValue(everythingBackport$texture(name, state.isBaby));
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/Rabbit;Lnet/minecraft/client/renderer/entity/state/RabbitRenderState;F)V", at = @At("TAIL"))
	private void everythingBackport$copyAnimations(Rabbit rabbit, RabbitRenderState state, float partialTick, CallbackInfo ci) {
		TinyAnimationStates.copy(rabbit, state);
	}
}
