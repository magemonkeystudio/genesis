package studio.magemonkey.genesis.managers.features;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.folia.CrossScheduler;

public class AutoRefreshHandler {

    private int id = -1;

    public void start(int speed, Genesis plugin) {
        // TODO couldn't find proper solution for folia yet
        if (!CrossScheduler.isFolia()) {
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

        private final Genesis plugin;

        public AutoRefreshRunnable(Genesis plugin) {
            this.plugin = plugin;
        }

        @Override
        public void run() {
            plugin.getClassManager().getShops().refreshShops(false);
        }
    }

}