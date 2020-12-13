package space.bbkr.cardstock.data;

import com.google.gson.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardDataSerializer implements JsonSerializer<CardData>, JsonDeserializer<CardData> {
	@Override
	public CardData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = JsonHelper.asObject(json, "card entry");
		int rarity = JsonHelper.getInt(obj, "rarity", 1);
		Text info = JsonHelper.hasElement(obj, "info") ? Text.Serializer.fromJson(obj.get("info")) : new LiteralText("");
		List<Text> lore = new ArrayList<>();
		for (JsonElement line : JsonHelper.getArray(obj, "lore", new JsonArray())) {
			lore.add(Text.Serializer.fromJson(line));
		}
		String author = JsonHelper.getString(obj, "artist", "");
		String date = JsonHelper.getString(obj, "date", "");
		return new CardData(rarity, info, lore, author, date);
	}

	@Override
	public JsonElement serialize(CardData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		json.addProperty("rarity", src.getRarity());
		json.add("info", Text.Serializer.toJsonTree(src.getInfo()));
		JsonArray lore = new JsonArray();
		for (Text line : src.getLore()) {
			lore.add(Text.Serializer.toJsonTree(line));
		}
		json.add("lore", lore);
		json.addProperty("author", src.getArtist());
		json.addProperty("date", src.getDate());
		return json;
	}
}
