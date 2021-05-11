package com.github.jenya705;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RGHandler implements Listener {

    @EventHandler
    public void playerQuit(PlayerQuitEvent event){

        RGPlugin plugin = RGPlugin.getInstance();
        plugin.getConfirm().remove(event.getPlayer().getUniqueId());

    }

}
