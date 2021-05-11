package com.github.jenya705;

import com.github.jenya705.command.CommandTranslator;
import com.github.jenya705.command.RGCommand;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class RGPlugin extends JavaPlugin {

    @Getter
    private static RGPlugin instance;

    private final IntegerFlag priceFlag = new IntegerFlag("rg-price", RegionGroup.NONE);
    private WorldGuardPlugin worldGuard;
    private WorldEditPlugin worldEdit;
    private Map<UUID, RGCreatingInfo> confirm;
    private RGConfig configuration;
    private Economy vaultEconomy;

    @Override
    public void onLoad() {
        worldGuard = WorldGuardPlugin.inst();
        getWorldGuard().getFlagRegistry().register(priceFlag);
    }

    @SneakyThrows
    @Override
    public void onEnable() {

        getDataFolder().mkdir();

        instance = this;
        configuration = new RGConfig();
        worldEdit = ((WorldEditPlugin) getServer().getPluginManager()
                .getPlugin("WorldEdit"));
        confirm = new HashMap<>();
        vaultEconomy = getServer().getServicesManager()
                .getRegistration(Economy.class).getProvider();
        getCommand("rg").setExecutor(new CommandTranslator(new RGCommand()));
        getServer().getPluginManager().registerEvents(new RGHandler(), this);

    }

    @Override
    public void onDisable() {

    }

    public static void runAsync(Runnable runnable){
        getInstance().getServer().getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }

    public static void runOnNextSync(Runnable runnable){
        getInstance().getServer().getScheduler().runTaskLater(getInstance(), runnable, 1);
    }

}
