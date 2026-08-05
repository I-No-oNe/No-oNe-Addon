package net.i_no_am.clickcrystals.addon.module.modules.pvp;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.modules.settings.ItemListSetting;
import io.github.itzispyder.clickcrystals.modules.settings.KeybindSetting;
import io.github.itzispyder.clickcrystals.modules.settings.ListSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;


public class LootKeeper extends AddonListenerModule {

    public LootKeeper() {
        super("loot-keeper", "Throws out your hotbar on a key, keeping only the items worth keeping");
    }

    private boolean isToggle = false;
    private long lastThrowTime = 0;
    private static final long THROW_DELAY_MS = 100;

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<Boolean> keepByKeyword = scGeneral.add(createBoolSetting()
            .name("keep-by-keyword")
            .description("Keep anything whose name contains one of your keywords, instead of naming every item.")
            .def(false)
            .build()
    );

    public final ListSetting keywords = scGeneral.add(createListSetting()
            .name("keywords-to-keep")
            .description("Words an item's name has to contain to be kept, so \"sword\" keeps every sword.")
            .def("sword", "pickaxe", "totem", "crystal", "pearl")
            .visibleWhen(keepByKeyword::getVal)
            .build()
    );

    public final ItemListSetting listItem = scGeneral.add(createItemListSetting()
            .name("items-to-keep")
            .description("Items that are never thrown. Everything else in the hotbar goes.")
            .def(Items.DIAMOND, Items.NETHERITE_INGOT, Items.END_CRYSTAL, Items.TOTEM_OF_UNDYING,
                    Items.ENDER_PEARL, Items.NETHERITE_SWORD, Items.ELYTRA, Items.FIREWORK_ROCKET)
            .visibleWhen(() -> !keepByKeyword.getVal())
            .build()
    );

    @SuppressWarnings("unused")
    public final ModuleSetting<Keybind> activateModule = scGeneral.add(KeybindSetting.create()
            .name("throw-bind")
            .description("Starts and stops the throwing.")
            .def(GLFW.GLFW_KEY_G)
            .onPress(keybind -> {
                isToggle = !isToggle;
                ChatUtils.sendPrefixMessage("LootKeeper module is " + (isToggle ? ChatFormatting.AQUA + "ON" : ChatFormatting.RED + "OFF") + ChatFormatting.RESET);
            })
            .condition((bind, screen) -> screen == null)
            .build()
    );

    /**
     * Keyword mode takes over the item list entirely, matching anything whose name contains
     * a keyword, so one word covers a whole family of items.
     */
    private boolean shouldKeep(ItemStack stack) {
        if (!keepByKeyword.getVal())
            return listItem.matches(stack);
        if (stack == null || stack.isEmpty())
            return false;

        String id = ItemListSetting.idOf(stack.getItem());
        return keywords.getEntries().stream().anyMatch(word -> id.contains(word.trim().toLowerCase().replace(' ', '_')));
    }

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (PlayerUtils.invalid() || !isToggle) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastThrowTime < THROW_DELAY_MS) return;


        if (!shouldKeep(HotbarUtils.getHand())) {
            // Select the slot with unwanted item
            HotbarUtils.search(i -> !shouldKeep(i));

            // Drop the item (true = drop entire stack)
            PlayerUtils.player().drop(true);

            // Swing hand for animation
            PlayerUtils.player().swing(InteractionHand.MAIN_HAND);

            lastThrowTime = currentTime;
        }
    }
}
