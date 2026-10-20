package backport.everything_backport.tinytakeover;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.tinytakeover.sound.CatSoundVariant;
import backport.everything_backport.tinytakeover.sound.ChickenSoundVariant;
import backport.everything_backport.tinytakeover.sound.CowSoundVariant;
import backport.everything_backport.tinytakeover.sound.PigSoundVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;

/** Synced entity data types for the 26.1 sound variants. */
public final class TinyEntityData {
	public static final EntityDataSerializer<Holder<CatSoundVariant>> CAT_SOUND_VARIANT = EntityDataSerializer.forValueType(CatSoundVariant.STREAM_CODEC);
	public static final EntityDataSerializer<Holder<ChickenSoundVariant>> CHICKEN_SOUND_VARIANT = EntityDataSerializer.forValueType(
		ChickenSoundVariant.STREAM_CODEC
	);
	public static final EntityDataSerializer<Holder<CowSoundVariant>> COW_SOUND_VARIANT = EntityDataSerializer.forValueType(CowSoundVariant.STREAM_CODEC);
	public static final EntityDataSerializer<Holder<PigSoundVariant>> PIG_SOUND_VARIANT = EntityDataSerializer.forValueType(PigSoundVariant.STREAM_CODEC);

	private TinyEntityData() {
	}

	static void init() {
		FabricTrackedDataRegistry.register(EverythingBackport.id("cat_sound_variant"), CAT_SOUND_VARIANT);
		FabricTrackedDataRegistry.register(EverythingBackport.id("chicken_sound_variant"), CHICKEN_SOUND_VARIANT);
		FabricTrackedDataRegistry.register(EverythingBackport.id("cow_sound_variant"), COW_SOUND_VARIANT);
		FabricTrackedDataRegistry.register(EverythingBackport.id("pig_sound_variant"), PIG_SOUND_VARIANT);
	}
}
