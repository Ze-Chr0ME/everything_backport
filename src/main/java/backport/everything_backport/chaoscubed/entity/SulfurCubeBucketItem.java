package backport.everything_backport.chaoscubed.entity;

import backport.everything_backport.chaoscubed.ChaosComponents;
import backport.everything_backport.chaoscubed.ChaosEntities;
import backport.everything_backport.chaoscubed.ChaosSounds;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluids;

/** 26.2's bucket of sulfur cube; the tooltip names the block it holds, as the {@code sulfur_cube_content} component does there. */
public class SulfurCubeBucketItem extends MobBucketItem {
	public SulfurCubeBucketItem(Properties properties) {
		super(ChaosEntities.SULFUR_CUBE, Fluids.EMPTY, ChaosSounds.BUCKET_EMPTY_SULFUR_CUBE, properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, builder, flag);
		SulfurCubeContent content = stack.get(ChaosComponents.SULFUR_CUBE_CONTENT);
		if (content != null) {
			content.addToTooltip(context, builder, flag, stack);
		}
	}
}
