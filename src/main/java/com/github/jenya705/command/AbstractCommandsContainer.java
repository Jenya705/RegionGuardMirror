package com.github.jenya705.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public abstract class AbstractCommandsContainer implements ICommandExecutor {

    private final Map<String, ICommandExecutor> commandExecutors;

    public AbstractCommandsContainer(){
        commandExecutors = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {
        if (!args.hasNext()) return false;
        ICommandExecutor commandExecutor = getCommandExecutors().get(args.next().toLowerCase(Locale.ROOT));
        if (commandExecutor == null) return false;
        return commandExecutor.execute(commandSender, args);
    }

}
