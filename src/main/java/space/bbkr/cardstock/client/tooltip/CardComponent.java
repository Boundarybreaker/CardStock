package space.bbkr.cardstock.client.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import space.bbkr.cardstock.CardStock;
import space.bbkr.cardstock.item.CardItem;

public class CardComponent implements TooltipComponent, TooltipData {
	private static final Identifier STAR_ID = new Identifier(CardStock.MODID, "textures/gui/star.png");
	private static final Identifier EMPTY_STAR_ID = new Identifier(CardStock.MODID, "textures/gui/empty_star.png");
	private final CardItem card;

	public CardComponent(CardItem card) {
		this.card = card;
	}

	@Override
	public int getHeight() {
		return 11;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return 80;
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
		//TODO: implement
		if (card.getData().getRarity() != 0) {
			textureManager.bindTexture(STAR_ID);
			for (int i = 0; i < card.getData().getRarity(); i++) {
				DrawableHelper.drawTexture(matrices, x + i * 10, y, z, 0, 0, 9, 9, 9, 9);
			}
			textureManager.bindTexture(EMPTY_STAR_ID);
			for (int i = card.getData().getRarity(); i < 5; i++) {
				DrawableHelper.drawTexture(matrices, x + i * 10, y, z, 0, 0, 9, 9, 9, 9);
			}
			textureManager.bindTexture(card.getSet().getEmblem());
			DrawableHelper.drawTexture(matrices, x + 70, y, z, 0, 0, 9, 9, 9, 9);
		}
	}
}
