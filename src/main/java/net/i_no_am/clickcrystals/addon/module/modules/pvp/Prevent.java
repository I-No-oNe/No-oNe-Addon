package net.i_no_am.clickcrystals.addon.module.modules.pvp;

import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import net.i_no_am.clickcrystals.addon.module.AddonModule;
import net.i_no_am.clickcrystals.addon.utils.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class Prevent extends AddonModule {
    public Prevent() {
        super("prevent", "Prevent making common mistakes in crystal PvP");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<Boolean> disableGlowstonePlacement = scGeneral.add(createBoolSetting()
            .name("disable-placing-glowstone")
            .description("Disable placing glowstone unless targeting a respawn anchor.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> disableAnchorOnGlowstone = scGeneral.add(createBoolSetting()
            .name("disable-placing-anchor-on-glowstone")
            .description("Disable placing a respawn anchor on glowstone.")
            .def(true)
            .build()
    );

    public final ModuleSetting<Boolean> disableDoubleAnchor = scGeneral.add(createBoolSetting()
            .name("disable-placing-anchor-on-anchor")
            .description("Disable placing a respawn anchor on top of another anchor.")
            .def(true)
            .build()
    );
    public final ModuleSetting<Boolean> disableDoubleGlowstone = scGeneral.add(createBoolSetting()
            .name("disable-placing-glowstone-on-glowstone")
            .description("Disable placing glowstone unless not targeting another glowstone.")
            .def(false)
            .build()
    );

    public ActionResult cannotPlace() {
        if (!isEnabled()) return ActionResult.SUCCESS;

        // Prevent placing a respawn anchor on another anchor
        if (disableDoubleAnchor.getVal()
                && HotbarUtils.isHoldingEitherHand(Items.RESPAWN_ANCHOR)
                && (BlockUtils.isLookingAt(Blocks.RESPAWN_ANCHOR) && !BlockUtils.isAnchorLoaded(1))) {
            return ActionResult.FAIL;
        }

        // Prevent placing a glowstone on another glowstone
        if (disableDoubleGlowstone.getVal()
                && HotbarUtils.isHoldingEitherHand(Items.GLOWSTONE)
                && BlockUtils.isLookingAt(Blocks.GLOWSTONE)) {
            return ActionResult.FAIL;
        }

        // Prevent placing a respawn anchor on glowstone
        if (disableAnchorOnGlowstone.getVal()
                && HotbarUtils.isHoldingEitherHand(Items.RESPAWN_ANCHOR)
                && BlockUtils.isLookingAt(Blocks.GLOWSTONE)) {
            return ActionResult.FAIL;
        }

        // Prevent placing glowstone if not targeting anchor or anchor is loaded
        if (disableGlowstonePlacement.getVal()
                && HotbarUtils.isHoldingEitherHand(Items.GLOWSTONE)
                && (!BlockUtils.isLookingAt(Blocks.RESPAWN_ANCHOR) || BlockUtils.isAnchorLoaded(1))) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }
}