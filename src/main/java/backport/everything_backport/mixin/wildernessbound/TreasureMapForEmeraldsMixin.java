package backport.everything_backport.mixin.wildernessbound;

import backport.everything_backport.wildernessbound.WildMaps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Cartographers sell 26.3's explorer map items instead of named filled maps. */
@Mixin(VillagerTrades.TreasureMapForEmeralds.class)
public abstract class TreasureMapForEmeraldsMixin {
	@Shadow
	@Final
	private TagKey<Structure> destination;

	@ModifyArg(
		method = "getOffer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/trading/MerchantOffer;<init>(Lnet/minecraft/world/item/trading/ItemCost;Ljava/util/Optional;Lnet/minecraft/world/item/ItemStack;IIF)V"
		),
		index = 2
	)
	private ItemStack everythingBackport$explorerMap(ItemStack map) {
		return WildMaps.toExplorerMap(map, this.destination);
	}
}
