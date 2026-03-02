package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.UpsideDown;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer<T extends LivingEntity> {

    @WrapMethod(method = "shouldFlipUpsideDown(Lnet/minecraft/entity/LivingEntity;)Z")
    public boolean onShouldFlipUpsideDown(T entity, Operation<Boolean> original) {
        return Module.get(UpsideDown.class).apply(original.call(entity), entity);
    }
}