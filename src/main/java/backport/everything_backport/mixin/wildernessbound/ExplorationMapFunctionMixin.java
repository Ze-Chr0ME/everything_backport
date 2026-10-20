package backport.everything_backport.mixin.wildernessbound;

import backport.everything_backport.wildernessbound.WildMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 26.3's exploration maps keep the item they are applied to, so an explorer map item stays that item once it is filled in. When no
 * structure is found, 26.3's loot tables discard the map (a {@code filtered} function on {@code map_id}), which happens here too.
 */
@Mixin(ExplorationMapFunction.class)
public abstract class ExplorationMapFunctionMixin {
	@Shadow
	@Final
	private TagKey<Structure> destination;
	@Shadow
	@Final
	private Holder<MapDecorationType> mapDecoration;
	@Shadow
	@Final
	private byte zoom;
	@Shadow
	@Final
	private int searchRadius;
	@Shadow
	@Final
	private boolean skipKnownStructures;

	@Inject(method = "run", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$fillExplorerMap(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
		if (!WildMaps.isExplorerMap(stack)) {
			return;
		}

		Vec3 origin = context.getOptionalParameter(LootContextParams.ORIGIN);
		if (origin != null) {
			ServerLevel level = context.getLevel();
			BlockPos pos = level.findNearestMapStructure(this.destination, BlockPos.containing(origin), this.searchRadius, this.skipKnownStructures);
			if (pos != null) {
				ItemStack map = MapItem.create(level, pos.getX(), pos.getZ(), this.zoom, true, true);
				MapItem.renderBiomePreviewMap(level, map);
				MapItemSavedData.addTargetDecoration(map, pos, "+", this.mapDecoration);
				cir.setReturnValue(map.transmuteCopy(stack.getItem(), stack.getCount()));
				return;
			}
		}

		cir.setReturnValue(ItemStack.EMPTY);
	}
}
