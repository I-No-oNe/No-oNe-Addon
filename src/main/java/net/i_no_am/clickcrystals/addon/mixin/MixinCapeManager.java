package net.i_no_am.clickcrystals.addon.mixin;

import com.mojang.authlib.GameProfile;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.system.CapeManager;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.CapeDisabler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapeManager.class)
public class MixinCapeManager implements Global {

    @Inject(remap = false, method = "reloadTextures", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;runAsync(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"), cancellable = true)
    private void onReload(CallbackInfo ci) {
        CapeDisabler c = Module.get(CapeDisabler.class);
        if (c == null) return;
        if (c.isEnabled() && c.disableClickCrystalCapes.getVal())
            ci.cancel();
    }

    @Inject(remap = false, method = "getCapeTexture", at = @At("RETURN"), cancellable = true)
    private void onGetCapeTexture(GameProfile profile, CallbackInfoReturnable<Identifier> cir) {
        CapeDisabler c = Module.get(CapeDisabler.class);
        if (c == null) return;
        if (c.isEnabled() && c.disableClickCrystalCapes.getVal()) {
            cir.cancel();
            cir.setReturnValue(null);
        }
    }
}