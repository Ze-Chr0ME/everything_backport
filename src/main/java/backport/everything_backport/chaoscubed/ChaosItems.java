package backport.everything_backport.chaoscubed;

import backport.everything_backport.chaoscubed.entity.SulfurCubeBucketItem;
import backport.everything_backport.registry.Reg;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;

public final class ChaosItems {
	public static final Item SULFUR = Reg.blockItem(ChaosBlocks.SULFUR);
	public static final Item POTENT_SULFUR = Reg.blockItem(ChaosBlocks.POTENT_SULFUR);
	public static final Item SULFUR_SLAB = Reg.blockItem(ChaosBlocks.SULFUR_SLAB);
	public static final Item SULFUR_STAIRS = Reg.blockItem(ChaosBlocks.SULFUR_STAIRS);
	public static final Item SULFUR_WALL = Reg.blockItem(ChaosBlocks.SULFUR_WALL);
	public static final Item POLISHED_SULFUR = Reg.blockItem(ChaosBlocks.POLISHED_SULFUR);
	public static final Item POLISHED_SULFUR_SLAB = Reg.blockItem(ChaosBlocks.POLISHED_SULFUR_SLAB);
	public static final Item POLISHED_SULFUR_STAIRS = Reg.blockItem(ChaosBlocks.POLISHED_SULFUR_STAIRS);
	public static final Item POLISHED_SULFUR_WALL = Reg.blockItem(ChaosBlocks.POLISHED_SULFUR_WALL);
	public static final Item SULFUR_BRICKS = Reg.blockItem(ChaosBlocks.SULFUR_BRICKS);
	public static final Item SULFUR_BRICK_SLAB = Reg.blockItem(ChaosBlocks.SULFUR_BRICK_SLAB);
	public static final Item SULFUR_BRICK_STAIRS = Reg.blockItem(ChaosBlocks.SULFUR_BRICK_STAIRS);
	public static final Item SULFUR_BRICK_WALL = Reg.blockItem(ChaosBlocks.SULFUR_BRICK_WALL);
	public static final Item CHISELED_SULFUR = Reg.blockItem(ChaosBlocks.CHISELED_SULFUR);
	public static final Item CINNABAR = Reg.blockItem(ChaosBlocks.CINNABAR);
	public static final Item CINNABAR_SLAB = Reg.blockItem(ChaosBlocks.CINNABAR_SLAB);
	public static final Item CINNABAR_STAIRS = Reg.blockItem(ChaosBlocks.CINNABAR_STAIRS);
	public static final Item CINNABAR_WALL = Reg.blockItem(ChaosBlocks.CINNABAR_WALL);
	public static final Item POLISHED_CINNABAR = Reg.blockItem(ChaosBlocks.POLISHED_CINNABAR);
	public static final Item POLISHED_CINNABAR_SLAB = Reg.blockItem(ChaosBlocks.POLISHED_CINNABAR_SLAB);
	public static final Item POLISHED_CINNABAR_STAIRS = Reg.blockItem(ChaosBlocks.POLISHED_CINNABAR_STAIRS);
	public static final Item POLISHED_CINNABAR_WALL = Reg.blockItem(ChaosBlocks.POLISHED_CINNABAR_WALL);
	public static final Item CINNABAR_BRICKS = Reg.blockItem(ChaosBlocks.CINNABAR_BRICKS);
	public static final Item CINNABAR_BRICK_SLAB = Reg.blockItem(ChaosBlocks.CINNABAR_BRICK_SLAB);
	public static final Item CINNABAR_BRICK_STAIRS = Reg.blockItem(ChaosBlocks.CINNABAR_BRICK_STAIRS);
	public static final Item CINNABAR_BRICK_WALL = Reg.blockItem(ChaosBlocks.CINNABAR_BRICK_WALL);
	public static final Item CHISELED_CINNABAR = Reg.blockItem(ChaosBlocks.CHISELED_CINNABAR);
	public static final Item SULFUR_SPIKE = Reg.blockItem(ChaosBlocks.SULFUR_SPIKE);
	public static final Item SULFUR_CUBE_BUCKET = Reg.item(
		"sulfur_cube_bucket",
		SulfurCubeBucketItem::new,
		new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
	);
	public static final Item SULFUR_CUBE_SPAWN_EGG = Reg.item("sulfur_cube_spawn_egg", properties -> new SpawnEggItem(ChaosEntities.SULFUR_CUBE, properties));
	public static final Item MUSIC_DISC_BOUNCE = Reg.item(
		"music_disc_bounce", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(ChaosCubed.BOUNCE)
	);

	private ChaosItems() {
	}

	static void init() {
	}
}
