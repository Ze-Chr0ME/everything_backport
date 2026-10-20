package backport.everything_backport.client.mixin.tinytakeover;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Lets renderers swap in models that are not subclasses of their 1.21.7 model type. */
@Mixin(AgeableMobRenderer.class)
public interface AgeableMobRendererAccessor {
	@Mutable
	@Accessor("adultModel")
	void everythingBackport$setAdultModel(EntityModel<?> model);

	@Mutable
	@Accessor("babyModel")
	void everythingBackport$setBabyModel(EntityModel<?> model);
}
