package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.SpearCombat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11 tracks how far each entity moved last tick; kinetic weapons scale with it. */
@Mixin(Entity.class)
public abstract class KnownSpeedEntityMixin implements SpearCombat.KnownSpeed {
	@Unique
	private Vec3 everythingBackport$lastKnownSpeed = Vec3.ZERO;
	@Unique
	@Nullable
	private Vec3 everythingBackport$lastKnownPosition;

	@Shadow
	public abstract Vec3 position();

	@Override
	public Vec3 everythingBackport$getLastKnownSpeed() {
		return this.everythingBackport$lastKnownSpeed;
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	private void everythingBackport$computeSpeed(CallbackInfo ci) {
		if (this.everythingBackport$lastKnownPosition == null) {
			this.everythingBackport$lastKnownPosition = this.position();
		}

		this.everythingBackport$lastKnownSpeed = this.position().subtract(this.everythingBackport$lastKnownPosition);
		this.everythingBackport$lastKnownPosition = this.position();
	}
}
