package backport.everything_backport.mixin;

import java.util.Map;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Parrot.class)
public interface ParrotAccessor {
	@Accessor("MOB_SOUND_MAP")
	static Map<EntityType<?>, SoundEvent> everythingBackport$getMobSoundMap() {
		throw new AssertionError();
	}
}
