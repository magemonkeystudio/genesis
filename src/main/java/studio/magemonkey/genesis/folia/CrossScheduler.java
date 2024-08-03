package studio.magemonkey.genesis.folia;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import studio.magemonkey.foliabridge.FoliaBridge;
import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CrossScheduler {

    private static final Genesis plugin  = ClassManager.manager.getPlugin();
    private static final boolean isFolia = Bukkit.getVersion().contains("Folia");

    public static boolean isFolia() {
        return isFolia;
    }

    public static void run(Runnable runnable) {
        if (isFolia) {
            FoliaBridge.execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runAsync(Runnable runnable) {
        if (isFolia) {
            FoliaBridge.execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static void runTaskLater(Runnable runnable, long delayTicks) {
        if (isFolia) {
            FoliaBridge.runDelayed(plugin, runnable, delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
        }
    }

    public static void runTaskLaterAsync(Runnable runnable, long delay) {
        if (isFolia) {
            FoliaBridge.runDelayed(plugin, runnable, delay);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        }
    }

    public static void runTaskTimer(Runnable runnable, long delay, long period) {
        if (isFolia) {
            FoliaBridge.runAtFixedRate(plugin, runnable, delay < 1 ? 1 : delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        }
    }

    public static void runTaskTimerAsync(Runnable runnable, long delay, long period) {
        if (isFolia) {
            FoliaBridge.runAtFixedRate(plugin, runnable, delay < 1 ? 1 : delay, period);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        }
    }

    public static <T> Future<T> callSyncMethod(Callable<T> task) {
        if (isFolia) {
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
        if (isFolia) {
            ClassManager.manager.getBugFinder().warn("Folia does not support cancelling tasks by ID");
        } else {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public static void cancelTasks(Plugin plugin) {
        if (isFolia) {
            FoliaBridge.cancelTasks(CrossScheduler.plugin, plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }
}
