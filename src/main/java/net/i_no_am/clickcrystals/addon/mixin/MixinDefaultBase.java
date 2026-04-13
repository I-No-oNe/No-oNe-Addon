package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.itzispyder.clickcrystals.gui.screens.DefaultBase;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.GUIFeaturesDisabler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DefaultBase.class)
public class MixinDefaultBase {

    @WrapMethod(method = "renderAnnouncementPing")
    private void onRenderAnnouncementPing(GuiGraphicsExtractor context, Operation<Void> original) {
        // If the feature is enabled, skip rendering the announcement ping.
        var guiFeaturesDisabler = Module.get(GUIFeaturesDisabler.class);
        if (!guiFeaturesDisabler.isEnabled() && guiFeaturesDisabler.newsDisable.getVal())
            original.call(context);
    }
}