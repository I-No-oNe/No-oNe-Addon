package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.commands.Command;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import net.minecraft.command.CommandSource;
import com.mojang.brigadier.context.CommandContext;

public class FreeRAMCommand extends Command {
    public FreeRAMCommand() {
        super("free-ram", "Free the RAM of the local system", ",free-ram");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder.executes(this::execute);
    }

    private int execute(CommandContext<CommandSource> context) {
        System.gc();
        ChatUtils.sendPrefixMessage("Request freeing RAM.");
        return SINGLE_SUCCESS;
    }
}
