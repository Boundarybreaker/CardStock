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
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import space.bbkr.cardstock.client.tooltip.CardComponent;
import space.bbkr.cardstock.data.Card;
import space.bbkr.cardstock.data.CardManager;
import space.bbkr.cardstock.data.CardSet;

import java.util.List;
import java.util.Optional;

public class CardItem extends Item {

	public CardItem(Settings settings) {
		super(settings);
	}

	public Card getCard(ItemStack stack) {
		return CardManager.INSTANCE.getCard(new Identifier(stack.getOrCreateTag().getString("Card")));
	}

	public CardSet getSet(ItemStack stack) {
		String cardId = stack.getOrCreateTag().getString("Card");
		return CardManager.INSTANCE.getSet(new Identifier(cardId.substring(0, cardId.lastIndexOf('/'))));
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return getCard(stack).getItemRarity();
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return getCard(stack).getRarity() == 5;
	}

	@Override
	public Text getName(ItemStack stack) {
		return new TranslatableText("card." + stack.getOrCreateTag().getString("Card")
				.replace(':', '.')
				.replace('/', '.')
		);
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		return Optional.of(new CardComponent(this, stack));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new LiteralText(""));
		Card card = getCard(stack);
		if (!card.toString().equals("")) tooltip.add(card.getInfo());
		if (Screen.hasShiftDown()) {
			for (Text line : card.getLore()) {
				tooltip.add(new LiteralText("  ").append(line));
			}
			if (card.getArtist() != null) {
				tooltip.add(new TranslatableText("text.cardstock.artist", card.getArtist(), card.getDate()).formatted(Formatting.GRAY));
			}
		} else {
			tooltip.add(new TranslatableText("text.cardstock.more").formatted(Formatting.GRAY));
		}
		if (context.isAdvanced()) {
			tooltip.add(new TranslatableText("text.cardstock.source").formatted(Formatting.BLUE, Formatting.ITALIC));
		}
	}
}
