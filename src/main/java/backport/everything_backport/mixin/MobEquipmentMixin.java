package backport.everything_backport.mixin;

import backport.everything_backport.copperage.CopperAgeItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 1.21.9 adds copper armor as the second tier of armor that mobs spawn with. */
@Mixin(Mob.class)
public abstract class MobEquipmentMixin {
	@Unique
	private static final EquipmentSlot[] EQUIPMENT_POPULATION_ORDER = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo ci) {
		Mob mob = (Mob)(Object)this;
		if (randomSource.nextFloat() < 0.15F * difficultyInstance.getSpecialMultiplier()) {
			int tier = randomSource.nextInt(3);

			for (int i = 1; i <= 3.0F; i++) {
				if (randomSource.nextFloat() < 0.1087F) {
					tier++;
				}
			}

			float stopChance = mob.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;
			boolean first = true;

			for (EquipmentSlot slot : EQUIPMENT_POPULATION_ORDER) {
				ItemStack itemStack = mob.getItemBySlot(slot);
				if (!first && randomSource.nextFloat() < stopChance) {
					break;
				}

				first = false;
				if (itemStack.isEmpty()) {
					Item item = Mob.getEquipmentForSlot(slot, tier);
					if (item != null) {
						mob.setItemSlot(slot, new ItemStack(item));
					}
				}
			}
		}

		ci.cancel();
	}

	@Inject(method = "getEquipmentForSlot", at = @At("HEAD"), cancellable = true)
	private static void everythingBackport$getEquipmentForSlot(EquipmentSlot slot, int tier, CallbackInfoReturnable<Item> cir) {
		cir.setReturnValue(everythingBackport$equipment(slot, tier));
	}

	@Unique
	@Nullable
	private static Item everythingBackport$equipment(EquipmentSlot slot, int tier) {
		Item[] items = switch (slot) {
			case HEAD -> new Item[] {
				Items.LEATHER_HELMET, CopperAgeItems.COPPER_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET
			};
			case CHEST -> new Item[] {
				Items.LEATHER_CHESTPLATE, CopperAgeItems.COPPER_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE,
				Items.DIAMOND_CHESTPLATE
			};
			case LEGS -> new Item[] {
				Items.LEATHER_LEGGINGS, CopperAgeItems.COPPER_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS
			};
			case FEET -> new Item[] {
				Items.LEATHER_BOOTS, CopperAgeItems.COPPER_BOOTS, Items.GOLDEN_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS
			};
			default -> null;
		};
		return items != null && tier >= 0 && tier < items.length ? items[tier] : null;
	}
}
