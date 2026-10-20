package backport.everything_backport.mixin.tinytakeover;

import backport.everything_backport.tinytakeover.TrumpetInstruments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Copper blocks play the 26.1 trumpet under a note block. */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateInstrumentMixin {
	@Shadow
	public abstract Block getBlock();

	@Inject(method = "instrument", at = @At("RETURN"), cancellable = true)
	private void everythingBackport$trumpet(CallbackInfoReturnable<NoteBlockInstrument> cir) {
		NoteBlockInstrument trumpet = TrumpetInstruments.forBlock(this.getBlock());
		if (trumpet != null) {
			cir.setReturnValue(trumpet);
		}
	}
}
