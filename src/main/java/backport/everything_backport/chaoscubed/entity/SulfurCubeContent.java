package backport.everything_backport.chaoscubed.entity;

import com.mojang.serialization.Codec;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

/**
 * 26.2's {@code sulfur_cube_content} component: the block item a sulfur cube swallowed. 26.2 stores an {@code ItemStackTemplate};
 * 1.21.7 has none, so this keeps a one-item stack and compares stacks by content.
 */
public final class SulfurCubeContent implements TooltipProvider {
	public static final Codec<SulfurCubeContent> CODEC = ItemStack.SINGLE_ITEM_CODEC.xmap(SulfurCubeContent::new, SulfurCubeContent::absorbedBlockItemStack);
	public static final StreamCodec<RegistryFriendlyByteBuf, SulfurCubeContent> STREAM_CODEC = ItemStack.STREAM_CODEC
		.map(SulfurCubeContent::new, SulfurCubeContent::absorbedBlockItemStack);
	private final ItemStack absorbedBlockItemStack;

	private SulfurCubeContent(ItemStack absorbedBlockItemStack) {
		this.absorbedBlockItemStack = absorbedBlockItemStack.copyWithCount(1);
	}

	public static SulfurCubeContent ofNonEmpty(ItemStack itemStack) {
		return new SulfurCubeContent(itemStack);
	}

	/** A fresh copy of the swallowed stack. */
	public ItemStack absorbedBlockItemStack() {
		return this.absorbedBlockItemStack.copy();
	}

	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
		consumer.accept(
			Component.translatable("entity.minecraft.sulfur_cube.content", this.absorbedBlockItemStack.getHoverName())
				.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
		);
	}

	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof SulfurCubeContent content && ItemStack.matches(this.absorbedBlockItemStack, content.absorbedBlockItemStack);
	}

	@Override
	public int hashCode() {
		return ItemStack.hashItemAndComponents(this.absorbedBlockItemStack);
	}
}
