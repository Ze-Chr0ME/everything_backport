package backport.everything_backport.tinytakeover;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public final class TinyTags {
	public static final TagKey<EntityType<?>> CANNOT_BE_AGE_LOCKED = TagKey.create(Registries.ENTITY_TYPE, EverythingBackport.vanilla("cannot_be_age_locked"));
	public static final TagKey<Item> METAL_NUGGETS = TagKey.create(Registries.ITEM, EverythingBackport.vanilla("metal_nuggets"));

	private TinyTags() {
	}
}
