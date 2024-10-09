package studio.magemonkey.genesis.pointsystem;

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import org.bukkit.OfflinePlayer;


public class GenesisPointsPluginTokenEnchant extends GenesisPointsPlugin {

    public GenesisPointsPluginTokenEnchant() {
        super("TokenEnchant", "TokenEnchants", "TE");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        return TokenEnchantAPI.getInstance().getTokens(player);
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        TokenEnchantAPI.getInstance().setTokens(player, points);
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        TokenEnchantAPI.getInstance().removeTokens(player, points);
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        TokenEnchantAPI.getInstance().addTokens(player, points);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return true;
    }

}
