package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.modules.misc.NameChanger;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StringDecomposer.class)
public class MixinStringDecomposer implements Global {
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z", ordinal = 0), method = {"iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z"}, index = 0)
    private static String adjustText(String text) {
        return protectName(text);
    }

    @Unique
    private static String protectName(String string) {
        if (!Module.get(NameChanger.class).isEnabled() || PlayerUtils.invalid()) return string;
        var s = mc.getUser().getName();
        if (string.contains(s)) return string.replace(s, Module.get(NameChanger.class).fakeName.getVal());
        return string;
    }

}