package com.github.jenya705.command;

import com.github.jenya705.RGPlugin;
import com.github.jenya705.RGUtils;
import com.github.jenya705.ValueContainer;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class RGCountCommand implements ICommandExecutor {

    @Override
    @SneakyThrows
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {

        if (!(commandSender instanceof Player)) return false;

        final RGPlugin plugin = RGPlugin.getInstance();
        final Player player = (Player) commandSender;
        final Selection selection = plugin.getWorldEdit().getSelection(player);
        if (selection != null){
            RGPlugin.runAsync(() -> {
                ValueContainer<Boolean> fully = new ValueContainer<>();
                ValueContainer<Boolean> intersects = new ValueContainer<>();
                List<ProtectedRegion> playerRegions = new ArrayList<>();
                int price = RGUtils.getRegionPrice(selection, player, intersects, fully, null,
                        new ValueContainer<>(playerRegions));
                if (price == -1){
                    player.sendMessage(plugin.getConfiguration().getStrangerRegionInSelection());
                    return;
                }
                if (!(RGUtils.canCreateRegionWithMessage(player, fully.getValue(), intersects.getValue(), selection))){
                    return;
                }
                StringBuilder messageBuilder = new StringBuilder(MessageFormat
                        .format(plugin.getConfiguration().getPriceMessage(), Integer.toString(price)));
                if (plugin.getConfiguration().isPrintCauseOfPriceCountCommand()) {
                    if (fully.getValue()) {
                        messageBuilder.append(plugin.getConfiguration().getPriceCauseFully());
                    }
                    else if (intersects.getValue()) {
                        messageBuilder.append(plugin.getConfiguration().getPriceCauseIntersect());
                    }
                    else {
                        messageBuilder.append(plugin.getConfiguration().getPriceCauseOut());
                    }
                }
                player.sendMessage(messageBuilder.toString());
            });
        }
        else {
            commandSender.sendMessage(plugin.getConfiguration().getSelectionNotExist());
        }

        return true;
    }
}
