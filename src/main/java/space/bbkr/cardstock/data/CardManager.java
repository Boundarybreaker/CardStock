package space.bbkr.cardstock.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import space.bbkr.cardstock.CardStock;

import java.util.*;

public class CardManager extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final CardManager INSTANCE = new CardManager();

	private final Map<Identifier, CardSet> sets = new HashMap<>();

	private final Card defaultMissingno;
	private final CardSet defaultMissingnoSet;

	public CardManager() {
		super(new GsonBuilder().setPrettyPrinting().create(), "cardstock/sets");
		this.defaultMissingno = new Card(1, new TranslatableText("text.cardstock.missingno"), new ArrayList<>(), "kat", "2020");
		Map<String, Card> setMap = new HashMap<>();
		setMap.put("missingno", defaultMissingno);
		this.defaultMissingnoSet = new CardSet(new Identifier(CardStock.MODID, "textures/gui/missingno.png"), setMap);
		sets.put(new Identifier(CardStock.MODID, "missingno"), defaultMissingnoSet);
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
		sets.clear();
		sets.put(new Identifier(CardStock.MODID, "missingno"), defaultMissingnoSet);
		int cardCount = 1;
		for (Identifier id : loader.keySet()) {
			JsonElement elem = loader.get(id);
			JsonObject json = JsonHelper.asObject(elem, "contents of " + id.toString());
			Identifier emblem = new Identifier(JsonHelper.getString(json, "emblem"));
			Map<String, Card> parsedCards = new HashMap<>();
			JsonObject cards = JsonHelper.getObject(json, "cards");

			for (Map.Entry<String, JsonElement> entry : cards.entrySet()) {
				JsonObject card = JsonHelper.asObject(entry.getValue(), entry.getKey());
				int rarity = JsonHelper.getInt(card, "rarity", 1);
				Text info = JsonHelper.hasElement(card, "info") ? Text.Serializer.fromJson(card.get("info")) : new LiteralText("");
				List<Text> lore = new ArrayList<>();
				for (JsonElement line : Objects.requireNonNull(JsonHelper.getArray(card, "lore", new JsonArray()))) {
					lore.add(Text.Serializer.fromJson(line));
				}
				String author = JsonHelper.getString(card, "artist", "");
				String date = JsonHelper.getString(card, "date", "");
				parsedCards.put(entry.getKey(), new Card(rarity, info, lore, author, date));
				cardCount++;
			}

			sets.put(id, new CardSet(emblem, parsedCards));
		}

		//TODO: networking :unamused:
		//TODO: proper singulars here
		CardStock.LOGGER.info("Loaded " + sets.size() + " card sets, including " + cardCount + " cards");
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(CardStock.MODID, "card_manager");
	}

	public Collection<Identifier> getSetIds() {
		return sets.keySet();
	}

	public CardSet getSet(Identifier id) {
		return sets.getOrDefault(id, defaultMissingnoSet);
	}

	public Card getCard(Identifier id) {
		Identifier setId = new Identifier(id.getNamespace(), id.getPath().substring(0, id.getPath().lastIndexOf('/')));
		String cardId = id.getPath().substring(id.getPath().lastIndexOf('/') + 1);
		return getSet(setId).getCard(cardId);
	}

	public CardSet getSet(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("Card", NbtType.STRING)) {
			return getSet(new Identifier(stack.getTag().getString("Card")));
		}
		return getSet(new Identifier(CardStock.MODID, "missingno"));
	}

	public Card getCard(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("Card", NbtType.STRING)) {
			return getCard(new Identifier(stack.getTag().getString("Card")));
		}
		return getCard(new Identifier(CardStock.MODID, "missingno/missingno"));
	}

	public CardSet getDefaultMissingnoSet() {
		return defaultMissingnoSet;
	}

	public Card getDefaultMissingno() {
		return defaultMissingno;
	}

	public void appendCards(List<ItemStack> toDisplay) {
		for (Identifier id : sets.keySet()) {
			CardSet set = sets.get(id);
			for (String name : set.getCards().keySet()) {
				Identifier cardId = new Identifier(id.getNamespace(), id.getPath() + '/' + name);
				ItemStack cardStack = new ItemStack(CardStock.CARD);
				cardStack.getOrCreateTag().putString("Card", cardId.toString());
				toDisplay.add(cardStack);
			}
		}
	}

	public PacketByteBuf getBuf() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVarInt(sets.size()); //amount of card sets
		for (Identifier id : sets.keySet()) {
			buf.writeIdentifier(id); //ID of the card set
			CardSet set = sets.get(id);
			buf.writeIdentifier(set.getEmblem()); //emblem of the card set
			Map<String, Card> cards = set.getCards();
			buf.writeVarInt(cards.size()); //amount of cards in the set
			for (String key : cards.keySet()) {
				buf.writeString(key); //name of the card
				Card card = cards.get(key);
				buf.writeVarInt(card.getRarity()); //rarity of the card
				buf.writeString(card.getArtist()); //artist of the card
				buf.writeString(card.getDate()); //date of the card
				buf.writeText(card.getInfo()); //card info
				buf.writeVarInt(card.getLore().size()); //number of lore lines
				for (Text line : card.getLore()) {
					buf.writeText(line); //line of lore
				}
			}
		}
		System.out.println("Card packet serialized!");
		return buf;
	}

	public void sendPacket(ServerPlayerEntity player) {
		PacketByteBuf buf = getBuf();
		ServerPlayNetworking.send(player, CardStock.CARD_SYNC, buf);
		System.out.println("Card packet sent from reload!");
	}

	public void recievePacket(PacketByteBuf buf) {
		System.out.println("Card packet recieved!");
		sets.clear();
		int setCount = buf.readVarInt();
		for (int i = 0; i < setCount; i++) {
			Identifier id = buf.readIdentifier();
			Identifier emblem = buf.readIdentifier();
			Map<String, Card> cards = new HashMap<>();
			int cardCount = buf.readVarInt();
			for (int j = 0; j < cardCount; j++) {
				String name = buf.readString();
				int rarity = buf.readVarInt();
				String artist = buf.readString();
				String date = buf.readString();
				Text info = buf.readText();
				List<Text> lore = new ArrayList<>();
				int loreCount = buf.readVarInt();
				for (int k = 0; k < loreCount; k++) {
					lore.add(buf.readText());
				}
				cards.put(name, new Card(rarity, info, lore, artist, date));
			}
			sets.put(id, new CardSet(emblem, cards));
		}
		System.out.println("Deserialized " + setCount + " cards");
	}
}
