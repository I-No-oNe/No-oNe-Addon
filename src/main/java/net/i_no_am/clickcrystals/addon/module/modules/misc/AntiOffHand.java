package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.i_no_am.clickcrystals.addon.setting.ItemListSetting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class AntiOffHand extends AddonListenerModule {

    public AntiOffHand() {
        super("anti-off-hand", "Blocks the off-hand swap key while you are holding items you cannot afford to move");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ItemListSetting offHandList = scGeneral.add(ItemListSetting.builder()
            .name("off-hand-items")
            .description("Holding one of these in the off-hand blocks the swap.")
            .def(Items.TOTEM_OF_UNDYING)
            .build()
    );

    public final ModuleSetting<Boolean> disableMainHandSwap = scGeneral.add(createBoolSetting()
            .name("disable-main-hand-swap")
            .description("Also block the swap based on what the main hand is holding.")
            .def(false)
            .build()
    );

    public final ItemListSetting mainHandList = scGeneral.add(ItemListSetting.builder()
            .name("main-hand-items")
            .description("Holding one of these in the main hand blocks the swap.")
            .def(Items.END_CRYSTAL, Items.ENDER_PEARL, Items.ELYTRA, Items.NETHERITE_SWORD, Items.DIAMOND_SWORD)
            .visibleWhen(disableMainHandSwap::getVal)
            .build()
    );

    @EventHandler
    private void onKeyPress(KeyPressEvent e) {
        if (!(e.getKeycode() == mc.options.keySwapOffhand.getDefaultKey().getValue())) return;

        if (offHandList.matches(HotbarUtils.getHand(InteractionHand.OFF_HAND)))
            e.cancel();

        else if (disableMainHandSwap.getVal() && mainHandList.matches(HotbarUtils.getHand(InteractionHand.MAIN_HAND)))
            e.cancel();
    }
}