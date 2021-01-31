package space.bbkr.cardstock.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import space.bbkr.cardstock.client.model.CardModelVariantProvider;

import java.util.Collection;

public class CardStockClient implements ClientModInitializer {
	private static final int START = "models/item/".length();
	private static final int JSON_END = ".json".length();

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> new CardModelVariantProvider());
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, consumer) -> {
			Collection<Identifier> cards = manager.findResources("models/item/card", string -> string.endsWith(".json"));
			for (Identifier id : cards) {
				consumer.accept(new ModelIdentifier(new Identifier(id.getNamespace(), id.getPath().substring(START, id.getPath().length() - JSON_END)), "inventory"));
			}
		});
	}
}
