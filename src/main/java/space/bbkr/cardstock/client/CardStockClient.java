package space.bbkr.cardstock.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import space.bbkr.cardstock.client.model.CardModelVariantProvider;

public class CardStockClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> new CardModelVariantProvider());
	}
}
