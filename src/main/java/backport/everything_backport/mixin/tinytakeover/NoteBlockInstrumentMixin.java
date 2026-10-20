package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TinySounds;
import java.util.Arrays;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 26.1 adds four trumpet instruments that copper blocks play under a note block. They are appended after the vanilla
 * constants so existing ordinals and switch tables keep working; block states are saved by name, so the order doesn't
 * matter for worlds.
 */
@Mixin(NoteBlockInstrument.class)
public abstract class NoteBlockInstrumentMixin {
	@Shadow
	@Final
	@Mutable
	private static NoteBlockInstrument[] $VALUES;

	@Invoker("<init>")
	private static NoteBlockInstrument everythingBackport$create(
		String internalName, int ordinal, String name, Holder<SoundEvent> sound, NoteBlockInstrument.Type type
	) {
		throw new AssertionError();
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void everythingBackport$addTrumpets(CallbackInfo ci) {
		int base = $VALUES.length;
		NoteBlockInstrument[] values = Arrays.copyOf($VALUES, base + 4);
		values[base] = everythingBackport$create("TRUMPET", base, "trumpet", TinySounds.NOTE_BLOCK_TRUMPET, NoteBlockInstrument.Type.BASE_BLOCK);
		values[base + 1] = everythingBackport$create(
			"TRUMPET_EXPOSED", base + 1, "trumpet_exposed", TinySounds.NOTE_BLOCK_TRUMPET_EXPOSED, NoteBlockInstrument.Type.BASE_BLOCK
		);
		values[base + 2] = everythingBackport$create(
			"TRUMPET_OXIDIZED", base + 2, "trumpet_oxidized", TinySounds.NOTE_BLOCK_TRUMPET_OXIDIZED, NoteBlockInstrument.Type.BASE_BLOCK
		);
		values[base + 3] = everythingBackport$create(
			"TRUMPET_WEATHERED", base + 3, "trumpet_weathered", TinySounds.NOTE_BLOCK_TRUMPET_WEATHERED, NoteBlockInstrument.Type.BASE_BLOCK
		);
		$VALUES = values;
	}
}
