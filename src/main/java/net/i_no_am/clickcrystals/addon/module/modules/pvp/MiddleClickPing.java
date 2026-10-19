package net.i_no_am.clickcrystals.addon.module.modules.pvp;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.phys.EntityHitResult;

public class MiddleClickPing extends AddonListenerModule {
    public MiddleClickPing() {
        super("middle-click-ping", "Middle-click a player to read their ping out in chat");
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {

        if (e.getButton() != InputConstants.MOUSE_BUTTON_MIDDLE || mc.hitResult == null || PlayerUtils.invalid()) return;

        if (mc.hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Player targetPlayer) {

            PlayerInfo entry = mc.getConnection().getPlayerInfo(targetPlayer.getUUID());

            if (entry != null) ChatUtils.sendPrefixMessage("Player " + ChatFormatting.AQUA + targetPlayer.getName().getString() + ChatFormatting.RESET + entry.getLatency() + " ms.");
            else ChatUtils.sendPrefixMessage("Could not retrieve ping for player " + ChatFormatting.AQUA + targetPlayer.getName().getString() + ChatFormatting.RESET + ".");
        }
    }
}