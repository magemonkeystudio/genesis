package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshoppro.folia.CrossScheduler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AutoRefreshHandler {

    private int id = -1;

    public void start(int speed, BossShop plugin) {
        // TODO couldn't find proper solution for folia yet
        if(!CrossScheduler.isFolia()) {
            BukkitTask t = new AutoRefreshRunnable(plugin).runTaskTimer(plugin, speed, speed);
            id = t.getTaskId();
        }
    }

    public void stop() {
        if (id == -1) {
            return;
        }
        CrossScheduler.cancelTask(id);
    }


    public class AutoRefreshRunnable extends BukkitRunnable {

        private BossShop plugin;

        public AutoRefreshRunnable(BossShop plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            plugin.getClassManager().getShops().refreshShops(false);
        }
    }

}