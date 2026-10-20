package backport.everything_backport.client.mixin.chaoscubed;

import backport.everything_backport.chaoscubed.ChaosAttributes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** 26.2: a living entity's name tag shows up to its {@code name_tag_distance} attribute away instead of a fixed 64 blocks. */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererNameTagMixin {
	@ModifyConstant(method = "extractRenderState", constant = @Constant(doubleValue = 4096.0))
	private double everythingBackport$nameTagDistance(double maxDistanceSq, Entity entity, EntityRenderState state, float partialTicks) {
		return entity instanceof LivingEntity living ? Mth.square(ChaosAttributes.get(living, ChaosAttributes.NAME_TAG_DISTANCE)) : maxDistanceSq;
	}
}
