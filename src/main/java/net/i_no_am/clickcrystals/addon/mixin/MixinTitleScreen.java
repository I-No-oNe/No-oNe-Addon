package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.Global;
import net.i_no_am.clickcrystals.addon.listener.events.mc.TitleScreenInitEvent;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen implements Global {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I", shift = At.Shift.BY))
    private void initGame(CallbackInfo ci) {
        system.eventBus.pass(new TitleScreenInitEvent());
    }
}