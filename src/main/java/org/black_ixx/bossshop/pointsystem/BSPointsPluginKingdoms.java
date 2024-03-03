package org.black_ixx.bossshop.pointsystem;

import org.bukkit.OfflinePlayer;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;

public class BSPointsPluginKingdoms extends BSPointsPlugin {
    public BSPointsPluginKingdoms() {
        super("Kingdoms");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
        if (kp.getKingdom() != null) {
            Kingdom kingdom = kp.getKingdom();
            if (kingdom != null) {
                return kingdom.getResourcePoints();
            }
        }
        return 0;
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
        if (kp.getKingdom() != null) {
            Kingdom kingdom = kp.getKingdom();
            if (kingdom != null) {
                kingdom.setResourcePoints((long) points);
                return kingdom.getResourcePoints();
            }
        }
        return 0;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        return setPoints(player, (long) (getPoints(player) - points));
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);
        if (kp.getKingdom() != null) {
            Kingdom kingdom = kp.getKingdom();
            if (kingdom != null) {
                kingdom.addResourcePoints((long) points);
                return kingdom.getResourcePoints();
            }
        }
        return 0;
    }

    @Override
    public boolean usesDoubleValues() {
        return true;
    }
}
