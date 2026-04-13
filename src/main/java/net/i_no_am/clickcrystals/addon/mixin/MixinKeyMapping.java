package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.modules.misc.SafeWalk;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyMapping.class)
public abstract class MixinKeyMapping implements Global {

    /**
     * @author ChamsBypass, Thunderhack <a href="https://github.com/Pan4ur/ThunderHack-Recode/blob/main/src/main/java/thunder/hack/injection/MixinKeyBinding.java">...</a>
     */
    @ModifyReturnValue(method = "isDown", at = @At("RETURN"))
    private boolean isDown(boolean original) {

        if (!this.equals(mc.options.keyShift)) return original;

        SafeWalk safeWalk = Module.get(SafeWalk.class);
        if (safeWalk == null || !safeWalk.isOn()) return original;

        if (PlayerUtils.invalid() || !mc.player.onGround()) return original;

        boolean isOnEdge = mc.level.getBlockState(new BlockPos((int) Math.floor(mc.player.getX()), (int) Math.floor(mc.player.getY()) - 1, (int) Math.floor(mc.player.getZ()))).isAir();

        return isOnEdge || original;
    }
}