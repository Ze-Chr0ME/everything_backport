package backport.everything_backport.client.mixin;

import backport.everything_backport.mountsofmayhem.MountsComponents;
import backport.everything_backport.mountsofmayhem.SpearCombat;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import backport.everything_backport.mountsofmayhem.component.PiercingWeapon;
import backport.everything_backport.mountsofmayhem.network.StabPayload;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.21.11's attack input: spears stab along the look direction instead of hitting the targeted entity, weapons with a
 * minimum attack charge can't swing early, and weapons with an attack range only hit inside it.
 */
@Mixin(Minecraft.class)
public abstract class SpearMinecraftMixin {
	@Shadow
	@Nullable
	public LocalPlayer player;
	@Shadow
	@Nullable
	public MultiPlayerGameMode gameMode;
	@Shadow
	@Nullable
	public HitResult hitResult;
	@Shadow
	public int missTime;

	@Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"), cancellable = true)
	private void everythingBackport$stab(CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
		if (SpearCombat.cannotAttackWithItem(this.player, itemStack, 0)) {
			cir.setReturnValue(false);
			return;
		}

		PiercingWeapon piercingWeapon = itemStack.get(MountsComponents.PIERCING_WEAPON);
		if (piercingWeapon != null && !this.player.isSpectator()) {
			// 1.21.11's MultiPlayerGameMode#piercingAttack.
			((MultiPlayerGameModeInvoker)this.gameMode).everythingBackport$ensureHasSentCarriedItem();
			ClientPlayNetworking.send(new StabPayload());
			SpearCombat.onAttack(this.player);
			piercingWeapon.makeSound(this.player);
			this.player.swing(InteractionHand.MAIN_HAND);
			cir.setReturnValue(true);
		}
	}

	@WrapWithCondition(
		method = "startAttack",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"
		)
	)
	private boolean everythingBackport$attackRange(MultiPlayerGameMode gameMode, Player player, Entity target) {
		AttackRange attackRange = player.getMainHandItem().get(MountsComponents.ATTACK_RANGE);
		return attackRange == null || attackRange.isInRange(player, this.hitResult.getLocation());
	}

	/** Holding attack with a spear doesn't mine blocks. */
	@Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$noMiningWithSpear(boolean attacking, CallbackInfo ci) {
		if (this.player != null && this.player.getMainHandItem().has(MountsComponents.PIERCING_WEAPON)) {
			if (!attacking) {
				this.missTime = 0;
			}

			ci.cancel();
		}
	}
}
