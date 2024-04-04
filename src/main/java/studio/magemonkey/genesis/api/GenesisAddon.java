package studio.magemonkey.genesis.api;

import studio.magemonkey.genesis.Genesis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GenesisAddon extends JavaPlugin {
    private Genesis bs;
    private boolean b = false;

    /////// //// //// //// //// ////

    @Override
    public void onEnable() {
        b = false;

        Plugin plugin = Bukkit.getPluginManager().getPlugin(Genesis.NAME);

        if (plugin == null) {
            printSevere(Genesis.NAME + " was not found... you need it in order to run " + getAddonName()
                    + "! Get it here: https://www.spigotmc.org/resources/25699/. Version v"
                    + getRequiredBossShopVersion() + " or newer is required!");
            printInfo("Disabling Addon...");
            b = true;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        bs = (Genesis) plugin;

        double bsWorth = getWorth(bs.getDescription().getVersion());
        double aoWorth = getWorth(getRequiredBossShopVersion());
        if (bsWorth < aoWorth) {
            printSevere(Genesis.NAME + " was found but it seems to be outdated... you need v"
                    + getRequiredBossShopVersion() + " or newer in order to run " + getAddonName()
                    + "! Get it here: https://www.spigotmc.org/resources/25699/");
            printInfo("Disabling Addon...");
            b = true;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        bs.getAPI().addEnabledAddon(this);

        Genesis.log("Enabling Addon " + getAddonName());
        enable();

    }

    @Override
    public void onDisable() {
        if (b) {
            return;
        }

        Genesis.log("Disabling Addon " + getAddonName());
        disable();
    }


    /**
     * Called to enable the addon
     */
    protected void enable() { // Can be overwritten
        enableAddon();
    }

    /**
     * Called to disable the addon
     */
    protected void disable() { // Can be overwritten
        disableAddon();
    }

    /**
     * Called to reload the addon
     *
     * @param sender the execute of the command
     */
    public void reload(CommandSender sender) { // Can be overwritten
        bossShopReloaded(sender);
        sender.sendMessage(
                ChatColor.YELLOW + "Reloaded " + Genesis.NAME + " Addon " + ChatColor.GOLD + getAddonName());
    }

    /////// //// //// //// //// ////

    /**
     * Prints a serve statement in the logger
     *
     * @param msg the message to log
     */
    public void printSevere(String msg) {
        Bukkit.getLogger().severe("[" + getAddonName() + "] " + msg);
    }

    /**
     * Prints a warning in the warning logger
     *
     * @param msg the message to log
     */
    public void printWarning(String msg) {
        Bukkit.getLogger().warning("[" + getAddonName() + "] " + msg);
    }

    /**
     * Prints information in the info logger
     *
     * @param msg message to log
     */
    public void printInfo(String msg) {
        Bukkit.getLogger().info("[" + getAddonName() + "] " + msg);
    }

    /**
     * Get an instance of the BossShop class
     *
     * @return instance of class
     */
    public final Genesis getBossShop() {
        return bs;
    }

    /////// //// //// //// //// ////


    protected double getWorth(String s) {
        try {
            if (s == null || s.isBlank()) {
                return 0;
            }
            s = s.replace("-SNAPSHOT", "");

            double   x     = 0;
            String[] parts = s.replace(".", ":").split(":");
            x += Integer.parseInt(parts[0].trim());
            if (parts.length == 2) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
            }
            if (parts.length == 3) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
                x += 0.01 * Integer.parseInt(parts[2].trim());
            }
            return x;
        } catch (Exception e) {
            printWarning("Was not able to get the version of " + s);
            return 1.00;
        }
    }

    /**
     * Creates store for an addon
     *
     * @param plugin the plugin addon
     * @param name   the name of the addon
     * @return new storage for an addon
     */
    public GenesisAddonStorage createStorage(Plugin plugin, String name) {
        return new GenesisAddonConfig(plugin, name);
    }

    /////// //// //// //// //// ////

    /**
     * Get the name of the addon
     *
     * @return name of addon
     */
    public abstract String getAddonName();

    /**
     * Get the version required for the addon to work
     *
     * @return version required
     */
    public abstract String getRequiredBossShopVersion();

    /**
     * Enables the addon
     */
    public abstract void enableAddon();

    public abstract void bossShopFinishedLoading();

    /**
     * Disables the addon
     */
    public abstract void disableAddon();

    public abstract void bossShopReloaded(CommandSender sender);
}
