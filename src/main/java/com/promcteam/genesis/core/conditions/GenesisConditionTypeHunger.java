package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public class GenesisConditionTypeHunger extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        return p.getFoodLevel();
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"hunger", "foodlevel", "food"};
    }


    @Override
    public void enableType() {
    }


}
