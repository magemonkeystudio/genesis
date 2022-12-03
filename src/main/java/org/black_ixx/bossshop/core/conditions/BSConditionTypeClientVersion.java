package org.black_ixx.bossshop.core.conditions;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.NetworkManager;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

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

    private static int getPlayerProtocolVersion(Player player) throws NoSuchFieldException, IllegalAccessException {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection connection = entityPlayer.playerConnection;
        NetworkManager networkManager = connection.networkManager;
        Field protocolVersionField = networkManager.getClass().getDeclaredField("protocolVersion");
        protocolVersionField.setAccessible(true);
        return protocolVersionField.getInt(networkManager);
    }
}
