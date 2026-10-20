package backport.everything_backport.client.mountsofmayhem.renderer;

import backport.everything_backport.mountsofmayhem.entity.ZombieNautilusVariant;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NautilusRenderState extends LivingEntityRenderState {
	public ItemStack saddle = ItemStack.EMPTY;
	public ItemStack bodyArmorItem = ItemStack.EMPTY;
	@Nullable
	public ZombieNautilusVariant variant;
}
