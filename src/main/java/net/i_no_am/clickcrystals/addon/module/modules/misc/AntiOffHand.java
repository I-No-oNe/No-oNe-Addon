package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.world.InteractionHand;
import java.util.Arrays;

public class AntiOffHand extends AddonListenerModule {

    public AntiOffHand() {
        super("anti-off-hand", "Prevents off-hand swapping under specific conditions");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<String> offHandList = scGeneral.add(createStringSetting()
            .name("off-hand-item-list")
            .description("List of items that prevent off-hand swap.")
            .def("totem")
            .build()
    );

    public final ModuleSetting<String> mainHandList = scGeneral.add(createStringSetting()
            .name("main-hand-item-list")
            .description("List of items that prevent main-hand swap.")
            .def("crystal,pearl,elytra,armor,sword")
            .build()
    );

    public final ModuleSetting<Boolean> disableMainHandSwap = scGeneral.add(createBoolSetting()
            .name("disable-main-hand-swap")
            .description("If enabled, swapping items from the main hand to the off-hand will be blocked if holding restricted items.")
            .def(false)
            .build()
    );

    @EventHandler
    private void onKeyPress(KeyPressEvent e) {
        if (!(e.getKeycode() == mc.options.keySwapOffhand.getDefaultKey().getValue())) return;

        if (isHoldingRestrictedItem(InteractionHand.OFF_HAND, offHandList.getVal()))
            e.cancel();

        else if (disableMainHandSwap.getVal() && isHoldingRestrictedItem(InteractionHand.MAIN_HAND, mainHandList.getVal()))
            e.cancel();
    }

    private boolean isHoldingRestrictedItem(InteractionHand hand, String itemList) {
        return Arrays.asList(itemList.split(",")).stream().anyMatch(item -> HotbarUtils.nameContains(item.trim(), hand));
    }
}