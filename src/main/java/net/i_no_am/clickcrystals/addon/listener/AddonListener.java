package net.i_no_am.clickcrystals.addon.listener;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import net.i_no_am.clickcrystals.addon.client.Manager;
import net.i_no_am.clickcrystals.addon.listener.events.mc.TitleScreenInitEvent;
import net.i_no_am.clickcrystals.addon.screen.AddonScreen;
import net.i_no_am.clickcrystals.addon.utils.OsUtils;

import java.util.concurrent.CompletableFuture;

public class AddonListener extends TickEventListener {

    @EventHandler
    public void onScreenInit(TitleScreenInitEvent e) {
        CompletableFuture.runAsync(() -> {
            mc.execute(() -> {
                if (Manager.banData.getBan().shouldDisplay() && !(mc.currentScreen instanceof AddonScreen)) {
                    mc.setScreen(new AddonScreen());
                    OsUtils.copy(OsUtils.getHWID());
                } else Manager.version.notifyUpdate();
            });
        });
    }
}