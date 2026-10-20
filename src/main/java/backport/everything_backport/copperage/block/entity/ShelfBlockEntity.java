package backport.everything_backport.copperage.block.entity;

import backport.everything_backport.copperage.CopperAgeBlockEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.slf4j.Logger;

public class ShelfBlockEntity extends BlockEntity implements ListBackedContainer {
	public static final int MAX_ITEMS = 3;
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String ALIGN_ITEMS_TO_BOTTOM_TAG = "align_items_to_bottom";
	private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
	private boolean alignItemsToBottom;

	public ShelfBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CopperAgeBlockEntities.SHELF, blockPos, blockState);
	}

	@Override
	protected void loadAdditional(ValueInput valueInput) {
		super.loadAdditional(valueInput);
		this.items.clear();
		ContainerHelper.loadAllItems(valueInput, this.items);
		this.alignItemsToBottom = valueInput.getBooleanOr(ALIGN_ITEMS_TO_BOTTOM_TAG, false);
	}

	@Override
	protected void saveAdditional(ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		ContainerHelper.saveAllItems(valueOutput, this.items, true);
		valueOutput.putBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG, this.alignItemsToBottom);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(collector, provider);
			ContainerHelper.saveAllItems(output, this.items, true);
			output.putBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG, this.alignItemsToBottom);
			return output.buildResult();
		}
	}

	@Override
	public NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public boolean stillValid(Player player) {
		return Container.stillValidBlockEntity(this, player);
	}

	public ItemStack swapItemNoUpdate(int slot, ItemStack itemStack) {
		ItemStack previous = this.removeItemNoUpdate(slot);
		this.setItemNoUpdate(slot, itemStack);
		return previous;
	}

	public void setChanged(Holder.Reference<GameEvent> gameEvent) {
		super.setChanged();
		if (this.level != null) {
			this.level.gameEvent(gameEvent, this.worldPosition, GameEvent.Context.of(this.getBlockState()));
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Override
	public void setChanged() {
		this.setChanged(GameEvent.BLOCK_ACTIVATE);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
		super.applyImplicitComponents(dataComponentGetter);
		dataComponentGetter.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.items);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
	}

	@Override
	public void removeComponentsFromTag(ValueOutput valueOutput) {
		valueOutput.discard("Items");
	}

	public boolean getAlignItemsToBottom() {
		return this.alignItemsToBottom;
	}
}
