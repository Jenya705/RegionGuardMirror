package com.github.jenya705.command;

import com.github.jenya705.RGPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class RGPosCommand implements ICommandExecutor {

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {
        if (!(commandSender instanceof Player)) return false;
        final String posName = args.getArgs()[args.getCurrent() - 1];
        Bukkit.dispatchCommand(commandSender, "/h" + posName.toLowerCase(Locale.ROOT));
        return true;
    }
}
