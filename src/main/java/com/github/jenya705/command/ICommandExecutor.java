package com.github.jenya705.command;

import org.bukkit.command.CommandSender;

public interface ICommandExecutor {

    boolean execute(CommandSender commandSender, ArgumentsIterator args);

}
