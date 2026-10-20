package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/** The attributes 26.2 adds to every living entity. */
public final class ChaosAttributes {
	public static final Holder<Attribute> AIR_DRAG_MODIFIER = register(
		"air_drag_modifier", new RangedAttribute("attribute.name.air_drag_modifier", 1.0, 0.0, 2048.0).setSyncable(true)
	);
	public static final Holder<Attribute> BELOW_NAME_DISTANCE = register(
		"below_name_distance", new RangedAttribute("attribute.name.below_name_distance", 10.0, 0.0, 512.0).setSyncable(true)
	);
	public static final Holder<Attribute> BOUNCINESS = register("bounciness", new RangedAttribute("attribute.name.bounciness", 0.0, 0.0, 1.0).setSyncable(true));
	public static final Holder<Attribute> FRICTION_MODIFIER = register(
		"friction_modifier", new RangedAttribute("attribute.name.friction_modifier", 1.0, 0.0, 2048.0).setSyncable(true)
	);
	/** 26.2's code asks for {@code attribute.name.name_tag_distance}, but its lang files only have {@code nameplate_distance}. */
	public static final Holder<Attribute> NAME_TAG_DISTANCE = register(
		"name_tag_distance", new RangedAttribute("attribute.name.nameplate_distance", 64.0, 0.0, 512.0).setSyncable(true)
	);

	private ChaosAttributes() {
	}

	private static Holder<Attribute> register(String name, Attribute attribute) {
		return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, EverythingBackport.vanilla(name), attribute);
	}

	/** The attribute's value, or its default for entities whose attribute map predates these attributes. */
	public static double get(LivingEntity entity, Holder<Attribute> attribute) {
		AttributeInstance instance = entity.getAttribute(attribute);
		return instance != null ? instance.getValue() : attribute.value().getDefaultValue();
	}

	/** 26.2's {@code LivingEntity.computeModifiedFriction}. */
	public static float computeModifiedFriction(float friction, float modifier) {
		return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
	}

	static void init() {
	}
}
