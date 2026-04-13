package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class FreeRAMCommand extends Command {
    public FreeRAMCommand() {
        super("free-ram", "Attempts to free up system memory", ",free-ram");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(context -> {
            info("Request freeing RAM.");
            System.gc();
            info("Freed RAM.");
            return SINGLE_SUCCESS;
        });
    }
}
