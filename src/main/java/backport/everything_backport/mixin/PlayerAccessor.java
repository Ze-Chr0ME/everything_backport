package backport.everything_backport.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {
	@Accessor("attackStrengthTicker")
	int everythingBackport$getAttackStrengthTicker();

	@Accessor("attackStrengthTicker")
	void everythingBackport$setAttackStrengthTicker(int ticks);

	@Accessor("currentImpulseContextResetGraceTime")
	int everythingBackport$getImpulseGraceTime();

	@Accessor("currentImpulseContextResetGraceTime")
	void everythingBackport$setImpulseGraceTime(int ticks);
}
