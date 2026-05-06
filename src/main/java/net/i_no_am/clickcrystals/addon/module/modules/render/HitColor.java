package net.i_no_am.clickcrystals.addon.module.modules.render;

import com.mojang.blaze3d.platform.NativeImage;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.accessor.OverlayTextureAccessor;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ARGB;

import java.awt.*;


public class HitColor extends AddonListenerModule {
    public HitColor() {
        super("hit-color", "Changes the hit flash color of entities");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> color = scGeneral.add(createColorSetting()
            .name("glow-color-setting")
            .description("Set the color of the glowing effect")
            .def(0xFF00FFFF)
            .build());

    private DynamicTexture imageBackedTexture;

    @Override
    public void onDisable() {
        if (PlayerUtils.invalid() || imageBackedTexture == null) return;
        resetOverlayColor(imageBackedTexture);
    }

    @EventHandler
    private void onTick(ClientTickEndEvent event) {
        if (!isEnabled() || PlayerUtils.invalid()) return;
        imageBackedTexture = ((OverlayTextureAccessor) mc.gameRenderer.overlayTexture()).addon$getTexture();
        applyOverlayColor(imageBackedTexture);
    }


    public Color getColor() {
        return color.getVal().withAlpha(255 - color.getVal().getAlpha());
    }

    public void applyOverlayColor(DynamicTexture originalTexture) {
        NativeImage nativeImage = originalTexture.getPixels();
        if (nativeImage == null) return;

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (i < 8) {
                    nativeImage.setPixel(j, i, argb());
                } else {
                    int k = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeImage.setPixel(j, i, ARGB.white(k));
                }
            }
        }
        originalTexture.upload();
    }

    public void resetOverlayColor(DynamicTexture originalTexture) {
        if (imageBackedTexture == null) return;
        NativeImage nativeImage = originalTexture.getPixels();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (i < 8) {
                    // Reset to default vanilla hit color
                    nativeImage.setPixel(j, i, -1291911168);
                } else {
                    int k = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeImage.setPixel(j, i, ARGB.white(k));
                }
            }
        }
        originalTexture.upload();
    }


    private int argb() {
        int alpha = 255 - color.getVal().getAlpha();
        return (alpha << 24) | (color.getVal().getRed() << 16) | (color.getVal().getGreen() << 8) | color.getVal().getBlue();
    }
}
