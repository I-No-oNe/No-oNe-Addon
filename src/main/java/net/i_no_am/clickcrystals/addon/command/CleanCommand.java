package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import net.i_no_am.clickcrystals.addon.command.argument.LogFileArgumentType;
import net.minecraft.command.CommandSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static net.i_no_am.clickcrystals.addon.command.argument.LogFileArgumentType.LOGS_FOLDER;

public class CleanCommand extends Command {

    public CleanCommand() {
        super("clean", "Clears specific lines from chat or log files", "log-cleaner");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("chat").executes(ctx -> {
            mc.inGameHud.getChatHud().clear(true);
            return SINGLE_SUCCESS;
        })).then(literal("logs").then(argument("content", StringArgumentType.string()).executes(ctx -> {
            String content = StringArgumentType.getString(ctx, "content");
            return cleanAllLogFiles(content);
        }).then(argument("fileName", LogFileArgumentType.create()).executes(ctx -> {
            String content = StringArgumentType.getString(ctx, "content");
            String fileName = ctx.getArgument("fileName", String.class);
            return cleanLogFile(content, fileName);
        }))));
    }

    private int cleanLogFile(String lineToRemove, String fileName) {
        Path path = Paths.get(LOGS_FOLDER, fileName);
        if (!Files.exists(path)) {
            info("File not found: " + fileName);
            return SINGLE_SUCCESS;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            List<String> cleaned = lines.stream().filter(line -> !line.contains(lineToRemove)).toList();

            Files.write(path, cleaned);
            info("Removed " + (lines.size() - cleaned.size()) + " lines from " + fileName);
        } catch (IOException e) {
            info("Failed to clean file: " + fileName);
            system.logger.error("Error while clean file(" + fileName + ")" + "->" + e);
        }

        return SINGLE_SUCCESS;
    }

    private int cleanAllLogFiles(String lineToRemove) {
        int totalRemoved = 0;
        try {
            List<Path> files = Files.list(Paths.get(LOGS_FOLDER)).filter(Files::isRegularFile).filter(p -> !p.getFileName().toString().endsWith(".gz")).toList();

            for (Path file : files) {
                List<String> lines = Files.readAllLines(file);
                List<String> cleaned = lines.stream().filter(line -> !line.contains(lineToRemove)).toList();

                int removed = lines.size() - cleaned.size();
                totalRemoved += removed;
                Files.write(file, cleaned);
                info("Cleaned " + removed + " lines from " + file.getFileName());
            }
            info("Total lines removed: " + totalRemoved);
        } catch (IOException e) {
            info("Error while cleaning log files.");
            system.logger.error("Error while cleaning log files ->" + e);
        }
        return SINGLE_SUCCESS;
    }
}
