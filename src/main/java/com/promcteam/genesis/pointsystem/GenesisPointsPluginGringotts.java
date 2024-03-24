package com.promcteam.genesis.pointsystem;

import org.bukkit.OfflinePlayer;
import org.gestern.gringotts.Gringotts;

public class GenesisPointsPluginGringotts extends GenesisPointsPlugin {
    public GenesisPointsPluginGringotts() {
        super("Gringotts");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        return Gringotts.instance.getEco().player(player.getUniqueId()).balance();
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        Gringotts.instance.getEco().player(player.getUniqueId()).setBalance(points);
        return getPoints(player);
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        Gringotts.instance.getEco().player(player.getUniqueId()).withdraw(points);
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        Gringotts.instance.getEco().player(player.getUniqueId()).deposit(points);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return true;
    }
}
