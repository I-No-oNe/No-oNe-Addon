package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.terraformersmc.modmenu.ModMenu;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.ModMenuDisabler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModMenu.class)
public class MixinModMenu {

    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    private void onInitializeClient(CallbackInfo ci) {
        ModMenuDisabler mod = Module.get(ModMenuDisabler.class);
        if (mod != null && mod.isEnabled()) mod.hideMods();
    }

    @ModifyReturnValue(method = "getDisplayedModCount", at = @At("RETURN"))
    private static String modifyModMenuVersion(String original) {
        return Module.isEnabled(ModMenuDisabler.class) ? Module.get(ModMenuDisabler.class).numberOfHiddenMods() : original;
    }
}
