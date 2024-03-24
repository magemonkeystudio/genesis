package com.promcteam.genesis.api;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class GenesisAddonConfigurable extends GenesisAddon {


    private GenesisAddonConfig config;


    /**
     * Enables the config for the addon
     */
    protected void enable() {
        config = new GenesisAddonConfig(this, "config");
        super.enable();
    }

    /**
     * Disables the addon for the config
     */
    protected void disable() {
        super.disable();
        if (saveConfigOnDisable()) {
            config.save();
        }
    }

    /**
     * Reloads thje config for an addon
     *
     * @param sender the execute of the command
     */
    public void reload(CommandSender sender) {
        config.reload();
        super.reload(sender);
    }


    /**
     * Gets the config for the addon as a FileConfiguration
     *
     * @return config for addon
     */
    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    /**
     * Gets the config for the addon
     *
     * @return config for addon
     */
    public GenesisAddonConfig getAddonConfig() {
        return config;
    }

    /**
     * Reloads the config
     */
    @Override
    public void reloadConfig() {
        config.reload();
    }

    /**
     * Saves the config
     */
    @Override
    public void saveConfig() {
        config.save();
    }

    /**
     * Determines whether or not to save the config when the addon is disabled
     *
     * @return save or not
     */
    public abstract boolean saveConfigOnDisable();


}
