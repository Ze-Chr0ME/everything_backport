package backport.everything_backport.client.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.11's {@code LocalPlayer#raycastHitResult}: an item with an attack range targets entities within it first. */
@Mixin(GameRenderer.class)
public abstract class SpearGameRendererMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private static HitResult filterHitResult(HitResult hitResult, Vec3 eyePosition, double range) {
		throw new AssertionError();
	}

	@WrapOperation(
		method = "pick(F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;")
	)
	private HitResult everythingBackport$attackRangePick(
		GameRenderer gameRenderer, Entity entity, double blockRange, double entityRange, float partialTick, Operation<HitResult> original
	) {
		AttackRange attackRange = SpearCombat.getActiveItem(this.minecraft.player).get(MountsComponents.ATTACK_RANGE);
		if (attackRange != null) {
			HitResult hitResult = attackRange.getClosesetHit(entity, partialTick, EntitySelector.CAN_BE_PICKED);
			if (hitResult instanceof BlockHitResult) {
				hitResult = filterHitResult(hitResult, entity.getEyePosition(partialTick), blockRange);
			}

			if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
				return hitResult;
			}
		}

		return original.call(gameRenderer, entity, blockRange, entityRange, partialTick);
	}
}
