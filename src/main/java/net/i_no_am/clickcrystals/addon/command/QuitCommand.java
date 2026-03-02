package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit", "Instantly quits the game or disconnects from server", ",quit");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {

        Text disconnectMessage = Text.literal("You Have Been Disconnected Via &aNo One Addon!");

        literalArgumentBuilder.executes(context -> {
            PlayerUtils.sendPacket(new DisconnectS2CPacket(disconnectMessage));
            return SINGLE_SUCCESS;
        });

        literalArgumentBuilder.then(LiteralArgumentBuilder.<CommandSource>literal("server")
                .executes(context -> {
                    PlayerUtils.sendPacket(new DisconnectS2CPacket(disconnectMessage));
                    return SINGLE_SUCCESS;
                })
        );

        literalArgumentBuilder.then(LiteralArgumentBuilder.<CommandSource>literal("mc")
                .executes(context -> {
                    mc.stop();
                    return SINGLE_SUCCESS;
                })
        );
    }
}
