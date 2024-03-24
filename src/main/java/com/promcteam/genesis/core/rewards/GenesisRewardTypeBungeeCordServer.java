package com.promcteam.genesis.core.rewards;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class GenesisRewardTypeBungeeCordServer extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be the name of connected BungeeCordServer (a single text line).");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        String server = (String) reward;
        ClassManager.manager.getBungeeCordManager().sendToServer(server, p, ClassManager.manager.getPlugin());
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        String server = (String) reward;
        return ClassManager.manager.getMessageHandler().get("Display.BungeeCordServer").replace("%server%", server);
    }

    @Override
    public String[] createNames() {
        return new String[]{"bungeecordserver", "bungee"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
