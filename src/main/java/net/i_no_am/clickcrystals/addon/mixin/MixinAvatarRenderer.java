package net.i_no_am.clickcrystals.addon.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.modules.misc.UpsideDown;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AvatarRenderer.class)
public class MixinAvatarRenderer<T extends LivingEntity> {

    @WrapMethod(method = "isEntityUpsideDown(Lnet/minecraft/world/entity/LivingEntity;)Z")
    public boolean onShouldFlipUpsideDown(T entity, Operation<Boolean> original) {
        return Module.get(UpsideDown.class).apply(original.call(entity), entity);
    }
}