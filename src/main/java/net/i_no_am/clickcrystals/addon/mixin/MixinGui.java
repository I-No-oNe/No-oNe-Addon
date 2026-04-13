package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.Global;
import net.i_no_am.clickcrystals.addon.listener.events.mc.EffectsOverlayInitEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui implements Global {
    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusEffectOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    system.eventBus.passWithCallbackInfo(ci,new EffectsOverlayInitEvent());
    }
}