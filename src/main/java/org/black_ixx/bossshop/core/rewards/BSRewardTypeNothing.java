package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSRewardTypeNothing extends BSRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return null;
    }

    public boolean validityCheck(String itemName, Object o) { // Here can't be any mistakes
        return true;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Nothing");
    }

    @Override
    public String[] createNames() {
        return new String[]{"nothing"};
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
