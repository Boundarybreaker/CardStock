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
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;
import space.bbkr.cardstock.CardStock;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class CardBakedModel implements BakedModel, FabricBakedModel {
	private static final BakedModelManager MANAGER = MinecraftClient.getInstance().getBakedModelManager();

	private final Map<Identifier, BakedModel> models;
	private final BakedModel wrapped;

	public CardBakedModel(Map<Identifier, BakedModel> models) {
		this.models = models;
		this.wrapped = models.getOrDefault(new ModelIdentifier(new Identifier(CardStock.MODID, "card/missingno/missingno"), "inventory"), MANAGER.getMissingModel());
	}

	private FabricBakedModel get() {
//		System.out.println("ID: " + new ModelIdentifier(new Identifier(CardStock.MODID, "card/missingno/missingno"), "inventory").toString());
//		System.out.println("found card: " + (wrapped != MANAGER.getMissingModel()));
		return (FabricBakedModel) wrapped;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		if (stack.hasTag() && stack.getTag().contains("Card", NbtType.STRING)) {
			Identifier cardId = new Identifier(stack.getTag().getString("Card"));
			ModelIdentifier modelId = new ModelIdentifier(new Identifier(cardId.getNamespace(), "item/card/" + cardId.getPath()), "inventory");
			((FabricBakedModel) models.getOrDefault(modelId, MANAGER.getMissingModel())).emitItemQuads(stack, randomSupplier, context);
		} else {
			get().emitItemQuads(stack, randomSupplier, context);
		}
	}

	@Override
	public boolean isVanillaAdapter() {
		return get().isVanillaAdapter();
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		get().emitBlockQuads(blockView, state, pos, randomSupplier, context);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return wrapped.getQuads(state, face, random);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return wrapped.useAmbientOcclusion();
	}

	@Override
	public boolean hasDepth() {
		return wrapped.hasDepth();
	}

	@Override
	public boolean isSideLit() {
		return wrapped.isSideLit();
	}

	@Override
	public boolean isBuiltin() {
		return wrapped.isBuiltin();
	}

	@Override
	public Sprite getSprite() {
		return wrapped.getSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return wrapped.getTransformation();
	}

	@Override
	public ModelOverrideList getOverrides() {
		return wrapped.getOverrides();
	}
}
