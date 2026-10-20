package backport.everything_backport.wildernessbound;

import backport.everything_backport.registry.Reg;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public final class WildSounds {
	public static final Holder<SoundEvent> POPLAR_LEAVES_AMBIENT = Reg.soundHolder("block.poplar_leaves.ambient");
	public static final SoundEvent POPLAR_LEAVES_BREAK = Reg.sound("block.poplar_leaves.break");
	public static final SoundEvent POPLAR_LEAVES_FALL = Reg.sound("block.poplar_leaves.fall");
	public static final SoundEvent POPLAR_LEAVES_HIT = Reg.sound("block.poplar_leaves.hit");
	public static final SoundEvent POPLAR_LEAVES_PLACE = Reg.sound("block.poplar_leaves.place");
	public static final SoundEvent POPLAR_LEAVES_STEP = Reg.sound("block.poplar_leaves.step");
	public static final SoundEvent RED_SHRUB_BREAK = Reg.sound("block.red_shrub.break");
	public static final SoundEvent RED_SHRUB_PLACE = Reg.sound("block.red_shrub.place");
	public static final SoundEvent SHELF_MUSHROOM_BOUNCE = Reg.sound("block.shelf_mushroom.bounce");
	public static final SoundEvent SHELF_MUSHROOM_BREAK = Reg.sound("block.shelf_mushroom.break");
	public static final SoundEvent SHELF_MUSHROOM_FALL = Reg.sound("block.shelf_mushroom.fall");
	public static final SoundEvent SHELF_MUSHROOM_PLACE = Reg.sound("block.shelf_mushroom.place");
	public static final SoundEvent SHELF_MUSHROOM_STEP = Reg.sound("block.shelf_mushroom.step");
	public static final SoundEvent STRAW_BED_BREAK = Reg.sound("block.straw_bed.break");
	public static final SoundEvent STRAW_BED_BREAK_LEAVE = Reg.sound("block.straw_bed.break_leave");
	public static final SoundEvent STRAW_BED_FALL = Reg.sound("block.straw_bed.fall");
	public static final SoundEvent STRAW_BED_HIT = Reg.sound("block.straw_bed.hit");
	public static final SoundEvent STRAW_BED_PLACE = Reg.sound("block.straw_bed.place");
	public static final SoundEvent STRAW_BED_STEP = Reg.sound("block.straw_bed.step");
	public static final SoundEvent CUSHION_BREAK = Reg.sound("entity.cushion.break");
	public static final SoundEvent CUSHION_GET_UP = Reg.sound("entity.cushion.get_up");
	public static final SoundEvent CUSHION_PLACE = Reg.sound("entity.cushion.place");
	public static final SoundEvent CUSHION_SIT = Reg.sound("entity.cushion.sit");

	public static final SoundType SHELF_MUSHROOM = new SoundType(
		1.0F, 1.0F, SHELF_MUSHROOM_BREAK, SHELF_MUSHROOM_STEP, SHELF_MUSHROOM_PLACE, SoundEvents.EMPTY, SHELF_MUSHROOM_FALL
	);
	public static final SoundType POPLAR_LEAVES = new SoundType(
		1.0F, 1.0F, POPLAR_LEAVES_BREAK, POPLAR_LEAVES_STEP, POPLAR_LEAVES_PLACE, POPLAR_LEAVES_HIT, POPLAR_LEAVES_FALL
	);
	public static final SoundType STRAW_BED = new SoundType(1.0F, 1.0F, STRAW_BED_BREAK, STRAW_BED_STEP, STRAW_BED_PLACE, STRAW_BED_HIT, STRAW_BED_FALL);
	public static final SoundType RED_SHRUB = new SoundType(1.0F, 1.0F, RED_SHRUB_BREAK, SoundEvents.EMPTY, RED_SHRUB_PLACE, SoundEvents.EMPTY, SoundEvents.EMPTY);

	private WildSounds() {
	}

	static void init() {
	}
}
