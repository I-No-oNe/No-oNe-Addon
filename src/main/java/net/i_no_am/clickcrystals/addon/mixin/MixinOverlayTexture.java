package net.i_no_am.clickcrystals.addon.mixin;

import net.i_no_am.clickcrystals.addon.accessor.OverlayTextureAccessor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OverlayTexture.class)
public class MixinOverlayTexture implements OverlayTextureAccessor {

    @Shadow @Final private DynamicTexture texture;

    @Override
    public DynamicTexture addon$getTexture() {
        return this.texture;
    }
}