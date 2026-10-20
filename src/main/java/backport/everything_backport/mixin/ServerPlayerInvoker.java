package backport.everything_backport.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerInvoker {
	@Invoker("nextContainerCounter")
	void everythingBackport$nextContainerCounter();

	@Accessor("containerCounter")
	int everythingBackport$getContainerCounter();

	@Invoker("initMenu")
	void everythingBackport$initMenu(AbstractContainerMenu menu);
}
