package net.i_no_am.clickcrystals.addon.mixin;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.ModListWidget;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.ModMenuDisabler;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModsScreen.class)
public class MixinModsScreen {

    @Shadow private ModListWidget modList;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        modList.children().removeIf(mod -> Module.isEnabled(ModMenuDisabler.class) && ModMenuDisabler.isHidden(mod.getMod().getId()));
    }
}
