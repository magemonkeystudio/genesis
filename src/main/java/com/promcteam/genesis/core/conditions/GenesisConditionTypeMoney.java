package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;

public class GenesisConditionTypeMoney extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        if (ClassManager.manager.getVaultHandler() == null) {
            return 0;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
            return 0;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            return ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
        }
        return 0;
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"money"};
    }


    @Override
    public void enableType() {
        ClassManager.manager.getSettings().setVaultEnabled(true);
        ClassManager.manager.getSettings().setMoneyEnabled(true);
    }


}
