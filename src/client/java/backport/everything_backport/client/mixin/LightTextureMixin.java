package backport.everything_backport.client.mixin;

import backport.everything_backport.client.copperage.EndFlashLevel;
import backport.everything_backport.client.copperage.EndFlashState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.Std140Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 1.21.9 lighting in the End: no more forced bright lightmap, the sky light is purple and its brightness follows the
 * End flashes.
 */
@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
	@Unique
	private static final Vector3f END_FLASH_SKY_LIGHT_COLOR = new Vector3f(0.9F, 0.5F, 1.0F);
	@Shadow
	@Final
	private Minecraft minecraft;

	@Unique
	private boolean everythingBackport$isEnd() {
		ClientLevel level = this.minecraft.level;
		return level != null && level.effects() instanceof DimensionSpecialEffects.EndEffects;
	}

	/** Sky factor: the second float written to the lightmap uniforms. */
	@WrapOperation(
		method = "updateLightTexture",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putFloat(F)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 1)
	)
	private Std140Builder everythingBackport$endSkyFactor(Std140Builder builder, float skyFactor, Operation<Std140Builder> original, @Local(argsOnly = true) float partialTick) {
		if (this.everythingBackport$isEnd()) {
			EndFlashState endFlashState = ((EndFlashLevel)this.minecraft.level).everythingBackport$endFlashState();
			float intensity = 0.0F;
			if (endFlashState != null && !this.minecraft.options.hideLightningFlash().get()) {
				intensity = endFlashState.getIntensity(partialTick);
				if (this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
					intensity /= 3.0F;
				}
			}

			skyFactor = intensity;
		}

		return original.call(builder, skyFactor);
	}

	@WrapOperation(
		method = "updateLightTexture",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putInt(I)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 0)
	)
	private Std140Builder everythingBackport$noBrightLightmapInEnd(Std140Builder builder, int useBrightLightmap, Operation<Std140Builder> original) {
		return original.call(builder, this.everythingBackport$isEnd() ? 0 : useBrightLightmap);
	}

	@WrapOperation(
		method = "updateLightTexture",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putVec3(Lorg/joml/Vector3fc;)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 0)
	)
	private Std140Builder everythingBackport$endSkyLightColor(Std140Builder builder, Vector3fc color, Operation<Std140Builder> original) {
		return original.call(builder, this.everythingBackport$isEnd() ? END_FLASH_SKY_LIGHT_COLOR : color);
	}
}
