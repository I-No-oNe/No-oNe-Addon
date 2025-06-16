package net.i_no_am.clickcrystals.addon.module.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.listener.events.mc.OverlayTextureInitEvent;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;


public class HitColor extends AddonListenerModule {
    public HitColor() {
        super("hit-color", "Change entity hit color.");
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

    NativeImageBackedTexture imageBackedTexture;

    @EventHandler
    private void onInit(OverlayTextureInitEvent event) {
        imageBackedTexture = event.getImageBackedTexture();
    }

    @Override
    public void onDisable() {
        if (PlayerUtils.invalid() || imageBackedTexture == null) return;
        resetOverlayColor(imageBackedTexture);
    }

    @EventHandler
    private void onTick(ClientTickEndEvent event) {
        if (!isEnabled()|| imageBackedTexture == null || PlayerUtils.invalid()) return;
        applyOverlayColor(imageBackedTexture);
    }


    public Color getColor() {
        int invertedAlpha = 255 - alpha.getVal().intValue();
        return new Color(red.getVal().intValue(), green.getVal().intValue(), blue.getVal().intValue(), invertedAlpha);
    }

    public void applyOverlayColor(NativeImageBackedTexture originalTexture) {
        NativeImage nativeImage = originalTexture.getImage();
        if (nativeImage == null) return;
        Color color = getColor();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (i < 8) {
                    nativeImage.setColorArgb(j, i, toArgb(color));
                } else {
                    int alpha = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeImage.setColorArgb(j, i, ColorHelper.withAlpha(alpha, -1));
                }
                RenderSystem.setupOverlayColor(originalTexture.getGlTexture());
            }
        }

        uploadTexture(originalTexture);
    }

    public void resetOverlayColor(NativeImageBackedTexture originalTexture) {
        NativeImage nativeImage = originalTexture.getImage();
        if (nativeImage == null) return;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (i < 8) {
                    // Reset to default vanilla hit color
                    nativeImage.setColorArgb(j, i, -1291911168);
                } else {
                    int alpha = (int) ((1.0F - (float) j / 15.0F * 0.75F) * 255.0F);
                    nativeImage.setColorArgb(j, i, ColorHelper.withAlpha(alpha, -1));
                }
            }
        }
        uploadTexture(originalTexture);
        RenderSystem.setupOverlayColor(originalTexture.getGlTexture());
    }

    private void uploadTexture(NativeImageBackedTexture texture) {
        texture.setFilter(false, false);
        texture.setClamp(true);
        texture.upload();
    }


    private static int toArgb(Color color) {
        return (color.getAlpha() << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }
}
