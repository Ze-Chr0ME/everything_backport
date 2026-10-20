package backport.everything_backport.mixin;

import backport.everything_backport.mountsofmayhem.ai.SpearApproach;
import backport.everything_backport.mountsofmayhem.ai.SpearAttack;
import backport.everything_backport.mountsofmayhem.ai.SpearRetreat;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** 1.21.11 piglins charge with spears: the spear behaviors run right after walking towards the target. */
@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
	@ModifyArg(
		method = "initFightActivity",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/Brain;addActivityAndRemoveMemoryWhenStopped(Lnet/minecraft/world/entity/schedule/Activity;ILcom/google/common/collect/ImmutableList;Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)V"
		),
		index = 2
	)
	private static ImmutableList<? extends BehaviorControl<? super Piglin>> everythingBackport$spearBehaviors(
		ImmutableList<? extends BehaviorControl<? super Piglin>> behaviors
	) {
		List<BehaviorControl<? super Piglin>> list = new ArrayList<>(behaviors);
		// After StopAttackingIfTargetInvalid, BackUpIfTooClose and SetWalkTargetFromAttackTargetIfTargetOutOfReach.
		int index = Math.min(3, list.size());
		list.addAll(index, List.of(new SpearApproach(1.0, 10.0F), new SpearAttack(1.0, 1.0, 10.0F, 2.0F), new SpearRetreat(1.0)));
		return ImmutableList.copyOf(list);
	}
}
