package com.github.jenya705.command;

import com.github.jenya705.RGPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class RGRemoveCommand implements ICommandExecutor{

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {
        if (!(commandSender instanceof Player)) return false;
        RGPlugin plugin = RGPlugin.getInstance();
        if (!args.hasNext()) {
            commandSender.sendMessage(plugin.getConfiguration().getRegionNameNotGiven());
            return true;
        }
        Player player = (Player) commandSender;
        String regionName = args.next();
        RGPlugin.runAsync(() -> {
            RegionManager manager = plugin.getWorldGuard().getRegionManager(player.getWorld());
            if (!manager.hasRegion(regionName)) {
                player.sendMessage(plugin.getConfiguration().getRemoveRegionNotExist());
                return;
            }
            ProtectedRegion regionToBeDeleted = manager.getRegion(regionName);
            if (!regionToBeDeleted.getOwners().contains(player.getUniqueId()) &&
                    !regionToBeDeleted.getOwners().contains(player.getName())) {
                player.sendMessage(plugin.getConfiguration().getRemoveRegionNotOwner());
                return;
            }
            int moneyToReturn = (int) (regionToBeDeleted.getFlag(plugin.getPriceFlag())
                    / plugin.getConfiguration().getRemoveReturnCoefficient());
            manager.removeRegion(regionName);
            plugin.getVaultEconomy().depositPlayer(player, moneyToReturn);
            player.sendMessage(MessageFormat.format(plugin.getConfiguration()
                    .getRemoveRegionSuccess(), regionName, Integer.toString(moneyToReturn)));
        });
        return true;
    }
}
