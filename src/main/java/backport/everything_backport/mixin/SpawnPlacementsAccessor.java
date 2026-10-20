package backport.everything_backport.mixin;

import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnPlacements.class)
public interface SpawnPlacementsAccessor {
	@Accessor("DATA_BY_TYPE")
	static Map<EntityType<?>, ?> everythingBackport$getDataByType() {
		throw new AssertionError();
	}
}
