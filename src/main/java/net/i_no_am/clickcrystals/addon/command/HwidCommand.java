package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import net.i_no_am.clickcrystals.addon.utils.OsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class HwidCommand extends Command {
    public HwidCommand(){
        super("hwid","Displays the user's HWID (Hardware ID)",",hwid");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> build) {
        build.executes(context -> {
            info( "Your HWID is: "+ ChatFormatting.GOLD + OsUtils.getHWID());
            OsUtils.copy(OsUtils.getHWID());
            return SINGLE_SUCCESS;
        });
    }
}
