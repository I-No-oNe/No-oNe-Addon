package net.i_no_am.clickcrystals.addon.screen;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.HyperLinkElement;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.i_no_am.clickcrystals.addon.utils.OsUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class AddonBanScreen extends GuiScreen {
    public final int windowWidth = mc.getWindow().getScaledWidth();
    public final int windowHeight = mc.getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = windowWidth / 2 - baseWidth / 2;
    public final int baseY = windowHeight / 2 - baseHeight / 2;

    private final HyperLinkElement discordLink = new HyperLinkElement(0, 0, "https://discord.com/users/1051897115447660697", "https://discord.com/users/I-No-oNe", 1.0F);

    public AddonBanScreen() {
        super("addon-ban-screen");
        this.addChild(discordLink);
    }

    @Override
    public void baseRender(DrawContext context, int mouseX, int mouseY, float delta) {
        if (PlayerUtils.invalid()) {
            this.renderPanoramaBackground(context, delta);
        }
        this.applyBlur();
        this.renderDarkening(context);

        int cX = baseX + baseWidth / 2;
        int cY = baseY + baseHeight / 6;
        String text;

        text = StringUtils.color("&cYou Aren't In The Addon Whitelist");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);
        cY += 30;
        text = StringUtils.color("&cReason:\n&7%s\n&eHWID: &f&%s").formatted("§aThis Addon Is Private", " " + OsUtils.getHWID());
        var lines = mc.textRenderer.wrapLines(StringVisitable.plain(text), baseWidth);
        for (OrderedText line : lines) {
            context.drawCenteredTextWithShadow(mc.textRenderer, line, cX, cY, 0xFFFFFFFF);
            cY += 20;
        }

        cY += 15;
        text = StringUtils.color("&cDM I-No-oNe For Access");
        RenderUtils.drawDefaultCenteredScaledText(context, Text.literal(text), cX, cY += 10, 1.0F, true);
        cY += 10;
        discordLink.x = cX - discordLink.width / 2;
        discordLink.y = cY + 10;
    }


    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new AddonBanScreen());
    }
}
