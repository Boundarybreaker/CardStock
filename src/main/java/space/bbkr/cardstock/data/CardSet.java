package space.bbkr.cardstock.data;

import net.minecraft.util.Identifier;

import java.util.Map;

public class CardSet {
	private final Identifier emblem;
	private final Map<String, Card> cards;

	public CardSet(Identifier emblem, Map<String, Card> cards) {
		this.emblem = emblem;
		this.cards = cards;
	}

	public Identifier getEmblem() {
		return emblem;
	}

	public Map<String, Card> getCards() {
		return cards;
	}

	public Card getCard(String name) {
		return cards.get(name);
	}
}
