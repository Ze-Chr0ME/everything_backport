package backport.everything_backport.client.mixin;

import backport.everything_backport.copperage.block.CopperChestBlock;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Copper chest textures for the chest block entity renderer. */
@Mixin(Sheets.class)
public abstract class SheetsMixin {
	@Inject(
		method = "chooseMaterial(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/state/properties/ChestType;Z)Lnet/minecraft/client/resources/model/Material;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void everythingBackport$copperChest(BlockEntity blockEntity, ChestType chestType, boolean christmas, CallbackInfoReturnable<Material> cir) {
		if (blockEntity.getBlockState().getBlock() instanceof CopperChestBlock copperChest) {
			String name = switch (copperChest.getState()) {
				case UNAFFECTED -> "copper";
				case EXPOSED -> "copper_exposed";
				case WEATHERED -> "copper_weathered";
				case OXIDIZED -> "copper_oxidized";
			};
			cir.setReturnValue(everythingBackport$material(chestType, name));
		}
	}

	@Unique
	private static Material everythingBackport$material(ChestType chestType, String name) {
		return Sheets.CHEST_MAPPER.defaultNamespaceApply(switch (chestType) {
			case LEFT -> name + "_left";
			case RIGHT -> name + "_right";
			case SINGLE -> name;
		});
	}
}
