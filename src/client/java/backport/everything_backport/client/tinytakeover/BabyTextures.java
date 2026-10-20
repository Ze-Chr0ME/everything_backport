package backport.everything_backport.client.tinytakeover;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

/**
 * The textures 26.1 gives its redesigned babies, keyed by the 1.21.7 adult texture they replace. Generated from the
 * 26.1.2 client: every {@code *_baby.png} next to the adult texture it belongs to, following 26.1's texture renames.
 */
public final class BabyTextures {
	private static final Map<ResourceLocation, ResourceLocation> BY_ADULT = new HashMap<>();

	static {
		put("textures/entity/armadillo.png", "textures/entity/armadillo/armadillo_baby.png");
		put("textures/entity/axolotl/axolotl_blue.png", "textures/entity/axolotl/axolotl_blue_baby.png");
		put("textures/entity/axolotl/axolotl_cyan.png", "textures/entity/axolotl/axolotl_cyan_baby.png");
		put("textures/entity/axolotl/axolotl_gold.png", "textures/entity/axolotl/axolotl_gold_baby.png");
		put("textures/entity/axolotl/axolotl_lucy.png", "textures/entity/axolotl/axolotl_lucy_baby.png");
		put("textures/entity/axolotl/axolotl_wild.png", "textures/entity/axolotl/axolotl_wild_baby.png");
		put("textures/entity/bear/polarbear.png", "textures/entity/bear/polarbear_baby.png");
		put("textures/entity/bee/bee_angry.png", "textures/entity/bee/bee_angry_baby.png");
		put("textures/entity/bee/bee_angry_nectar.png", "textures/entity/bee/bee_angry_nectar_baby.png");
		put("textures/entity/bee/bee.png", "textures/entity/bee/bee_baby.png");
		put("textures/entity/bee/bee_nectar.png", "textures/entity/bee/bee_nectar_baby.png");
		put("textures/entity/camel/camel.png", "textures/entity/camel/camel_baby.png");
		put("textures/entity/cat/all_black.png", "textures/entity/cat/cat_all_black_baby.png");
		put("textures/entity/cat/black.png", "textures/entity/cat/cat_black_baby.png");
		put("textures/entity/cat/british_shorthair.png", "textures/entity/cat/cat_british_shorthair_baby.png");
		put("textures/entity/cat/calico.png", "textures/entity/cat/cat_calico_baby.png");
		put("textures/entity/cat/cat_collar.png", "textures/entity/cat/cat_collar_baby.png");
		put("textures/entity/cat/jellie.png", "textures/entity/cat/cat_jellie_baby.png");
		put("textures/entity/cat/persian.png", "textures/entity/cat/cat_persian_baby.png");
		put("textures/entity/cat/ragdoll.png", "textures/entity/cat/cat_ragdoll_baby.png");
		put("textures/entity/cat/red.png", "textures/entity/cat/cat_red_baby.png");
		put("textures/entity/cat/siamese.png", "textures/entity/cat/cat_siamese_baby.png");
		put("textures/entity/cat/tabby.png", "textures/entity/cat/cat_tabby_baby.png");
		put("textures/entity/cat/white.png", "textures/entity/cat/cat_white_baby.png");
		put("textures/entity/cat/ocelot.png", "textures/entity/cat/ocelot_baby.png");
		put("textures/entity/chicken/cold_chicken.png", "textures/entity/chicken/chicken_cold_baby.png");
		put("textures/entity/chicken/temperate_chicken.png", "textures/entity/chicken/chicken_temperate_baby.png");
		put("textures/entity/chicken/warm_chicken.png", "textures/entity/chicken/chicken_warm_baby.png");
		put("textures/entity/cow/cold_cow.png", "textures/entity/cow/cow_cold_baby.png");
		put("textures/entity/cow/temperate_cow.png", "textures/entity/cow/cow_temperate_baby.png");
		put("textures/entity/cow/warm_cow.png", "textures/entity/cow/cow_warm_baby.png");
		put("textures/entity/cow/brown_mooshroom.png", "textures/entity/cow/mooshroom_brown_baby.png");
		put("textures/entity/cow/red_mooshroom.png", "textures/entity/cow/mooshroom_red_baby.png");
		put("textures/entity/dolphin.png", "textures/entity/dolphin/dolphin_baby.png");
		put("textures/entity/fox/fox.png", "textures/entity/fox/fox_baby.png");
		put("textures/entity/fox/fox_sleep.png", "textures/entity/fox/fox_sleep_baby.png");
		put("textures/entity/fox/snow_fox.png", "textures/entity/fox/fox_snow_baby.png");
		put("textures/entity/fox/snow_fox_sleep.png", "textures/entity/fox/fox_snow_sleep_baby.png");
		put("textures/entity/goat/goat.png", "textures/entity/goat/goat_baby.png");
		put("textures/entity/hoglin/hoglin.png", "textures/entity/hoglin/hoglin_baby.png");
		put("textures/entity/hoglin/zoglin.png", "textures/entity/hoglin/zoglin_baby.png");
		put("textures/entity/horse/donkey.png", "textures/entity/horse/donkey_baby.png");
		put("textures/entity/horse/horse_black.png", "textures/entity/horse/horse_black_baby.png");
		put("textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_brown_baby.png");
		put("textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_chestnut_baby.png");
		put("textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_creamy_baby.png");
		put("textures/entity/horse/horse_darkbrown.png", "textures/entity/horse/horse_darkbrown_baby.png");
		put("textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_gray_baby.png");
		put("textures/entity/horse/horse_markings_blackdots.png", "textures/entity/horse/horse_markings_blackdots_baby.png");
		put("textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_white_baby.png");
		put("textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_whitedots_baby.png");
		put("textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitefield_baby.png");
		put("textures/entity/horse/horse_skeleton.png", "textures/entity/horse/horse_skeleton_baby.png");
		put("textures/entity/horse/horse_white.png", "textures/entity/horse/horse_white_baby.png");
		put("textures/entity/horse/horse_zombie.png", "textures/entity/horse/horse_zombie_baby.png");
		put("textures/entity/horse/mule.png", "textures/entity/horse/mule_baby.png");
		put("textures/entity/llama/brown.png", "textures/entity/llama/llama_brown_baby.png");
		put("textures/entity/llama/creamy.png", "textures/entity/llama/llama_creamy_baby.png");
		put("textures/entity/llama/gray.png", "textures/entity/llama/llama_gray_baby.png");
		put("textures/entity/llama/white.png", "textures/entity/llama/llama_white_baby.png");
		put("textures/entity/panda/aggressive_panda.png", "textures/entity/panda/aggressive_panda_baby.png");
		put("textures/entity/panda/brown_panda.png", "textures/entity/panda/brown_panda_baby.png");
		put("textures/entity/panda/lazy_panda.png", "textures/entity/panda/lazy_panda_baby.png");
		put("textures/entity/panda/panda.png", "textures/entity/panda/panda_baby.png");
		put("textures/entity/panda/playful_panda.png", "textures/entity/panda/playful_panda_baby.png");
		put("textures/entity/panda/weak_panda.png", "textures/entity/panda/weak_panda_baby.png");
		put("textures/entity/panda/worried_panda.png", "textures/entity/panda/worried_panda_baby.png");
		put("textures/entity/pig/cold_pig.png", "textures/entity/pig/pig_cold_baby.png");
		put("textures/entity/pig/temperate_pig.png", "textures/entity/pig/pig_temperate_baby.png");
		put("textures/entity/pig/warm_pig.png", "textures/entity/pig/pig_warm_baby.png");
		put("textures/entity/piglin/piglin.png", "textures/entity/piglin/piglin_baby.png");
		put("textures/entity/piglin/zombified_piglin.png", "textures/entity/piglin/zombified_piglin_baby.png");
		put("textures/entity/sheep/sheep.png", "textures/entity/sheep/sheep_baby.png");
		put("textures/entity/sheep/sheep_wool.png", "textures/entity/sheep/sheep_wool_baby.png");
		put("textures/entity/sniffer/sniffer.png", "textures/entity/sniffer/snifflet.png");
		put("textures/entity/squid/glow_squid.png", "textures/entity/squid/glow_squid_baby.png");
		put("textures/entity/squid/squid.png", "textures/entity/squid/squid_baby.png");
		put("textures/entity/strider/strider.png", "textures/entity/strider/strider_baby.png");
		put("textures/entity/strider/strider_cold.png", "textures/entity/strider/strider_cold_baby.png");
		put("textures/entity/turtle/big_sea_turtle.png", "textures/entity/turtle/turtle_baby.png");
		put("textures/entity/villager/villager.png", "textures/entity/villager/villager_baby.png");
		put("textures/entity/wolf/wolf_angry.png", "textures/entity/wolf/wolf_angry_baby.png");
		put("textures/entity/wolf/wolf_ashen_angry.png", "textures/entity/wolf/wolf_ashen_angry_baby.png");
		put("textures/entity/wolf/wolf_ashen.png", "textures/entity/wolf/wolf_ashen_baby.png");
		put("textures/entity/wolf/wolf_ashen_tame.png", "textures/entity/wolf/wolf_ashen_tame_baby.png");
		put("textures/entity/wolf/wolf.png", "textures/entity/wolf/wolf_baby.png");
		put("textures/entity/wolf/wolf_black_angry.png", "textures/entity/wolf/wolf_black_angry_baby.png");
		put("textures/entity/wolf/wolf_black.png", "textures/entity/wolf/wolf_black_baby.png");
		put("textures/entity/wolf/wolf_black_tame.png", "textures/entity/wolf/wolf_black_tame_baby.png");
		put("textures/entity/wolf/wolf_chestnut_angry.png", "textures/entity/wolf/wolf_chestnut_angry_baby.png");
		put("textures/entity/wolf/wolf_chestnut.png", "textures/entity/wolf/wolf_chestnut_baby.png");
		put("textures/entity/wolf/wolf_chestnut_tame.png", "textures/entity/wolf/wolf_chestnut_tame_baby.png");
		put("textures/entity/wolf/wolf_collar.png", "textures/entity/wolf/wolf_collar_baby.png");
		put("textures/entity/wolf/wolf_rusty_angry.png", "textures/entity/wolf/wolf_rusty_angry_baby.png");
		put("textures/entity/wolf/wolf_rusty.png", "textures/entity/wolf/wolf_rusty_baby.png");
		put("textures/entity/wolf/wolf_rusty_tame.png", "textures/entity/wolf/wolf_rusty_tame_baby.png");
		put("textures/entity/wolf/wolf_snowy_angry.png", "textures/entity/wolf/wolf_snowy_angry_baby.png");
		put("textures/entity/wolf/wolf_snowy.png", "textures/entity/wolf/wolf_snowy_baby.png");
		put("textures/entity/wolf/wolf_snowy_tame.png", "textures/entity/wolf/wolf_snowy_tame_baby.png");
		put("textures/entity/wolf/wolf_spotted_angry.png", "textures/entity/wolf/wolf_spotted_angry_baby.png");
		put("textures/entity/wolf/wolf_spotted.png", "textures/entity/wolf/wolf_spotted_baby.png");
		put("textures/entity/wolf/wolf_spotted_tame.png", "textures/entity/wolf/wolf_spotted_tame_baby.png");
		put("textures/entity/wolf/wolf_striped_angry.png", "textures/entity/wolf/wolf_striped_angry_baby.png");
		put("textures/entity/wolf/wolf_striped.png", "textures/entity/wolf/wolf_striped_baby.png");
		put("textures/entity/wolf/wolf_striped_tame.png", "textures/entity/wolf/wolf_striped_tame_baby.png");
		put("textures/entity/wolf/wolf_tame.png", "textures/entity/wolf/wolf_tame_baby.png");
		put("textures/entity/wolf/wolf_woods_angry.png", "textures/entity/wolf/wolf_woods_angry_baby.png");
		put("textures/entity/wolf/wolf_woods.png", "textures/entity/wolf/wolf_woods_baby.png");
		put("textures/entity/wolf/wolf_woods_tame.png", "textures/entity/wolf/wolf_woods_tame_baby.png");
		put("textures/entity/zombie/drowned.png", "textures/entity/zombie/drowned_baby.png");
		put("textures/entity/zombie/drowned_outer_layer.png", "textures/entity/zombie/drowned_outer_layer_baby.png");
		put("textures/entity/zombie/husk.png", "textures/entity/zombie/husk_baby.png");
		put("textures/entity/zombie/zombie.png", "textures/entity/zombie/zombie_baby.png");
		put("textures/entity/zombie_villager/zombie_villager.png", "textures/entity/zombie_villager/zombie_villager_baby.png");
	}

	private BabyTextures() {
	}

	private static void put(String adult, String baby) {
		BY_ADULT.put(ResourceLocation.withDefaultNamespace(adult), ResourceLocation.withDefaultNamespace(baby));
	}

	/** The baby texture for an adult texture, or the adult texture itself when 26.1 has no baby version of it. */
	public static ResourceLocation forBaby(ResourceLocation adult) {
		return BY_ADULT.getOrDefault(adult, adult);
	}
}
