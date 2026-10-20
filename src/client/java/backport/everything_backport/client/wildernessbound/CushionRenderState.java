package backport.everything_backport.client.wildernessbound;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class CushionRenderState extends EntityRenderState {
	public Direction direction = Direction.NORTH;
	public ResourceLocation texture = CushionRenderer.texture(net.minecraft.world.item.DyeColor.WHITE);
}
