package space.bbkr.cardstock.client.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import space.bbkr.cardstock.CardStock;
import space.bbkr.cardstock.data.CardManager;
import space.bbkr.cardstock.data.CardSet;

import java.util.*;
import java.util.function.Function;

public class CardUnbakedModel implements UnbakedModel {
	@Override
	public Collection<Identifier> getModelDependencies() {
		Set<Identifier> models = new HashSet<>();
		for (Identifier id : CardManager.INSTANCE.getSetIds()) {
			CardSet set = CardManager.INSTANCE.getSet(id);
			for (String name : set.getCards().keySet()) {
				models.add(new ModelIdentifier(new Identifier(id.getNamespace(), "card/" + id.getPath() + '/' + name), "inventory"));
			}
		}
		models.add(new ModelIdentifier(new Identifier(CardStock.MODID, "card/missingno/missingno"), "inventory"));
		return models;
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
//		Set<SpriteIdentifier> sprites = new HashSet<>();
//		for (Identifier id : getModelDependencies()) {
//			sprites.addAll(unbakedModelGetter.apply(id).getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
//		}
//		return sprites;
		return Collections.emptySet();
	}

	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		Map<Identifier, BakedModel> models = new HashMap<>();
		for (Identifier id : getModelDependencies()) {
			System.out.println("Instructing to bake model " + id.toString());
			BakedModel model = loader.getOrLoadModel(id).bake(loader, textureGetter, rotationContainer, id);
//			BakedModel model = loader.bake(id, rotationContainer);
			models.put(id, model);
			System.out.println(models.get(id).getSprite());
			System.out.println(models.get(id).getQuads(null, null, new Random()));
		}
		return new CardBakedModel(models);
	}
}
