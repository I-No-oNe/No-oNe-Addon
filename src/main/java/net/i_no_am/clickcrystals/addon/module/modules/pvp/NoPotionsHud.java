package net.i_no_am.clickcrystals.addon.module.modules.pvp;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import net.i_no_am.clickcrystals.addon.listener.events.mc.EffectsOverlayInitEvent;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;

public class NoPotionsHud extends AddonListenerModule {
    public NoPotionsHud() {
        super("no-potion-hud", "Hides the potion effect icons in the corner of the screen");
    }

    @EventHandler
    private void effectGui(EffectsOverlayInitEvent e) {
        if (isEnabled()) e.cancel();
    }
}
