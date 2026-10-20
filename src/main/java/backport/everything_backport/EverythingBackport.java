package backport.everything_backport;

import backport.everything_backport.chaoscubed.ChaosCubed;
import backport.everything_backport.copperage.CopperAge;
import backport.everything_backport.mountsofmayhem.MountsOfMayhem;
import backport.everything_backport.tinytakeover.TinyTakeover;
import backport.everything_backport.wildernessbound.WildernessBound;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EverythingBackport implements ModInitializer {
    public static final String MOD_ID = "everything_backport";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[Everything Backport] Initializing...");
		CopperAge.init();
		MountsOfMayhem.init();
		TinyTakeover.init();
		ChaosCubed.init();
		WildernessBound.init();
    }

    /** Backported content keeps its vanilla id so worlds carry over when the pack moves to a newer version. */
    public static ResourceLocation vanilla(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
