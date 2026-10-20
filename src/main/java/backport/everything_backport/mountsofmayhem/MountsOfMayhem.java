package backport.everything_backport.mountsofmayhem;

import backport.everything_backport.mixin.ParrotAccessor;
import backport.everything_backport.mountsofmayhem.enchantment.MountsEnchantments;
import backport.everything_backport.mountsofmayhem.inventory.MountMenus;
import backport.everything_backport.mountsofmayhem.network.StabPayload;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Mounts of Mayhem (Java Edition 1.21.11). */
public final class MountsOfMayhem {
	public static final ResourceLocation NAUTILUS_ONE_CM = registerCustomStat("nautilus_one_cm", StatFormatter.DISTANCE);

	private MountsOfMayhem() {
	}

	public static void init() {
		MountsSounds.init();
		MountsComponents.init();
		MountsRegistries.init();
		MountsEffects.init();
		MountsMemories.init();
		MountsEnchantments.init();
		MountsTriggers.init();
		MountsEntities.init();
		MountsItems.init();
		StabPayload.init();
		MountMenus.init();
		registerParrotImitations();
		registerCreativeTabs();
	}

	private static ResourceLocation registerCustomStat(String name, StatFormatter formatter) {
		ResourceLocation id = backport.everything_backport.EverythingBackport.vanilla(name);
		Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
		Stats.CUSTOM.get(id, formatter);
		return id;
	}

	private static void registerParrotImitations() {
		Map<EntityType<?>, SoundEvent> sounds = ParrotAccessor.everythingBackport$getMobSoundMap();
		sounds.put(MountsEntities.CAMEL_HUSK, MountsSounds.PARROT_IMITATE_CAMEL_HUSK);
		sounds.put(MountsEntities.PARCHED, MountsSounds.PARROT_IMITATE_PARCHED);
		sounds.put(EntityType.ZOMBIE_HORSE, MountsSounds.PARROT_IMITATE_ZOMBIE_HORSE);
		sounds.put(MountsEntities.ZOMBIE_NAUTILUS, MountsSounds.PARROT_IMITATE_ZOMBIE_NAUTILUS);
	}

	private static void registerCreativeTabs() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.addAfter(Items.NETHERITE_SWORD, stacks(MountsItems.SPEARS));
			entries.addAfter(Items.DIAMOND_HORSE_ARMOR, MountsItems.NETHERITE_HORSE_ARMOR);
			entries.addAfter(Items.WOLF_ARMOR, stacks(MountsItems.NAUTILUS_ARMOR));
		});
		// 1.21.7 sorts spawn eggs alphabetically.
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
			entries.addAfter(Items.CAMEL_SPAWN_EGG, MountsItems.CAMEL_HUSK_SPAWN_EGG);
			entries.addAfter(Items.MULE_SPAWN_EGG, MountsItems.NAUTILUS_SPAWN_EGG);
			entries.addAfter(Items.PANDA_SPAWN_EGG, MountsItems.PARCHED_SPAWN_EGG);
			entries.addAfter(Items.ZOMBIE_HORSE_SPAWN_EGG, MountsItems.ZOMBIE_NAUTILUS_SPAWN_EGG);
		});
	}

	private static List<ItemStack> stacks(List<Item> items) {
		return items.stream().map(ItemStack::new).toList();
	}
}
