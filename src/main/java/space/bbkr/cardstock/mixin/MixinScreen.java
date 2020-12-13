package space.bbkr.cardstock.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Screen.class)
public class MixinScreen {
	//the data.ifPresent lambda inside of Screen#renderTooltip
	@Inject(method = "method_32635", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onComponentConstruct(List<TooltipComponent> list, TooltipData data, CallbackInfo info) {
		if (data instanceof TooltipComponent) {
			list.add((TooltipComponent) data);
			info.cancel();
		}
	}
}
