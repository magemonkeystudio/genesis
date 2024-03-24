package com.promcteam.genesis.core.rewards;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class GenesisRewardTypeShop extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be the name of a shop (a single text line).");
        return false;
    }

    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        String shopName = (String) reward;

        if (shopName == null || shopName == "" || shopName.length() < 1) {
            p.closeInventory();
        } else {
            ClassManager.manager.getShops().openShop(p, shopName);
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        String shopName = (String) reward;
        if (shopName == null || shopName == "" || shopName.length() < 1) {
            return ClassManager.manager.getMessageHandler().get("Display.Close");
        }
        return ClassManager.manager.getMessageHandler().get("Display.Shop").replace("%shop%", shopName);
    }

    @Override
    public String[] createNames() {
        return new String[]{"shop"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean isActualReward() {
        return false;
    }

}
