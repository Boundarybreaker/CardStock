package space.bbkr.cardstock.data;

import draylar.staticcontent.api.ContentData;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import space.bbkr.cardstock.CardStock;
import space.bbkr.cardstock.item.CardItem;

import java.util.Map;

public class CardSetData implements ContentData {
	private final Identifier emblem;
	private final Map<String, CardData> cards;

	public CardSetData(Identifier emblem, Map<String, CardData> cards) {
		this.emblem = emblem;
		this.cards = cards;
	}

	public Identifier getEmblem() {
		return emblem;
	}

	public Map<String, CardData> getCards() {
		return cards;
	}

	@Override
	public void register(Identifier id) {
		String path = id.getPath().substring(id.getPath().lastIndexOf('/') + 1, id.getPath().lastIndexOf('.'));
		for (String card : cards.keySet()) {
			Registry.register(
					Registry.ITEM,
					new Identifier(id.getNamespace(), "card/" + path + "/" + card),
					new CardItem(cards.get(card), this, new Item.Settings().group(CardStock.GROUP))
			);
		}
		CardStock.addSet(new Identifier(id.getNamespace(), path), this);
	}
}
