package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BSConditionTypeClientVersion extends BSConditionTypeNumber{
    @Override
    public void enableType() {}

    @Override
    public String[] createNames() {
        return new String[]{"client", "version"};
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) throws NoSuchFieldException, IllegalAccessException {
        return getPlayerProtocolVersion(p);
    }

    private static int getPlayerProtocolVersion(Player player) {
        String version = Bukkit.getServer().getBukkitVersion().split("-")[0];
        String[] vers = version.split("\\.");
        Class c;
        try { c = Class.forName("net.minecraft.server.v"+vers[0]+"_"+vers[1]+"_R"+vers[2]+".entity.CraftPlayer");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return 0;
        }
        //I can't use NMS...
        return 0;
    }
}
