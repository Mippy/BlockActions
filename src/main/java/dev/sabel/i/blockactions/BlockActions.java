package dev.sabel.i.blockactions;

import dev.sabel.i.blockactions.commands.BACommand;
import dev.sabel.i.blockactions.events.RightClickAction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockActions extends JavaPlugin {

    public final BlockActionStore bas = new BlockActionStore(this);
    public FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();

        bas.setup();

        getCommand("blockactions").setExecutor(new BACommand(this));

        Bukkit.getPluginManager().registerEvents(new RightClickAction(this), this);
        getLogger().info("Enabled!");
    }
}
