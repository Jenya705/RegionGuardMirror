package com.github.jenya705.command;

import com.github.jenya705.RGCreatingInfo;
import com.github.jenya705.RGPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RGConfirmCommand implements ICommandExecutor{

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {

        if (!(commandSender instanceof Player)) return false;

        final Player player = (Player) commandSender;
        final RGPlugin plugin = RGPlugin.getInstance();
        final RGCreatingInfo regionInfo = plugin.getConfirm()
                .getOrDefault(player.getUniqueId(), null);
        if (regionInfo == null || !regionInfo.isSelection(
                plugin.getWorldEdit().getSelection(player))){
            commandSender.sendMessage(plugin.getConfiguration().getConfirmBeforeCreate());
            return true;
        }
        int price = regionInfo.getRegionPrice();
        if (price <= plugin.getVaultEconomy().getBalance(player)) { // can buy
            EconomyResponse response = plugin.getVaultEconomy().withdrawPlayer(player, price);
            if (response.transactionSuccess()){ // success transaction wow
                final WorldGuardPlugin worldGuard = plugin.getWorldGuard();
                ProtectedCuboidRegion region = new ProtectedCuboidRegion(
                        regionInfo.getRegionName(),
                        regionInfo.getSelectionMin(),
                        regionInfo.getSelectionMax()
                );
                region.getOwners().addPlayer(player.getUniqueId());
                region.setFlag(plugin.getPriceFlag(), price);
                worldGuard.getRegionManager(regionInfo.getWorld()).addRegion(region);
                player.sendMessage(plugin.getConfiguration().getRegionCreated());
            }
            else { // transaction failed
                player.sendMessage(plugin.getConfiguration().getTransactionError());
            }
        }
        else { // can not buy
            player.sendMessage(plugin.getConfiguration().getBalanceLessThanPrice());
        }
        return true;

    }
}
