package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.ai.SpearUseGoal;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11: one zombified piglin in twenty carries a golden spear, and they charge with it. */
@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinMixin extends Zombie {
	private ZombifiedPiglinMixin(EntityType<? extends Zombie> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "addBehaviourGoals", at = @At("HEAD"))
	private void everythingBackport$spearGoal(CallbackInfo ci) {
		this.goalSelector.addGoal(1, new SpearUseGoal<>((ZombifiedPiglin)(Object)this, 1.0, 1.0, 10.0F, 2.0F));
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
	private void everythingBackport$goldenSpear(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo ci) {
		if (randomSource.nextInt(20) == 0) {
			this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(MountsItems.GOLDEN_SPEAR));
		}
	}
}
