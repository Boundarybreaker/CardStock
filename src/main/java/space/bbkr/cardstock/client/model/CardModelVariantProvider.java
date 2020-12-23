package space.bbkr.cardstock.client.model;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import space.bbkr.cardstock.CardStock;
import space.bbkr.cardstock.data.CardManager;
import space.bbkr.cardstock.data.CardSet;

import java.util.*;

public class CardModelVariantProvider implements ModelVariantProvider {
	private static final ModelIdentifier CARD = new ModelIdentifier(new Identifier(CardStock.MODID, "card"), "inventory");
	@Override
	public @Nullable UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) throws ModelProviderException {
		if (modelId.equals(CARD)) {
			Map<Identifier, UnbakedModel> models = new HashMap<>();
			for (Identifier id : getCardModels()) {
				UnbakedModel newModel = context.loadModel(id);
//				for (Identifier newId : newModel.getModelDependencies()) {
//					context.loadModel(newId);
//				}
				models.put(id, newModel);
			}
			return new CardUnbakedModel(models);
		}
		return null;
	}

	private Set<Identifier> getCardModels() {
		Set<Identifier> models = new HashSet<>();
		for (Identifier id : CardManager.INSTANCE.getSetIds()) {
			CardSet set = CardManager.INSTANCE.getSet(id);
			for (String name : set.getCards().keySet()) {
				models.add(new ModelIdentifier(new Identifier(id.getNamespace(), "card/" + id.getPath() + '/' + name), "inventory"));
			}
		}
		//just in case!
		models.add(new ModelIdentifier(new Identifier(CardStock.MODID, "card/missingno/missingno"), "inventory"));
		return models;
	}
}
