package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.events.networking.GameJoinEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.listeners.NetworkEventListener;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.screen.AddonScreen;
import net.i_no_am.clickcrystals.addon.modules.CapeDisabler;
import net.i_no_am.clickcrystals.addon.utils.NetworkUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkEventListener.class)
public class MixinNetworkEventListener implements Global {
    @Inject(remap = false, method = "handPlayerJoin", at = @At(value = "INVOKE", target = "Lio/github/itzispyder/clickcrystals/client/system/ClickCrystalsInfo;getStaff(Ljava/util/UUID;)Lio/github/itzispyder/clickcrystals/client/system/ClickCrystalsInfo$User;"), cancellable = true)
    private void cancelStaffMessage(PacketReceiveEvent e, CallbackInfo ci) {
        CapeDisabler cape = Module.get(CapeDisabler.class);
        if (cape.isEnabled() && cape.disableStaffNotifications.getVal()) {
            ci.cancel();
        }
    }

    @Inject(remap = false, method = "handPlayerJoin", at = @At(value = "INVOKE", target = "Lio/github/itzispyder/clickcrystals/client/system/ClickCrystalsInfo;getOwner(Ljava/util/UUID;)Lio/github/itzispyder/clickcrystals/client/system/ClickCrystalsInfo$User;"), cancellable = true)
    private void cancelOwnerMessage(PacketReceiveEvent e, CallbackInfo ci) {
        CapeDisabler cape = Module.get(CapeDisabler.class);
        if (cape.isEnabled() && cape.disableStaffNotifications.getVal()) {
            ci.cancel();
        }
    }
}
