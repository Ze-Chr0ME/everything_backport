package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.MountsMemories;
import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.Collection;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** 1.21.11: one piglin swordsman in ten carries a golden spear instead, and its brain remembers spear charges. */
@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin {
	private PiglinMixin(EntityType<? extends AbstractPiglin> entityType, Level level) {
		super(entityType, level);
	}

	@ModifyReturnValue(method = "createSpawnWeapon", at = @At("RETURN"))
	private ItemStack everythingBackport$goldenSpear(ItemStack original) {
		return original.is(Items.GOLDEN_SWORD) && this.random.nextInt(10) == 0 ? new ItemStack(MountsItems.GOLDEN_SPEAR) : original;
	}

	@ModifyArg(
		method = "brainProvider",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/Brain;provider(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/world/entity/ai/Brain$Provider;"),
		index = 0
	)
	private Collection<? extends MemoryModuleType<?>> everythingBackport$spearMemories(Collection<? extends MemoryModuleType<?>> memories) {
		return ImmutableList.<MemoryModuleType<?>>builder()
			.addAll(memories)
			.add(
				MountsMemories.SPEAR_FLEEING_TIME,
				MountsMemories.SPEAR_FLEEING_POSITION,
				MountsMemories.SPEAR_CHARGE_POSITION,
				MountsMemories.SPEAR_ENGAGE_TIME,
				MountsMemories.SPEAR_STATUS
			)
			.build();
	}
}
