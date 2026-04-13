package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.InfiniteChat;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Shadow protected EditBox input;

    @Inject(method = "init()V",at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (Module.get(InfiniteChat.class).isEnabled())
            input.setMaxLength(Integer.MAX_VALUE);
    }
}