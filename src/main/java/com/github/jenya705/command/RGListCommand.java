package com.github.jenya705.command;

import com.github.jenya705.RGPlugin;
import com.github.jenya705.RGUtils;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RGListCommand implements ICommandExecutor{

    @Override
    public boolean execute(CommandSender commandSender, ArgumentsIterator args) {

        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        RGPlugin plugin = RGPlugin.getInstance();
        RGPlugin.runAsync( () -> {
            Map<String, ProtectedRegion> playerRegions = RGUtils.getPlayerRegions(player,
                    plugin.getConfiguration().isListPrintMemberRegions());
            List<Map.Entry<String, ProtectedRegion>> listedPlayerRegions = new ArrayList<>(playerRegions.entrySet());
            StringBuilder stringBuilder = new StringBuilder(plugin.getConfiguration().getListRegion());
            for (int i = 0; i < plugin.getConfiguration().getListRegionCount() && i < playerRegions.size(); ++i) {
                stringBuilder.append("\n").append(listedPlayerRegions.get(i).getKey());
            }
            if (plugin.getConfiguration().getListRegionCount() < playerRegions.size()) {
                stringBuilder.append("\n").append(MessageFormat.format(
                        plugin.getConfiguration().getRegionMore(),
                        playerRegions.size() - plugin.getConfiguration().getListRegionCount()));
            }
            player.sendMessage(stringBuilder.toString());
        });
        return true;
    }
}
