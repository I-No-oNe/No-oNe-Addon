package net.i_no_am.clickcrystals.addon.listener;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import net.i_no_am.clickcrystals.addon.client.Manager;
import net.i_no_am.clickcrystals.addon.listener.events.mc.TitleScreenInitEvent;
import net.i_no_am.clickcrystals.addon.screen.AddonWelcomeScreen;
import net.i_no_am.clickcrystals.addon.utils.FileUtils;

import java.util.concurrent.CompletableFuture;

public class AddonListener extends TickEventListener {

    @EventHandler
    public void onScreenInit(TitleScreenInitEvent e) {
        CompletableFuture.runAsync(() -> {
            mc.execute(() -> {

                Manager.version.notifyUpdate();
                boolean bl = Boolean.TRUE.equals(FileUtils.getValue("isFirstSeen"));
                if (!AddonWelcomeScreen.isClosed & bl) {
                    mc.setScreen(new AddonWelcomeScreen());
                    system.println("Welcome to ClickCrystals Addon by I-No-oNe!");
                    FileUtils.setValue("isFirstSeen", false);
                    //      COMMITED OUT BECAUSE THE ADDON IS NOW PUBLIC!
//                } else if (Manager.banData.getBan().shouldDisplay() && !(mc.currentScreen instanceof AddonBanScreen)) {
//                    mc.setScreen(new AddonBanScreen());
//                    OsUtils.copy(OsUtils.getHWID());
                } else Manager.version.notifyUpdate();
            });
        });
    }
}