package net.i_no_am.clickcrystals.addon.screen;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ButtonElement;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.i_no_am.clickcrystals.addon.client.data.Constants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;

public class AddonWelcomeScreen extends GuiScreen {

    public static boolean isClosed;
    public final int windowWidth = mc.getWindow().getGuiScaledWidth();
    public final int windowHeight = mc.getWindow().getGuiScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = windowWidth / 2 - baseWidth / 2;
    public final int baseY = windowHeight / 2 - baseHeight / 2;

    private final ButtonElement websiteButton = new ButtonElement(
            "Visit Addon Website",
            0, 0, 180, 25,
            (mx, my, self) -> system.openUrl(Constants.URL.SITE)
    );

    private final ButtonElement closeButton = new ButtonElement(
            "Close",
            0, 0, 180, 25,
            (mx, my, self) -> setClose()
    );

    public AddonWelcomeScreen() {
        super("addon-welcome-screen");
        this.addChild(websiteButton);
        this.addChild(closeButton);
    }

    @Override
    public void baseRender(GuiGraphicsExtractor graphicsExtractor, int mouseX, int mouseY, float delta) {
        this.extractPanorama(graphicsExtractor, delta);
        this.extractBlurredBackground(graphicsExtractor);
        this.extractMenuBackground(graphicsExtractor);

        int cX = baseX + baseWidth / 2;
        int cY = baseY + 40;

        String text = StringUtils.color("&aWelcome to No oNe's Addon!");
        RenderUtils.drawDefaultCenteredScaledText(graphicsExtractor, Component.literal(text), cX, cY, 1.2F, true);
        cY += 40;

        text = StringUtils.color("&7This addon extends ClickCrystals with new modules, new Commands,");
        List<FormattedCharSequence> lines1 = mc.font.split(FormattedText.of(text), baseWidth - 20);
        for (FormattedCharSequence line : lines1) {
            graphicsExtractor.centeredText(mc.font, line, cX, cY, 0xFFFFFFFF);
            cY += 15;
        }

        text = StringUtils.color("&7and customization options to enhance your experience.");
        List<FormattedCharSequence> lines2 = mc.font.split(FormattedText.of(text), baseWidth - 20);
        for (FormattedCharSequence line : lines2) {
            graphicsExtractor.centeredText(mc.font, line, cX, cY, 0xFFFFFFFF);
            cY += 15;
        }

        // Place buttons
        int btnSpacing = 35;
        websiteButton.x = cX - websiteButton.width / 2;
        websiteButton.y = cY + 30;

        closeButton.x = cX - closeButton.width / 2;
        closeButton.y = websiteButton.y + btnSpacing;
    }

    public void setClose() {
        isClosed = true;
        mc.setScreen(null);
    }
}
