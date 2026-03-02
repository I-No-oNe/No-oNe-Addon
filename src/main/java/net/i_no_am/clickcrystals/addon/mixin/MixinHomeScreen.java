package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.gui.screens.HomeScreen;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.GUIFeaturesDisabler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HomeScreen.class, remap = false)
public class MixinHomeScreen implements Global {

    @Shadow public static boolean OPENED_BEFORE;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void disableDiscordScreen(CallbackInfo ci) {
        var featuresDisabler = Module.get(GUIFeaturesDisabler.class);
        if (featuresDisabler.isEnabled() && featuresDisabler.discordDisable.getVal()) OPENED_BEFORE = true;
    }
}