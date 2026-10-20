package backport.everything_backport;

import backport.everything_backport.client.copperage.CopperAgeClient;
import backport.everything_backport.client.mountsofmayhem.MountsOfMayhemClient;
import net.fabricmc.api.ClientModInitializer;

public class EverythingBackportClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CopperAgeClient.init();
		MountsOfMayhemClient.init();
	}
}
