package org.black_ixx.bossshop.core.conditions;

import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public class BSConditionTypePlaceHolderAPIAddonInstalled extends BSConditionType{
    @Override
    public void enableType() {}

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition) {
        if (conditiontype.equalsIgnoreCase("papiaddon-installed")) {
            return PlaceholderAPI.isRegistered(condition);}
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"papiaddon-installed"};
    }

    @Override
    public String[] showStructure() {
        return new String[]{"papiaddon-installed:[string]"};
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }
}
