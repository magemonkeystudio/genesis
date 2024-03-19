package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshoppro.folia.CrossScheduler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ServerPingingRunnableHandler {

    private int id = -1;

    public void start(int speed, BossShop plugin, ServerPingingManager manager) {
        // TODO couldn't find proper solution for folia yet
        if(!CrossScheduler.isFolia()) {
            BukkitTask t = new ServerPingingRunnable(manager).runTaskTimerAsynchronously(plugin, speed, speed);
            id = t.getTaskId();
        }
    }

    public void stop() {
        if (id == -1) {
            return;
        }
        CrossScheduler.cancelTask(id);
    }


    public class ServerPingingRunnable extends BukkitRunnable {

        private final ServerPingingManager manager;

        public ServerPingingRunnable(ServerPingingManager manager) {
            this.manager = manager;
        }


        @Override
        public void run() {
            manager.update();
        }
    }

}