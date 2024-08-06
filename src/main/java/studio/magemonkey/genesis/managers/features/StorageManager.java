package studio.magemonkey.genesis.managers.features;

import studio.magemonkey.genesis.Genesis;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class StorageManager {


    private final String            fileName = "data";
    private final File              file;
    private       FileConfiguration config   = null;

    public StorageManager(final Genesis plugin) {
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
        reloadConfig();
    }

    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();

        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            getConfig().save(file);
        } catch (IOException e) {
            Genesis.log("Could not save plugin data to " + file);
        }
    }


}
