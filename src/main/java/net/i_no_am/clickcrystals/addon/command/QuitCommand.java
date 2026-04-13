package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit", "Instantly quits the game or disconnects from server", ",quit");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> literalArgumentBuilder) {

        Component disconnectMessage = Component.literal("You Have Been Disconnected Via &aNo One Addon!");

        literalArgumentBuilder.executes(_ -> {
            PlayerUtils.sendPacket(new ClientboundDisconnectPacket(disconnectMessage));
            return SINGLE_SUCCESS;
        });

        LiteralArgumentBuilder.<SharedSuggestionProvider>literal("server")
                .executes(_ -> {
                    PlayerUtils.sendPacket(new ClientboundDisconnectPacket(disconnectMessage));
                    return SINGLE_SUCCESS;
                }).build();

        LiteralArgumentBuilder.<SharedSuggestionProvider>literal("mc")
                .executes(_ -> {
                    mc.destroy();
                    return SINGLE_SUCCESS;
                }).build();
    }
}
