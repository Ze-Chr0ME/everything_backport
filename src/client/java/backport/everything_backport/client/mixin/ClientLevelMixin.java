package backport.everything_backport.client.mixin;

import backport.everything_backport.client.copperage.DirectionalSoundInstance;
import backport.everything_backport.client.copperage.EndFlashLevel;
import backport.everything_backport.client.copperage.EndFlashState;
import backport.everything_backport.copperage.CopperAgeSounds;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements EndFlashLevel {
	@Shadow
	@Final
	private Minecraft minecraft;
	@Unique
	@Nullable
	private EndFlashState everythingBackport$endFlashState;

	@Shadow
	public abstract DimensionSpecialEffects effects();

	@Override
	@Nullable
	public EndFlashState everythingBackport$endFlashState() {
		if (this.everythingBackport$endFlashState == null && this.effects() instanceof DimensionSpecialEffects.EndEffects) {
			this.everythingBackport$endFlashState = new EndFlashState();
		}

		return this.everythingBackport$endFlashState;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void everythingBackport$tickEndFlash(BooleanSupplier booleanSupplier, CallbackInfo ci) {
		EndFlashState endFlashState = this.everythingBackport$endFlashState();
		if (endFlashState != null) {
			ClientLevel level = (ClientLevel)(Object)this;
			endFlashState.tick(level.getGameTime());
			if (endFlashState.flashStartedThisTick() && !(this.minecraft.screen instanceof WinScreen)) {
				this.minecraft
					.getSoundManager()
					.playDelayed(
						new DirectionalSoundInstance(
							CopperAgeSounds.WEATHER_END_FLASH,
							SoundSource.WEATHER,
							level.random,
							this.minecraft.gameRenderer.getMainCamera(),
							endFlashState.getXAngle(),
							endFlashState.getYAngle()
						),
						EndFlashState.SOUND_DELAY_IN_TICKS
					);
			}
		}
	}
}
