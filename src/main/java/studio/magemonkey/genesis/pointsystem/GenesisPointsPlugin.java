package studio.magemonkey.genesis.pointsystem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class GenesisPointsPlugin {
    private final String   name;
    private final String[] nicknames;
    private final boolean  available;

    public GenesisPointsPlugin(String mainName, String... nicknames) {
        this.name = mainName;
        this.nicknames = nicknames;
        this.available = Bukkit.getPluginManager().getPlugin(name) != null;
    }

    /**
     * Get the name of the points plugin
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the nicknames of the points plugin
     *
     * @return nicknames
     */
    public String[] getNicknames() {
        return nicknames;
    }

    /**
     * Check if points plugin is available
     *
     * @return available or not
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Get the points from an offline player
     *
     * @param player player to get points from
     * @return points of player
     */
    public abstract double getPoints(OfflinePlayer player);

    /**
     * Set the points of a player
     *
     * @param player player to modify
     * @param points points to set
     * @return points to set
     */
    public abstract double setPoints(OfflinePlayer player, double points);

    /**
     * Take points from a player
     *
     * @param player the player to modify
     * @param points points to take
     * @return amount taken
     */
    public abstract double takePoints(OfflinePlayer player, double points);

    /**
     * Give points to a player
     *
     * @param player player to modify
     * @param points the amount to give
     * @return amount given
     */
    public abstract double givePoints(OfflinePlayer player, double points);

    /**
     * Use double values
     *
     * @return true or false
     */
    public abstract boolean usesDoubleValues();


    /**
     * Register a plugin into the API
     */
    public void register() {
        GenesisPointsAPI.register(this);
    }
}
