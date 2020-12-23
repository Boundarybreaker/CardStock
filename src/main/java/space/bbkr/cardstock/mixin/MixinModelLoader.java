package space.bbkr.cardstock.mixin;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sun.nio.ch.IOUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Mixin(ModelLoader.class)
public class MixinModelLoader {
	@Inject(method = "getOrLoadModel", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void getMyFuckingStacktrace(Identifier id, CallbackInfoReturnable<UnbakedModel> info, UnbakedModel model, Identifier newId, Exception e) {
		e.printStackTrace();
	}

	@Inject(method = "bake", at = @At("HEAD"))
	private void getMyFuckingIdentifier(Identifier id, ModelBakeSettings settings, CallbackInfoReturnable<BakedModel> info) {
		if (id.toString().contains("cardstock")) {
			System.out.println("WE FOUND IT FUCKER! IT'S AT " + id.toString());
		}
	}

//	@Inject(method = "loadModelFromJson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;deserialize(Ljava/io/Reader;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//	private void getMyFuckingModel(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info, Reader reader, Resource resource) {
//		if (id.toString().contains("cardstock")) {
//			try {
//				System.out.println("JSON contents: " + IOUtils.toString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
//				resource.getInputStream().reset();
//			} catch (IOException e) {
//				//NOOP bc this is debug
//			}
//		}
//	}
}
