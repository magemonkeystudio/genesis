package com.promcteam.genesis.core.prices;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.rewards.GenesisRewardType;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class GenesisPriceTypeNumber extends GenesisPriceType {


    public abstract boolean isIntegerValue();

    @Override
    public boolean isPlayerDependend(GenesisBuy buy, ClickType clickType) {
        return super.isPlayerDependend(buy, clickType) || (buy.getRewardType(clickType) == GenesisRewardType.ItemAll
                && ClassManager.manager.getSettings().getItemAllShowFinalReward());
    }

    @Override
    public boolean supportsMultipliers() {
        return true;
    }

    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        return takePrice(p, buy, price, clickType, 1);
    }

    @Override
    public boolean hasPrice(Player p, GenesisBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        return hasPrice(p, buy, price, clickType, 1, messageOnFailure);
    }


    public abstract String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType, int multiplier);

    public abstract boolean hasPrice(Player p,
                                     GenesisBuy buy,
                                     Object price,
                                     ClickType clickType,
                                     int multiplier,
                                     boolean messageOnFailure);

    public abstract String getDisplayBalance(Player p, GenesisBuy buy, Object price, ClickType clickType);


}
