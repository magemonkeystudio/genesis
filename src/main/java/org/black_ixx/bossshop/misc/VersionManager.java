package org.black_ixx.bossshop.misc;

import org.bukkit.Bukkit;

public class VersionManager {
    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isAtLeast(int i) {
        String version  = Bukkit.getServer().getBukkitVersion().split("-")[0];
        int    version2 = Integer.parseInt(version.split("\\.")[1]);
        return version2 >= i;
    }

}
