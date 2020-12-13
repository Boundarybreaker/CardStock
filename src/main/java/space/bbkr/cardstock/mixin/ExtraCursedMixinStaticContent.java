package space.bbkr.cardstock.mixin;

import com.google.gson.GsonBuilder;
import draylar.staticcontent.StaticContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import space.bbkr.cardstock.data.CardData;
import space.bbkr.cardstock.data.CardDataSerializer;

import java.lang.reflect.Type;

@Mixin(StaticContent.class)
public class ExtraCursedMixinStaticContent {
	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/gson/GsonBuilder;registerTypeAdapter(Ljava/lang/reflect/Type;Ljava/lang/Object;)Lcom/google/gson/GsonBuilder;"))
	private static GsonBuilder injectExtraCursedTypeAdapters(GsonBuilder builder, Type type, Object serializer) {
		return builder.registerTypeAdapter(type, serializer).registerTypeAdapter(CardData.class, new CardDataSerializer());
	}
}
