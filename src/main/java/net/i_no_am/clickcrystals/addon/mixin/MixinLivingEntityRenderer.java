package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.modules.misc.UpsideDown;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer {

    @ModifyReturnValue(method = "shouldFlipUpsideDown(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"))
    private boolean onRender(boolean original) {
        UpsideDown u = Module.get(UpsideDown.class);
        if (u.isEnabled())
            return !u.shouldIgnore(PlayerUtils.getClientWorld().getEntities());
        return original;
    }
}
