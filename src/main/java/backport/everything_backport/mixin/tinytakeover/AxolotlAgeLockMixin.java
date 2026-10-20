package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.AgeLockable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** A bucketed axolotl keeps its age lock (26.1). */
@Mixin(Axolotl.class)
public abstract class AxolotlAgeLockMixin {
	@Inject(method = "saveToBucketTag", at = @At("TAIL"))
	private void everythingBackport$bucketAgeLocked(ItemStack bucket, CallbackInfo ci) {
		boolean locked = ((AgeLockable)this).everythingBackport$isAgeLocked();
		CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> tag.putBoolean("AgeLocked", locked));
	}

	@Inject(method = "loadFromBucketTag", at = @At("TAIL"))
	private void everythingBackport$unbucketAgeLocked(CompoundTag tag, CallbackInfo ci) {
		((AgeLockable)this).everythingBackport$setAgeLocked(tag.getBooleanOr("AgeLocked", false));
	}
}
