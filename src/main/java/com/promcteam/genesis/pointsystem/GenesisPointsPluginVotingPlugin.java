package com.promcteam.genesis.pointsystem;

import com.bencodez.votingplugin.VotingPluginMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class GenesisPointsPluginVotingPlugin extends GenesisPointsPlugin {
    public GenesisPointsPluginVotingPlugin() {
        super("VotingPlugin", "VP");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        VotingPluginMain votingPlugin = (VotingPluginMain) Bukkit.getPluginManager().getPlugin("VotingPlugin");
        if (player instanceof Player) {
            return votingPlugin.getVotingPluginUserManager().getVotingPluginUser(player).getPoints();
        } else {
            return 0;
        }
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        VotingPluginMain votingPlugin = (VotingPluginMain) Bukkit.getPluginManager().getPlugin("VotingPlugin");
        if (player instanceof Player) {
            votingPlugin.getVotingPluginUserManager().getVotingPluginUser(player).setPoints((int) points);
            return points;
        } else {
            return 0;
        }
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        VotingPluginMain votingPlugin = (VotingPluginMain) Bukkit.getPluginManager().getPlugin("VotingPlugin");
        if (player instanceof Player) {
            votingPlugin.getVotingPluginUserManager().getVotingPluginUser(player).removePoints((int) points);
            return points;
        } else {
            return 0;
        }
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        VotingPluginMain votingPlugin = (VotingPluginMain) Bukkit.getPluginManager().getPlugin("VotingPlugin");
        if (player instanceof Player) {
            votingPlugin.getVotingPluginUserManager().getVotingPluginUser(player).addPoints((int) points);
            return getPoints(player);
        } else {
            return 0;
        }
    }


    @Override
    public boolean usesDoubleValues() {
        return false;
    }

}
