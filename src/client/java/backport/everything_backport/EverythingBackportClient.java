package backport.everything_backport;

import backport.everything_backport.client.chaoscubed.ChaosCubedClient;
import backport.everything_backport.client.wildernessbound.WildernessBoundClient;
import backport.everything_backport.client.copperage.CopperAgeClient;
import backport.everything_backport.client.mountsofmayhem.MountsOfMayhemClient;
import backport.everything_backport.client.tinytakeover.TinyTakeoverClient;
import net.fabricmc.api.ClientModInitializer;

public class EverythingBackportClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CopperAgeClient.init();
		MountsOfMayhemClient.init();
		TinyTakeoverClient.init();
		ChaosCubedClient.init();
		WildernessBoundClient.init();
	}
}
