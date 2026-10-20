package backport.everything_backport.chaoscubed;

import backport.everything_backport.EverythingBackport;
import backport.everything_backport.chaoscubed.particle.GeyserBaseParticleOptions;
import backport.everything_backport.chaoscubed.particle.GeyserParticleOptions;
import backport.everything_backport.registry.Reg;
import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class ChaosParticles {
	public static final SimpleParticleType SULFUR_BUBBLES = Reg.particle("sulfur_bubbles", false);
	public static final SimpleParticleType NOXIOUS_GAS = Reg.particle("noxious_gas", false);
	public static final SimpleParticleType NOXIOUS_GAS_CLOUD = Reg.particle("noxious_gas_cloud", false);
	public static final ParticleType<GeyserParticleOptions> GEYSER = register("geyser", GeyserParticleOptions::codec, GeyserParticleOptions::streamCodec);
	public static final ParticleType<GeyserBaseParticleOptions> GEYSER_BASE = register(
		"geyser_base", GeyserBaseParticleOptions::codec, GeyserBaseParticleOptions::streamCodec
	);
	public static final ParticleType<GeyserBaseParticleOptions> GEYSER_POOF = register(
		"geyser_poof", GeyserBaseParticleOptions::codec, GeyserBaseParticleOptions::streamCodec
	);
	public static final ParticleType<GeyserParticleOptions> GEYSER_PLUME = register("geyser_plume", GeyserParticleOptions::codec, GeyserParticleOptions::streamCodec);
	public static final SimpleParticleType SULFUR_CUBE_GOO = Reg.particle("sulfur_cube_goo", false);

	private ChaosParticles() {
	}

	private static <T extends ParticleOptions> ParticleType<T> register(
		String name,
		Function<ParticleType<T>, MapCodec<T>> codec,
		Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec
	) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, EverythingBackport.vanilla(name), FabricParticleTypes.complex(true, codec, streamCodec));
	}

	static void init() {
	}
}
