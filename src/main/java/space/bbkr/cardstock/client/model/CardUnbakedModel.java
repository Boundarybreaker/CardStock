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
	private final Map<Identifier, UnbakedModel> models;

	public CardUnbakedModel(Map<Identifier, UnbakedModel> models) {
		this.models = models;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		Set<Identifier> deps = new HashSet<>(models.keySet());
		for (Identifier id : models.keySet()) {
			UnbakedModel model = models.get(id);
			deps.addAll(model.getModelDependencies());
		}
		return deps;
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		Set<SpriteIdentifier> sprites = new HashSet<>();
		for (Identifier id : getModelDependencies()) {
			sprites.addAll(unbakedModelGetter.apply(id).getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
		}
		return sprites;
	}

	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		Map<Identifier, BakedModel> baked = new HashMap<>();
		for (Identifier id : models.keySet()) {
			baked.put(id, loader.bake(id, rotationContainer));
		}
		return new CardBakedModel(baked);
	}
}
