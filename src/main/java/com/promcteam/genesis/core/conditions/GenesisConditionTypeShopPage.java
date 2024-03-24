package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public class GenesisConditionTypeShopPage extends GenesisConditionTypeNumber {
    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        return super.meetsCondition(holder, shopItem, p, conditiontype, transformLine(holder, condition));
    }

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
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


    public String transformLine(GenesisShopHolder holder, String s) {
        s = s.replace("%maxpage%", String.valueOf(holder.getDisplayHighestPage()));
        s = s.replace("%maxshoppage%", String.valueOf(holder.getDisplayHighestPage()));
        return s;
    }
}
