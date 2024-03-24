package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;

public class GenesisConditionTypePoints extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        return ClassManager.manager.getPointsManager().getPoints(p);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"points"};
    }


    @Override
    public void enableType() {
        ClassManager.manager.getSettings().setPointsEnabled(true);
    }


}
