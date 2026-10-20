package backport.everything_backport.copperage;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.copperage.block.CopperGolemStatueBlock;
import backport.everything_backport.copperage.block.WeatheringCopperBlocks;
import backport.everything_backport.registry.Reg;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class CopperAgeItems {
	public static final ToolMaterial COPPER_TOOL_MATERIAL = new ToolMaterial(
		CopperAgeTags.Blocks.INCORRECT_FOR_COPPER_TOOL, 190, 5.0F, 1.0F, 13, CopperAgeTags.Items.COPPER_TOOL_MATERIALS
	);
	public static final ResourceKey<EquipmentAsset> COPPER_EQUIPMENT_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, EverythingBackport.vanilla("copper"));
	public static final ArmorMaterial COPPER_ARMOR_MATERIAL = new ArmorMaterial(
		11, defense(1, 3, 4, 2, 4), 8, CopperAgeSounds.ARMOR_EQUIP_COPPER, 0.0F, 0.0F, CopperAgeTags.Items.REPAIRS_COPPER_ARMOR, COPPER_EQUIPMENT_ASSET
	);

	public static final Item COPPER_SWORD = Reg.item("copper_sword", new Item.Properties().sword(COPPER_TOOL_MATERIAL, 3.0F, -2.4F));
	public static final Item COPPER_SHOVEL = Reg.item("copper_shovel", properties -> new ShovelItem(COPPER_TOOL_MATERIAL, 1.5F, -3.0F, properties));
	public static final Item COPPER_PICKAXE = Reg.item("copper_pickaxe", new Item.Properties().pickaxe(COPPER_TOOL_MATERIAL, 1.0F, -2.8F));
	public static final Item COPPER_AXE = Reg.item("copper_axe", properties -> new AxeItem(COPPER_TOOL_MATERIAL, 7.0F, -3.2F, properties));
	public static final Item COPPER_HOE = Reg.item("copper_hoe", properties -> new HoeItem(COPPER_TOOL_MATERIAL, -1.0F, -2.0F, properties));
	public static final Item COPPER_HELMET = Reg.item("copper_helmet", new Item.Properties().humanoidArmor(COPPER_ARMOR_MATERIAL, ArmorType.HELMET));
	public static final Item COPPER_CHESTPLATE = Reg.item("copper_chestplate", new Item.Properties().humanoidArmor(COPPER_ARMOR_MATERIAL, ArmorType.CHESTPLATE));
	public static final Item COPPER_LEGGINGS = Reg.item("copper_leggings", new Item.Properties().humanoidArmor(COPPER_ARMOR_MATERIAL, ArmorType.LEGGINGS));
	public static final Item COPPER_BOOTS = Reg.item("copper_boots", new Item.Properties().humanoidArmor(COPPER_ARMOR_MATERIAL, ArmorType.BOOTS));
	public static final Item COPPER_HORSE_ARMOR = Reg.item("copper_horse_armor", new Item.Properties().horseArmor(COPPER_ARMOR_MATERIAL));
	public static final Item COPPER_NUGGET = Reg.item("copper_nugget");
	public static final Item COPPER_GOLEM_SPAWN_EGG = Reg.item("copper_golem_spawn_egg", properties -> new SpawnEggItem(CopperAgeEntities.COPPER_GOLEM, properties));

	public static final Item COPPER_TORCH = Reg.blockItem(
		CopperAgeBlocks.COPPER_TORCH, (block, properties) -> new StandingAndWallBlockItem(block, CopperAgeBlocks.COPPER_WALL_TORCH, Direction.DOWN, properties)
	);
	public static final WeatheringCopperItems COPPER_BARS = WeatheringCopperItems.create(CopperAgeBlocks.COPPER_BARS);
	public static final WeatheringCopperItems COPPER_CHAIN = WeatheringCopperItems.create(CopperAgeBlocks.COPPER_CHAIN);
	public static final WeatheringCopperItems COPPER_LANTERN = WeatheringCopperItems.create(CopperAgeBlocks.COPPER_LANTERN);
	public static final List<Item> SHELVES = CopperAgeBlocks.SHELVES.stream()
		.map(block -> Reg.blockItem(block, new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)))
		.toList();
	public static final List<Item> COPPER_CHESTS = CopperAgeBlocks.COPPER_CHESTS.stream().map(Reg::blockItem).toList();
	public static final List<Item> COPPER_GOLEM_STATUES = CopperAgeBlocks.COPPER_GOLEM_STATUES.stream()
		.map(
			block -> Reg.blockItem(
				block,
				new Item.Properties()
					.component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CopperGolemStatueBlock.POSE, CopperGolemStatueBlock.Pose.STANDING))
			)
		)
		.toList();
	public static final List<Item> LIGHTNING_RODS = CopperAgeBlocks.LIGHTNING_RODS.stream()
		.skip(1)
		.map(Reg::blockItem)
		.toList();

	private CopperAgeItems() {
	}

	private static Map<ArmorType, Integer> defense(int boots, int leggings, int chestplate, int helmet, int body) {
		Map<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
		map.put(ArmorType.BOOTS, boots);
		map.put(ArmorType.LEGGINGS, leggings);
		map.put(ArmorType.CHESTPLATE, chestplate);
		map.put(ArmorType.HELMET, helmet);
		map.put(ArmorType.BODY, body);
		return map;
	}

	static void init() {
	}

	public record WeatheringCopperItems(
		Item unaffected, Item exposed, Item weathered, Item oxidized, Item waxed, Item waxedExposed, Item waxedWeathered, Item waxedOxidized
	) {
		static WeatheringCopperItems create(WeatheringCopperBlocks blocks) {
			return new WeatheringCopperItems(
				Reg.blockItem(blocks.unaffected()),
				Reg.blockItem(blocks.exposed()),
				Reg.blockItem(blocks.weathered()),
				Reg.blockItem(blocks.oxidized()),
				Reg.blockItem(blocks.waxed()),
				Reg.blockItem(blocks.waxedExposed()),
				Reg.blockItem(blocks.waxedWeathered()),
				Reg.blockItem(blocks.waxedOxidized())
			);
		}

		public List<Item> asList() {
			return List.of(this.unaffected, this.exposed, this.weathered, this.oxidized, this.waxed, this.waxedExposed, this.waxedWeathered, this.waxedOxidized);
		}
	}

}
