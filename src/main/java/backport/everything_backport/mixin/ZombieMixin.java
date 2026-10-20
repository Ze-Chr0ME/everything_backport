package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.MountsItems;
import backport.everything_backport.mountsofmayhem.ai.SpearUseGoal;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** 1.21.11: zombies (and husks) can spawn with an iron spear and charge with it. */
@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
	private ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@WrapOperation(
		method = "addBehaviourGoals",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 0)
	)
	private void everythingBackport$spearGoal(GoalSelector goalSelector, int priority, Goal goal, Operation<Void> original) {
		if (goal instanceof ZombieAttackGoal) {
			original.call(goalSelector, 2, new SpearUseGoal<>((Zombie)(Object)this, 1.0, 1.0, 10.0F, 2.0F));
			original.call(goalSelector, 3, goal);
		} else {
			original.call(goalSelector, priority, goal);
		}
	}

	@Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
	private void everythingBackport$spearEquipment(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo ci) {
		super.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
		if (randomSource.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
			int weapon = randomSource.nextInt(6);
			if (weapon == 0) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
			} else if (weapon == 1) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(MountsItems.IRON_SPEAR));
			} else {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
			}
		}

		ci.cancel();
	}
}
