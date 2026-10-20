package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.copperage.CopperAgeItems;
import backport.everything_backport.mountsofmayhem.component.AttackRange;
import backport.everything_backport.mountsofmayhem.component.KineticWeapon;
import backport.everything_backport.mountsofmayhem.component.PiercingWeapon;
import backport.everything_backport.mountsofmayhem.component.SwingAnimation;
import backport.everything_backport.mountsofmayhem.component.SwingAnimationType;
import backport.everything_backport.mountsofmayhem.component.UseEffects;
import backport.everything_backport.registry.Reg;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.damagesource.DamageType;

public final class MountsItems {
	public static final ResourceKey<DamageType> SPEAR_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, backport.everything_backport.EverythingBackport.vanilla("spear"));

	public static final Item WOODEN_SPEAR = Reg.item("wooden_spear", spear(ToolMaterial.WOOD, 0.65F, 0.7F, 0.75F, 5.0F, 14.0F, 10.0F, 5.1F, 15.0F, 4.6F));
	public static final Item STONE_SPEAR = Reg.item("stone_spear", spear(ToolMaterial.STONE, 0.75F, 0.82F, 0.7F, 4.5F, 10.0F, 9.0F, 5.1F, 13.75F, 4.6F));
	public static final Item COPPER_SPEAR = Reg.item(
		"copper_spear", spear(CopperAgeItems.COPPER_TOOL_MATERIAL, 0.85F, 0.82F, 0.65F, 4.0F, 9.0F, 8.25F, 5.1F, 12.5F, 4.6F)
	);
	public static final Item IRON_SPEAR = Reg.item("iron_spear", spear(ToolMaterial.IRON, 0.95F, 0.95F, 0.6F, 2.5F, 8.0F, 6.75F, 5.1F, 11.25F, 4.6F));
	public static final Item GOLDEN_SPEAR = Reg.item("golden_spear", spear(ToolMaterial.GOLD, 0.95F, 0.7F, 0.7F, 3.5F, 10.0F, 8.5F, 5.1F, 13.75F, 4.6F));
	public static final Item DIAMOND_SPEAR = Reg.item("diamond_spear", spear(ToolMaterial.DIAMOND, 1.05F, 1.075F, 0.5F, 3.0F, 7.5F, 6.5F, 5.1F, 10.0F, 4.6F));
	public static final Item NETHERITE_SPEAR = Reg.item(
		"netherite_spear", spear(ToolMaterial.NETHERITE, 1.15F, 1.2F, 0.4F, 2.5F, 7.0F, 5.5F, 5.1F, 8.75F, 4.6F).fireResistant()
	);
	public static final List<Item> SPEARS = List.of(WOODEN_SPEAR, STONE_SPEAR, COPPER_SPEAR, IRON_SPEAR, GOLDEN_SPEAR, DIAMOND_SPEAR, NETHERITE_SPEAR);

	public static final Item NETHERITE_HORSE_ARMOR = Reg.item("netherite_horse_armor", new Item.Properties().horseArmor(ArmorMaterials.NETHERITE).fireResistant());
	public static final Item COPPER_NAUTILUS_ARMOR = Reg.item("copper_nautilus_armor", nautilusArmor(CopperAgeItems.COPPER_ARMOR_MATERIAL));
	public static final Item IRON_NAUTILUS_ARMOR = Reg.item("iron_nautilus_armor", nautilusArmor(ArmorMaterials.IRON));
	public static final Item GOLDEN_NAUTILUS_ARMOR = Reg.item("golden_nautilus_armor", nautilusArmor(ArmorMaterials.GOLD));
	public static final Item DIAMOND_NAUTILUS_ARMOR = Reg.item("diamond_nautilus_armor", nautilusArmor(ArmorMaterials.DIAMOND));
	public static final Item NETHERITE_NAUTILUS_ARMOR = Reg.item("netherite_nautilus_armor", nautilusArmor(ArmorMaterials.NETHERITE).fireResistant());
	public static final List<Item> NAUTILUS_ARMOR = List.of(
		COPPER_NAUTILUS_ARMOR, IRON_NAUTILUS_ARMOR, GOLDEN_NAUTILUS_ARMOR, DIAMOND_NAUTILUS_ARMOR, NETHERITE_NAUTILUS_ARMOR
	);

	public static final Item NAUTILUS_SPAWN_EGG = spawnEgg("nautilus_spawn_egg", MountsEntities.NAUTILUS);
	public static final Item ZOMBIE_NAUTILUS_SPAWN_EGG = spawnEgg("zombie_nautilus_spawn_egg", MountsEntities.ZOMBIE_NAUTILUS);
	public static final Item CAMEL_HUSK_SPAWN_EGG = spawnEgg("camel_husk_spawn_egg", MountsEntities.CAMEL_HUSK);
	public static final Item PARCHED_SPAWN_EGG = spawnEgg("parched_spawn_egg", MountsEntities.PARCHED);

	private MountsItems() {
	}

	/**
	 * 1.21.11's {@code Item.Properties#spear}. Times are in seconds: the swing, the delay before a charge hits, and how
	 * long a charge can dismount, knock back and damage; speeds are blocks per second.
	 */
	private static Item.Properties spear(
		ToolMaterial material,
		float swingSeconds,
		float damageMultiplier,
		float delaySeconds,
		float dismountSeconds,
		float dismountSpeed,
		float knockbackSeconds,
		float knockbackSpeed,
		float damageSeconds,
		float damageRelativeSpeed
	) {
		boolean wood = material == ToolMaterial.WOOD;
		return new Item.Properties()
			.durability(material.durability())
			.repairable(material.repairItems())
			.enchantable(material.enchantmentValue())
			.component(MountsComponents.DAMAGE_TYPE, SPEAR_DAMAGE)
			.component(
				MountsComponents.KINETIC_WEAPON,
				new KineticWeapon(
					10,
					(int)(delaySeconds * 20.0F),
					KineticWeapon.Condition.ofAttackerSpeed((int)(dismountSeconds * 20.0F), dismountSpeed),
					KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackSeconds * 20.0F), knockbackSpeed),
					KineticWeapon.Condition.ofRelativeSpeed((int)(damageSeconds * 20.0F), damageRelativeSpeed),
					0.38F,
					damageMultiplier,
					Optional.of(wood ? MountsSounds.SPEAR_WOOD_USE : MountsSounds.SPEAR_USE),
					Optional.of(wood ? MountsSounds.SPEAR_WOOD_HIT : MountsSounds.SPEAR_HIT)
				)
			)
			.component(
				MountsComponents.PIERCING_WEAPON,
				new PiercingWeapon(
					true, false, Optional.of(wood ? MountsSounds.SPEAR_WOOD_ATTACK : MountsSounds.SPEAR_ATTACK), Optional.of(wood ? MountsSounds.SPEAR_WOOD_HIT : MountsSounds.SPEAR_HIT)
				)
			)
			.component(MountsComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
			.component(MountsComponents.MINIMUM_ATTACK_CHARGE, 1.0F)
			.component(MountsComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(swingSeconds * 20.0F)))
			.attributes(
				ItemAttributeModifiers.builder()
					.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0F + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
					)
					.add(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 1.0F / swingSeconds - 4.0, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
					)
					.build()
			)
			.component(MountsComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
			.component(DataComponents.WEAPON, new Weapon(1));
	}

	private static Item.Properties nautilusArmor(ArmorMaterial material) {
		HolderGetter<EntityType<?>> entityTypes = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ENTITY_TYPE);
		return new Item.Properties()
			.attributes(material.createAttributes(ArmorType.BODY))
			.component(
				DataComponents.EQUIPPABLE,
				Equippable.builder(EquipmentSlot.BODY)
					.setEquipSound(MountsSounds.ARMOR_EQUIP_NAUTILUS)
					.setAsset(material.assetId())
					.setAllowedEntities(entityTypes.getOrThrow(MountsTags.EntityTypes.CAN_WEAR_NAUTILUS_ARMOR))
					.setDamageOnHurt(false)
					.setEquipOnInteract(true)
					.setCanBeSheared(true)
					.setShearingSound(MountsSounds.ARMOR_UNEQUIP_NAUTILUS)
					.build()
			)
			.stacksTo(1);
	}

	private static Item spawnEgg(String name, EntityType<? extends net.minecraft.world.entity.Mob> type) {
		return Reg.item(name, properties -> new SpawnEggItem(type, properties));
	}

	static void init() {
	}
}
