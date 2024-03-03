package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSCustomLink;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSRewardTypeCustom extends BSRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return o; // Because nothing is known about the custom reward type
    }

    public boolean validityCheck(String itemName, Object o) { // Because nothing is known about the custom reward type
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
        BSCustomLink link = (BSCustomLink) reward;
        link.doAction(p);
    }

    @Override
    public String[] createNames() {
        return new String[]{"custom"};
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Custom");
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }


}
