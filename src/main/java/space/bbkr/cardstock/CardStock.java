package space.bbkr.cardstock;

import draylar.staticcontent.StaticContent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.bbkr.cardstock.data.CardSetData;

import java.util.HashMap;
import java.util.Map;

public class CardStock implements ModInitializer {
	public static final String MODID = "cardstock";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	private static final Map<Identifier, CardSetData> SETS = new HashMap<>();

	//TODO: proper group icon
	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "cards"), () -> new ItemStack(Items.BOOK));

	@Override
	public void onInitialize() {
		StaticContent.load(new Identifier(MODID, "sets"), CardSetData.class);
	}

	public static void addSet(Identifier id, CardSetData data) {
		SETS.put(id, data);
	}

	public static CardSetData getSet(Identifier id) {
		return SETS.get(id);
	}
}
