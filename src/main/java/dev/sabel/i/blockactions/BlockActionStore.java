package dev.sabel.i.blockactions;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class BlockActionStore {

    private final BlockActions plugin;
    private File file;

    public FileConfiguration data;
    public HashMap<Material, String> actions;

    public BlockActionStore(BlockActions p) {
        plugin = p;
    }

    public final void setup() {
        actions = new HashMap<>();
        file = new File(plugin.getDataFolder(), "actions.yml");
        boolean fe = file.exists();
        if (!fe) {
            try {
                boolean s = file.createNewFile();
                if (!s) {
                    plugin.getLogger().warning("Could not create actions.yml file. Check permissions?");
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create actions.yml file!");
                e.printStackTrace();
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
        if (fe) load();
        save();
    }

    public final void load() {
        actions = new HashMap<>();
        data = YamlConfiguration.loadConfiguration(file);
        if (data.getConfigurationSection("actions") == null) return;
        for (String a : data.getConfigurationSection("actions").getKeys(false)) {
            Material m = Material.getMaterial(a);
            if (m == null) {
                plugin.getLogger().warning("Invalid actions.yml! Plugin failed to load.");
                actions = new HashMap<>();
                return;
            }
            actions.put(m, data.getString("actions." + m));
        }
    }

    public final void save() {
        try {
            data.set("actions", "");
            actions.forEach((m, a) -> data.set("actions." + m.toString(), a));
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
