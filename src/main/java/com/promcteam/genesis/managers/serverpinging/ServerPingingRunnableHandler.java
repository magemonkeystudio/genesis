package com.promcteam.genesis.managers.serverpinging;

import com.promcteam.genesis.Genesis;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ServerPingingRunnableHandler {

    private int id = -1;

    public void start(int speed, Genesis plugin, ServerPingingManager manager) {
        BukkitTask t = new ServerPingingRunnable(manager).runTaskTimerAsynchronously(plugin, speed, speed);
        id = t.getTaskId();
    }

    public void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
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