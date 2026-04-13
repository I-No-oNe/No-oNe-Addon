package net.i_no_am.clickcrystals.addon.module.modules.pvp;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LootKeeper extends AddonListenerModule {

    public LootKeeper() {
        super("loot-keeper", "Prevents specific items from being dropped or lost");
    }

    private boolean isToggle = false;
    private long lastThrowTime = 0;
    private static final long THROW_DELAY_MS = 100;

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<String> listItem = scGeneral.add(createStringSetting()
            .name("list-of-items-to-not-throw")
            .description("Which items the module won't throw, separated by commas:")
            .def("diamond,netherite,crystals,totem,pearl,sword,elytra,rockets")
            .build()
    );

    @SuppressWarnings("unused")
    public final ModuleSetting<Keybind> activateModule = scGeneral.add(KeybindSetting.create()
            .name("activate-module-bind")
            .description("Key to start throwing items not in the list.")
            .def(GLFW.GLFW_KEY_G)
            .onPress(keybind -> {
                isToggle = !isToggle;
                ChatUtils.sendPrefixMessage("LootKeeper module is " + (isToggle ? ChatFormatting.AQUA + "ON" : ChatFormatting.RED + "OFF") + ChatFormatting.RESET);
            })
            .condition((bind, screen) -> screen == null)
            .build()
    );

    private List<String> cachedKeepList = null;
    private String lastRawKeepList = "";

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid() || !isToggle) return;

        String rawKeepList = listItem.getVal();

        if (!rawKeepList.equals(lastRawKeepList)) {
            cachedKeepList = Arrays.stream(rawKeepList.split(","))
                    .map(String::trim)
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .toList();
            lastRawKeepList = rawKeepList;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastThrowTime < THROW_DELAY_MS) return;


        boolean shouldKeep = cachedKeepList.stream().anyMatch(HotbarUtils::nameContains);

        if (!shouldKeep) {
            // Select the slot with unwanted item
            HotbarUtils.search(i -> cachedKeepList.contains(i.getItem().getDescriptionId()));

            // Drop the item (true = drop entire stack)
            PlayerUtils.player().drop(true);

            // Swing hand for animation
            PlayerUtils.player().swing(InteractionHand.MAIN_HAND);

            lastThrowTime = currentTime;
        }
    }
}
