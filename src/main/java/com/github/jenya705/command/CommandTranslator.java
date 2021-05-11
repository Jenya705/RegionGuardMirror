package com.github.jenya705.command;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Getter
public class CommandTranslator implements CommandExecutor {

    private final ICommandExecutor localCommandExecutor;

    public CommandTranslator(ICommandExecutor executor){
        localCommandExecutor = executor;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        return localCommandExecutor.execute(commandSender, new ArgumentsIterator(args));
    }
}
