package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import net.minecraft.command.CommandSource;

public class FreeRAMCommand extends Command {
    public FreeRAMCommand() {
        super("free-ram", "Attempts to free up system memory", ",free-ram");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Request freeing RAM.");
            System.gc();
            info("Freed RAM.");
            return SINGLE_SUCCESS;
        });
    }
}
