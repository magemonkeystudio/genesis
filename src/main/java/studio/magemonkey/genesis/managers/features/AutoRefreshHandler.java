package studio.magemonkey.genesis.managers.features;

import studio.magemonkey.genesis.Genesis;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AutoRefreshHandler {

    private int id = -1;

    public void start(int speed, Genesis plugin) {
        BukkitTask t = new AutoRefreshRunnable(plugin).runTaskTimer(plugin, speed, speed);
        id = t.getTaskId();
    }

    public void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
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