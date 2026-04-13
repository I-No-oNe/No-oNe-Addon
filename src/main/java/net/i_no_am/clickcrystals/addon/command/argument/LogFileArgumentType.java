package net.i_no_am.clickcrystals.addon.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.itzispyder.clickcrystals.Global;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class LogFileArgumentType implements ArgumentType<String>, Global {

    public static final String LOGS_FOLDER = "logs";
    private static final DynamicCommandExceptionType FILE_NOT_FOUND = new DynamicCommandExceptionType(name ->
            Component.literal("Log file not found: " + name));

    public static LogFileArgumentType create() {
        return new LogFileArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        for (String file : getLogFileNames()) {
            if (file.equals(input)) {
                return file;
            }
        }
        throw FILE_NOT_FOUND.create(input);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getLogFileNames(), builder);
    }

    public List<String> getLogFileNames() {
        List<String> fileNames = new ArrayList<>();
        try {
            Files.list(Paths.get(LOGS_FOLDER))
                    .filter(Files::isRegularFile)
                    .filter(file -> !file.getFileName().toString().endsWith(".gz"))
                    .forEach(file -> fileNames.add(file.getFileName().toString()));
        } catch (IOException ex) {
            system.logger.error("Error getting logs ->" + ex);
        }
        return fileNames;
    }
}