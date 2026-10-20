package backport.everything_backport.copperage;

import backport.everything_backport.registry.Reg;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public final class CopperAgeSounds {
	public static final Holder<SoundEvent> ARMOR_EQUIP_COPPER = Reg.soundHolder("item.armor.equip_copper");
	public static final SoundEvent COPPER_CHEST_CLOSE = Reg.sound("block.copper_chest.close");
	public static final SoundEvent COPPER_CHEST_OPEN = Reg.sound("block.copper_chest.open");
	public static final SoundEvent COPPER_CHEST_WEATHERED_CLOSE = Reg.sound("block.copper_chest_weathered.close");
	public static final SoundEvent COPPER_CHEST_WEATHERED_OPEN = Reg.sound("block.copper_chest_weathered.open");
	public static final SoundEvent COPPER_CHEST_OXIDIZED_CLOSE = Reg.sound("block.copper_chest_oxidized.close");
	public static final SoundEvent COPPER_CHEST_OXIDIZED_OPEN = Reg.sound("block.copper_chest_oxidized.open");
	public static final SoundEvent COPPER_GOLEM_STEP = Reg.sound("entity.copper_golem.step");
	public static final SoundEvent COPPER_GOLEM_HURT = Reg.sound("entity.copper_golem.hurt");
	public static final SoundEvent COPPER_GOLEM_DEATH = Reg.sound("entity.copper_golem.death");
	public static final SoundEvent COPPER_GOLEM_WEATHERED_STEP = Reg.sound("entity.copper_golem_weathered.step");
	public static final SoundEvent COPPER_GOLEM_WEATHERED_HURT = Reg.sound("entity.copper_golem_weathered.hurt");
	public static final SoundEvent COPPER_GOLEM_WEATHERED_DEATH = Reg.sound("entity.copper_golem_weathered.death");
	public static final SoundEvent COPPER_GOLEM_OXIDIZED_STEP = Reg.sound("entity.copper_golem_oxidized.step");
	public static final SoundEvent COPPER_GOLEM_OXIDIZED_HURT = Reg.sound("entity.copper_golem_oxidized.hurt");
	public static final SoundEvent COPPER_GOLEM_OXIDIZED_DEATH = Reg.sound("entity.copper_golem_oxidized.death");
	public static final SoundEvent COPPER_GOLEM_SPIN = Reg.sound("entity.copper_golem.spin");
	public static final SoundEvent COPPER_GOLEM_WEATHERED_SPIN = Reg.sound("entity.copper_golem_weathered.spin");
	public static final SoundEvent COPPER_GOLEM_OXIDIZED_SPIN = Reg.sound("entity.copper_golem_oxidized.spin");
	public static final SoundEvent COPPER_GOLEM_ITEM_GET = Reg.sound("entity.copper_golem.no_item_get");
	public static final SoundEvent COPPER_GOLEM_ITEM_NO_GET = Reg.sound("entity.copper_golem.no_item_no_get");
	public static final SoundEvent COPPER_GOLEM_ITEM_DROP = Reg.sound("entity.copper_golem.item_drop");
	public static final SoundEvent COPPER_GOLEM_ITEM_NO_DROP = Reg.sound("entity.copper_golem.item_no_drop");
	public static final SoundEvent COPPER_GOLEM_BECOME_STATUE = Reg.sound("entity.copper_golem_become_statue");
	public static final SoundEvent COPPER_GOLEM_STATUE_BREAK = Reg.sound("block.copper_golem_statue.break");
	public static final SoundEvent COPPER_GOLEM_STATUE_PLACE = Reg.sound("block.copper_golem_statue.place");
	public static final SoundEvent COPPER_GOLEM_STATUE_HIT = Reg.sound("block.copper_golem_statue.hit");
	public static final SoundEvent COPPER_GOLEM_STATUE_STEP = Reg.sound("block.copper_golem_statue.step");
	public static final SoundEvent COPPER_GOLEM_STATUE_FALL = Reg.sound("block.copper_golem_statue.fall");
	public static final SoundEvent COPPER_GOLEM_SPAWN = Reg.sound("entity.copper_golem.spawn");
	public static final SoundEvent COPPER_GOLEM_SHEAR = Reg.sound("entity.copper_golem.shear");
	public static final SoundEvent SHELF_ACTIVATE = Reg.sound("block.shelf.activate");
	public static final SoundEvent SHELF_BREAK = Reg.sound("block.shelf.break");
	public static final SoundEvent SHELF_DEACTIVATE = Reg.sound("block.shelf.deactivate");
	public static final SoundEvent SHELF_FALL = Reg.sound("block.shelf.fall");
	public static final SoundEvent SHELF_HIT = Reg.sound("block.shelf.hit");
	public static final SoundEvent SHELF_MULTI_SWAP = Reg.sound("block.shelf.multi_swap");
	public static final SoundEvent SHELF_PLACE = Reg.sound("block.shelf.place");
	public static final SoundEvent SHELF_PLACE_ITEM = Reg.sound("block.shelf.place_item");
	public static final SoundEvent SHELF_SINGLE_SWAP = Reg.sound("block.shelf.single_swap");
	public static final SoundEvent SHELF_STEP = Reg.sound("block.shelf.step");
	public static final SoundEvent SHELF_TAKE_ITEM = Reg.sound("block.shelf.take_item");
	public static final SoundEvent WEATHER_END_FLASH = Reg.sound("weather.end_flash");

	public static final SoundType COPPER_GOLEM_STATUE_SOUND_TYPE = new SoundType(
		1.0F, 1.0F, COPPER_GOLEM_STATUE_BREAK, COPPER_GOLEM_STATUE_STEP, COPPER_GOLEM_STATUE_PLACE, COPPER_GOLEM_STATUE_HIT, COPPER_GOLEM_STATUE_FALL
	);
	public static final SoundType SHELF_SOUND_TYPE = new SoundType(1.0F, 1.0F, SHELF_BREAK, SHELF_STEP, SHELF_PLACE, SHELF_HIT, SHELF_FALL);

	private CopperAgeSounds() {
	}

	static void init() {
	}
}
