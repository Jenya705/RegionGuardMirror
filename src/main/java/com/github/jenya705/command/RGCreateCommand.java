package com.github.jenya705.command;

import com.github.jenya705.RGCreatingInfo;
import com.github.jenya705.RGPlugin;
import com.github.jenya705.RGUtils;
import com.github.jenya705.ValueContainer;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class RGCreateCommand implements ICommandExecutor {

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {
        if (!(commandSender instanceof Player)) return false;
        final RGPlugin plugin = RGPlugin.getInstance();
        if (!args.hasNext()){
            commandSender.sendMessage(plugin.getConfiguration().getRegionNameNotGiven());
            return true;
        }
        final String regionName = args.next();
        final Player player = (Player) commandSender;
        if (plugin.getWorldGuard().getRegionManager(player.getWorld()).hasRegion(regionName)){
            commandSender.sendMessage(plugin.getConfiguration().getRegionWithNameAlreadyExists());
            return true;
        }
        final Selection selection = plugin.getWorldEdit().getSelection(player);
        if (selection != null){
            RGPlugin.runAsync(() -> {
                List<ProtectedRegion> regionsIntersects = new ArrayList<>();
                List<ProtectedRegion> playerRegions = new ArrayList<>();
                ValueContainer<Boolean> intersects = new ValueContainer<>();
                ValueContainer<Boolean> fully = new ValueContainer<>();
                int price = RGUtils.getRegionPrice(selection, player, intersects, fully,
                        new ValueContainer<>(regionsIntersects), new ValueContainer<>(playerRegions));
                if (price == -1) {
                    player.sendMessage(plugin.getConfiguration().getStrangerRegionInSelection());
                    return;
                }
                boolean firstPlayerRegion = false;
                if (!fully.getValue()) {
                    if (!RGUtils.canCreateRegionWithMessage(player, fully.getValue(), intersects.getValue(), selection)) {
                        return;
                    }
                    if (playerRegions.isEmpty() && RGUtils.isRegionFree(selection)) {
                        price = 0;
                        firstPlayerRegion = true;
                    }
                }
                if (!firstPlayerRegion) {
                    StringBuilder messageBuilder = new StringBuilder(MessageFormat.format(
                            plugin.getConfiguration().getConfirmCreate(), regionName, Integer.toString(price)));
                    if (plugin.getConfiguration().isPrintCauseOfPriceCreateCommand()) {
                        if (regionsIntersects.isEmpty()) messageBuilder.append(
                                plugin.getConfiguration().getRegionIntersectsNotFound());
                        else {
                            messageBuilder.append(plugin.getConfiguration().getRegionIntersectsFound());
                            for (int i = 0; i < plugin.getConfiguration().getRegionsOnPage() && i < regionsIntersects.size(); ++i) {
                                messageBuilder.append("\n").append(regionsIntersects.get(i).getId());
                            }
                            if (regionsIntersects.size() > plugin.getConfiguration().getRegionsOnPage()) {
                                messageBuilder.append("\n").append(MessageFormat.format(
                                        plugin.getConfiguration().getRegionMore(), Integer.toString(
                                                regionsIntersects.size() - plugin.getConfiguration().getRegionsOnPage())));
                            }
                        }
                    }
                    player.sendMessage(messageBuilder.toString());
                }
                else {
                    player.sendMessage(MessageFormat.format(plugin.getConfiguration().getFirstFreeRegion(),
                            Integer.toString(plugin.getConfiguration().getFreeRegionSize())) + "\n" + MessageFormat.format(
                                    plugin.getConfiguration().getConfirmCreate(), regionName, Integer.toString(price)));
                }
                plugin.getConfirm().put(player.getUniqueId(), new RGCreatingInfo(
                        selection.getNativeMinimumPoint().toBlockPoint(),
                        selection.getNativeMaximumPoint().toBlockVector(),
                        regionName, price, selection.getWorld()));
            });
        }
        else {
            player.sendMessage(plugin.getConfiguration().getSelectionNotExist());
        }
        return true;
    }
}
