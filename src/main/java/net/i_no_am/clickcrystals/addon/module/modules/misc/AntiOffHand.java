package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ItemListSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class AntiOffHand extends AddonListenerModule {

    public AntiOffHand() {
        super("anti-off-hand", "Prevents off-hand swapping under specific conditions");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ItemListSetting offHandList = scGeneral.add(createItemListSetting()
            .name("off-hand-item-list")
            .description("List of items that prevent off-hand swap.")
            .def(Items.TOTEM_OF_UNDYING)
            .build()
    );

    public final ModuleSetting<Boolean> disableMainHandSwap = scGeneral.add(createBoolSetting()
            .name("disable-main-hand-swap")
            .description("If enabled, swapping items from the main hand to the off-hand will be blocked if holding restricted items.")
            .def(false)
            .build()
    );

    public final ItemListSetting mainHandList = scGeneral.add(createItemListSetting()
            .name("main-hand-item-list")
            .description("List of items that prevent main-hand swap.")
            .def(Items.END_CRYSTAL, Items.ENDER_PEARL, Items.ELYTRA, Items.NETHERITE_SWORD, Items.DIAMOND_SWORD)
            .visibleWhen(disableMainHandSwap::getVal)
            .build()
    );

    @EventHandler
    private void onKeyPress(KeyPressEvent e) {
        if (!(e.getKeycode() == mc.options.keySwapOffhand.getDefaultKey().getValue())) return;

        if (isHoldingRestrictedItem(InteractionHand.OFF_HAND, offHandList))
            e.cancel();

        else if (disableMainHandSwap.getVal() && isHoldingRestrictedItem(InteractionHand.MAIN_HAND, mainHandList))
            e.cancel();
    }

    private boolean isHoldingRestrictedItem(InteractionHand hand, ItemListSetting itemList) {
        return itemList.matches(HotbarUtils.getHand(hand));
    }
}