package backport.everything_backport.client.mixin;

import backport.everything_backport.client.copperage.OnShelfTransform;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Reads the {@code on_shelf} display transform added in 1.21.9. */
@Mixin(targets = "net.minecraft.client.renderer.block.model.ItemTransforms$Deserializer")
public abstract class ItemTransformsDeserializerMixin {
	@Inject(
		method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/ItemTransforms;",
		at = @At("RETURN")
	)
	private void everythingBackport$readOnShelf(JsonElement json, Type type, JsonDeserializationContext context, CallbackInfoReturnable<ItemTransforms> cir) {
		JsonObject object = json.getAsJsonObject();
		if (object.has("on_shelf")) {
			ItemTransform transform = context.deserialize(object.get("on_shelf"), ItemTransform.class);
			((OnShelfTransform)(Object)cir.getReturnValue()).everythingBackport$setOnShelf(transform);
		}
	}
}
