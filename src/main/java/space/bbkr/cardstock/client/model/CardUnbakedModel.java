package space.bbkr.cardstock.client.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
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
		List<Identifier> models = new ArrayList<>();
		for (Identifier id : CardManager.INSTANCE.getSetIds()) {
			CardSet set = CardManager.INSTANCE.getSet(id);
			for (String name : set.getCards().keySet()) {
				models.add(new ModelIdentifier(new Identifier(id.getNamespace(), "card/" + id.getPath() + '/' + name), "inventory"));
			}
		}
		models.add(new ModelIdentifier(new Identifier(CardStock.MODID, "card_missingno"), "inventory"));
		return models;
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptySet();
//		return Collections.singleton(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(CardStock.MODID, "item/card_missingno")));
	}

	@Nullable
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		return new CardBakedModel();
	}
}
