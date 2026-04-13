package net.i_no_am.clickcrystals.addon.module.modules.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
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
    public final ModuleSetting<Double> green = scGeneral.add(createDoubleSetting()
            .name("Green")
            .description("Decide how much green will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> blue = scGeneral.add(createDoubleSetting()
            .name("Blue")
            .description("Decide how much blue will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> red = scGeneral.add(createDoubleSetting()
            .name("Red")
            .description("Decide how much red will be on the color pattern.")
            .def(255.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );
    public final ModuleSetting<Double> alpha = scGeneral.add(createDoubleSetting()
            .name("Alpha")
            .description("Decide the transparency level (0 is fully transparent, 255 is fully opaque).")
            .def(75.0)
            .max(255.0)
            .min(0.0)
            .decimalPlaces(1)
            .build()
    );

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
        int invertedAlpha = 255 - alpha.getVal().intValue();
        return new Color(red.getVal().intValue(), green.getVal().intValue(), blue.getVal().intValue(), invertedAlpha);
    }

    public void applyOverlayColor(DynamicTexture originalTexture) {
        NativeImage nativeImage = originalTexture.getPixels();
        if (nativeImage == null) return;
        Color color = getColor();

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (i < 8) {
                    nativeImage.setPixel(j, i, toArgb(color));
                } else {
                    int k = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeImage.setPixel(j, i, ARGB.white(k));
                }
            }
        }
        originalTexture.upload();
    }

    public void resetOverlayColor(DynamicTexture originalTexture) {
        NativeImage nativeImage = originalTexture.getPixels();
        if (nativeImage == null) return;

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


    private static int toArgb(Color color) {
        return (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }
}
