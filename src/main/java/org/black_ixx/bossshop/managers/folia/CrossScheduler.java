package org.black_ixx.bossshop.managers.folia;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CrossScheduler {

    private static final BossShop plugin = ClassManager.manager.getPlugin();
    private static final boolean isFolia = Bukkit.getVersion().contains("Folia");
    private static Map<Integer, Task> foliaTasks = new TreeMap<>();

    public static boolean isFolia() {
        return isFolia;
    }

    public static void run(Runnable runnable) {
        if(isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runAsync(Runnable runnable) {
        if(isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static Task runTaskLater(Runnable runnable, long delayTicks) {
        if(isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> runnable.run(), delayTicks));
        } else {
            return new Task(Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks));
        }
    }

    public static Task runTaskLaterAsync(Runnable runnable, long delay) {
        if(isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> runnable.run(), delay));
        } else {
            return new Task(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
        }
    }

    public static Task runTaskTimer(Runnable runnable, long delay, long period) {
        if(isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> runnable.run(), delay < 1 ? 1 : delay, period));
        } else {
            return new Task(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
        }
    }

    public static Task runTaskTimerAsync(Runnable runnable, long delay, long period) {
        if(isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> runnable.run(), delay < 1 ? 1 : delay, period));
        } else {
            return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
        }
    }

    public static <T> Future<T> callSyncMethod(Callable<T> task) {
        if(isFolia) {
            ClassManager.manager.getBugFinder().warn("Folia does not support cancelling tasks by ID");
            runTaskLater(() -> {
                try {
                    task.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, 1);
            return new CompletableFuture<>(); // Folia does not support returning a Future
        } else {
            return Bukkit.getScheduler().callSyncMethod(plugin, task);
        }
    }
    public static void cancelTask(int taskId) {
        if(isFolia) {
            ClassManager.manager.getBugFinder().warn("Folia does not support cancelling tasks by ID");
        } else {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public static void cancelTasks(Plugin plugin) {
        if(isFolia) {
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    public static class Task {
        private Object foliaTask;
        private BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
        }

        Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        public void cancel() {
            if(foliaTask != null)
                ((ScheduledTask) foliaTask).cancel();
            else
                bukkitTask.cancel();
        }
    }
}
