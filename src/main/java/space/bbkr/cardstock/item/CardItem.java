package space.bbkr.cardstock.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import space.bbkr.cardstock.client.tooltip.CardComponent;
import space.bbkr.cardstock.data.CardData;
import space.bbkr.cardstock.data.CardSetData;

import java.util.List;
import java.util.Optional;

public class CardItem extends Item {
	private final CardData data;
	private final CardSetData set;

	public CardItem(CardData data, CardSetData set, Settings settings) {
		super(settings.rarity(data.getItemRarity()));
		this.data = data;
		this.set = set;
	}

	public CardData getData() {
		return data;
	}

	public CardSetData getSet() {
		return set;
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return data.getRarity() == 5;
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		return Optional.of(new CardComponent(this));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new LiteralText(""));
		if (!data.toString().equals("")) tooltip.add(data.getInfo());
		if (Screen.hasShiftDown()) {
			for (Text line : data.getLore()) {
				tooltip.add(new LiteralText("  ").append(line));
			}
			if (data.getArtist() != null) {
				tooltip.add(new TranslatableText("text.cardstock.source", data.getArtist(), data.getDate()).formatted(Formatting.GRAY));
			}
		} else {
			tooltip.add(new TranslatableText("text.cardstock.more").formatted(Formatting.GRAY));
		}
	}
}
