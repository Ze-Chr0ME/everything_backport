package backport.everything_backport.mixin.wildernessbound;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 26.3's {@code #enderman_does_not_teleport_to}: endermen no longer teleport onto bedrock. */
@Mixin(EnderMan.class)
public abstract class EnderManTeleportMixin {
	@WrapOperation(method = "teleport(DDD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;randomTeleport(DDDZ)Z"))
	private boolean everythingBackport$notOntoBedrock(
		EnderMan self, double x, double y, double z, boolean particles, Operation<Boolean> original, @Local BlockState ground
	) {
		return !ground.is(Blocks.BEDROCK) && original.call(self, x, y, z, particles);
	}
}
