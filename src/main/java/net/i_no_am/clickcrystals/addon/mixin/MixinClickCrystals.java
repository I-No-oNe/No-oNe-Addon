package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.ClickCrystals;
import net.i_no_am.clickcrystals.addon.AddonManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClickCrystals.class)
public class MixinClickCrystals {

    /**
     * WE DISABLE THE CC CONFIG BECAUSE IT WON'T SAVE DATA FROM THE MODULES WE ADDING, WE NEED TO CANCEL THE ORIGINAL METHODS AND CREATE NEW ONES
     * @see AddonManager
     **/

    @Inject(remap = false, method = "onInitialize", at = @At(value = "INVOKE", target = "Lio/github/itzispyder/clickcrystals/client/system/Config;loadEntireConfig()V"), cancellable = true)
    private void disableLoadingConfig(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(remap = false, method = "onInitialize", at = @At(value = "INVOKE", target = "Lio/github/itzispyder/clickcrystals/client/system/ProfileManager$ProfileConfig;getCurrentProfileName()Ljava/lang/String;"), cancellable = true)
    private void disablePrintingProfiles(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(remap = false, method = "onInitialize", at = @At(value = "INVOKE", target = "Lio/github/itzispyder/clickcrystals/client/system/ProfileManager;init()V"), cancellable = true)
    private void disableLoadingProfile(CallbackInfo ci) {
        ci.cancel();
    }
}