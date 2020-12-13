package space.bbkr.cardstock.data;

import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.List;

public class CardData {
	private final int rarity;
	private final Text info;
	private final List<Text> lore;
	private final String author;
	private final String date;

	public CardData(int rarity, Text info, List<Text> lore, String author, String date) {
		this.rarity = rarity;
		this.info = info;
		this.lore = lore;
		this.author = author;
		this.date = date;
	}

	public Rarity getItemRarity() {
		return rarity == 0? Rarity.COMMON : Rarity.values()[Math.min(rarity - 1, 3)];
	}

	public int getRarity() {
		return rarity;
	}

	public Text getInfo() {
		return info;
	}

	public List<Text> getLore() {
		return lore;
	}

	public String getArtist() {
		return author;
	}

	public String getDate() {
		return date;
	}
}
