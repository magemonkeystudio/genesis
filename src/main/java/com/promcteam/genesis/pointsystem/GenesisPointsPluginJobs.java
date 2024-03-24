package com.promcteam.genesis.pointsystem;

import com.gamingmesh.jobs.Jobs;
import org.bukkit.OfflinePlayer;

public class GenesisPointsPluginJobs extends GenesisPointsPlugin {

    public GenesisPointsPluginJobs() {
        super("Jobs", "JobsReborn");
    }

    @Override
    public double getPoints(OfflinePlayer player) {
        return Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId()).getPointsData().getCurrentPoints();
    }

    @Override
    public double setPoints(OfflinePlayer player, double points) {
        Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId()).setPoints(points);
        return points;
    }

    @Override
    public double takePoints(OfflinePlayer player, double points) {
        Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId()).takePoints(points);
        return getPoints(player);
    }

    @Override
    public double givePoints(OfflinePlayer player, double points) {
        Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId()).addPoints(points);
        return getPoints(player);
    }

    @Override
    public boolean usesDoubleValues() {
        return true;
    }

}
