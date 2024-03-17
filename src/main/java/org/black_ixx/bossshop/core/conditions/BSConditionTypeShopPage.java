package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public class BSConditionTypeShopPage extends BSConditionTypeNumber {
    @Override
    public boolean meetsCondition(BSShopHolder holder,
                                  BSBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        return super.meetsCondition(holder, shopItem, p, conditiontype, transformLine(holder, condition));
    }

    @Override
    public double getNumber(BSBuy shopItem, BSShopHolder holder, Player p) {
        return holder.getDisplayPage();
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"shoppage", "page"};
    }


    @Override
    public void enableType() {
    }


    public String transformLine(BSShopHolder holder, String s) {
        s = s.replace("%maxpage%", String.valueOf(holder.getDisplayHighestPage()));
        s = s.replace("%maxshoppage%", String.valueOf(holder.getDisplayHighestPage()));
        return s;
    }
}
