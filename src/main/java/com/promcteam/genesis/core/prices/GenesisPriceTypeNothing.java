package com.promcteam.genesis.core.prices;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GenesisPriceTypeNothing extends GenesisPriceType {


    public Object createObject(Object o, boolean forceFinalState) {
        return null;
    }

    public boolean validityCheck(String itemName, Object o) {
        return true;
    }

    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p, GenesisBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        return true;
    }

    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        return "";
    }

    @Override
    public String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Nothing");
    }


    @Override
    public String[] createNames() {
        return new String[]{"nothing", "free"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
