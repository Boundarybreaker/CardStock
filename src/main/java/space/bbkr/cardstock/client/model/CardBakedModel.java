package space.bbkr.cardstock.client.model;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import space.bbkr.cardstock.CardStock;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class CardBakedModel extends ForwardingBakedModel {
	private final BakedModelManager MANAGER = MinecraftClient.getInstance().getBakedModelManager();

	public CardBakedModel() {
		this.wrapped = MANAGER.getModel(new ModelIdentifier(new Identifier(CardStock.MODID, "card_missingno"), "inventory"));
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		if (stack.hasTag() && stack.getTag().contains("Card", NbtType.STRING)) {
			Identifier cardId = new Identifier(stack.getTag().getString("Card"));
			ModelIdentifier modelId = new ModelIdentifier(new Identifier(cardId.getNamespace(), "card/" + cardId.getPath()), "inventory");
			((FabricBakedModel) MANAGER.getModel(modelId)).emitItemQuads(stack, randomSupplier, context);
		} else {
			super.emitItemQuads(stack, randomSupplier, context);
		}
	}
}
