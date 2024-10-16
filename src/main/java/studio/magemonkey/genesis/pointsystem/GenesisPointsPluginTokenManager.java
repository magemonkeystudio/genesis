package studio.magemonkey.genesis.pointsystem;

import me.realized.tokenmanager.TokenManagerPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.OptionalLong;


public class GenesisPointsPluginTokenManager extends GenesisPointsPlugin {
    private final TokenManagerPlugin tm;

    public GenesisPointsPluginTokenManager() {
        super("TokenManager", "TM");
        tm = TokenManagerPlugin.getInstance();
    }


    @Override
    public double getPoints(OfflinePlayer player) {
        if (player instanceof Player) {
            OptionalLong l = tm.getTokens((Player) player);
            return l.isPresent() ? l.getAsLong() : 0;
        }
        return -1;
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            tm.setTokens((Player) player, (long) points);
            return points;
        } else {
            return -1;
        }
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            return this.setPoints(player, this.getPoints(player) - points);
        } else {
            return -1;
        }
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        if (player instanceof Player) {
            return this.setPoints(player, this.getPoints(player) + points);
        } else {
            return -1;
        }
    }


    @Override
    public boolean usesDoubleValues() {
        return false;
    }
}
