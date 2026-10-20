package backport.everything_backport.client.mixin;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.client.copperage.EndFlashLevel;
import backport.everything_backport.client.copperage.EndFlashState;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Draws the End flash in the sky after the End sky box (1.21.9). */
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
	@Unique
	private static final ResourceLocation END_FLASH_LOCATION = EverythingBackport.vanilla("textures/environment/end_flash.png");
	@Unique
	@Nullable
	private GpuBuffer everythingBackport$endFlashBuffer;

	@Inject(method = "renderEndSky", at = @At("TAIL"))
	private void everythingBackport$renderEndFlash(CallbackInfo ci) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level instanceof EndFlashLevel level) {
			EndFlashState endFlashState = level.everythingBackport$endFlashState();
			if (endFlashState != null) {
				float intensity = endFlashState.getIntensity(minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false));
				if (intensity > 1.0E-5F) {
					this.everythingBackport$drawEndFlash(intensity, endFlashState.getXAngle(), endFlashState.getYAngle());
				}
			}
		}
	}

	@Unique
	private void everythingBackport$drawEndFlash(float intensity, float xAngle, float yAngle) {
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(END_FLASH_LOCATION);
		texture.setUseMipmaps(false);
		Matrix4fStack modelView = RenderSystem.getModelViewStack();
		modelView.pushMatrix();
		modelView.rotate(Axis.YP.rotationDegrees(180.0F - yAngle));
		modelView.rotate(Axis.XP.rotationDegrees(-90.0F - xAngle));
		modelView.translate(0.0F, 100.0F, 0.0F);
		modelView.scale(60.0F, 1.0F, 60.0F);
		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
			.writeTransform(modelView, new Vector4f(intensity, intensity, intensity, intensity), new Vector3f(), new Matrix4f(), 0.0F);
		GpuTextureView color = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
		GpuTextureView depth = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();
		RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
		GpuBuffer indices = quadIndices.getBuffer(6);

		try (RenderPass renderPass = RenderSystem.getDevice()
				.createCommandEncoder()
				.createRenderPass(() -> "End flash", color, OptionalInt.empty(), depth, OptionalDouble.empty())) {
			renderPass.setPipeline(RenderPipelines.CELESTIAL);
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setUniform("DynamicTransforms", transforms);
			renderPass.bindSampler("Sampler0", texture.getTextureView());
			renderPass.setVertexBuffer(0, this.everythingBackport$getEndFlashBuffer());
			renderPass.setIndexBuffer(indices, quadIndices.type());
			renderPass.drawIndexed(0, 0, 6, 1);
		}

		modelView.popMatrix();
	}

	@Unique
	private GpuBuffer everythingBackport$getEndFlashBuffer() {
		if (this.everythingBackport$endFlashBuffer == null) {
			try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * DefaultVertexFormat.POSITION_TEX.getVertexSize())) {
				BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				Matrix4f matrix = new Matrix4f();
				bufferBuilder.addVertex(matrix, -1.0F, 0.0F, -1.0F).setUv(0.0F, 0.0F);
				bufferBuilder.addVertex(matrix, 1.0F, 0.0F, -1.0F).setUv(1.0F, 0.0F);
				bufferBuilder.addVertex(matrix, 1.0F, 0.0F, 1.0F).setUv(1.0F, 1.0F);
				bufferBuilder.addVertex(matrix, -1.0F, 0.0F, 1.0F).setUv(0.0F, 1.0F);

				try (MeshData meshData = bufferBuilder.buildOrThrow()) {
					this.everythingBackport$endFlashBuffer = RenderSystem.getDevice().createBuffer(() -> "End flash quad", GpuBuffer.USAGE_VERTEX, meshData.vertexBuffer());
				}
			}
		}

		return this.everythingBackport$endFlashBuffer;
	}
}
