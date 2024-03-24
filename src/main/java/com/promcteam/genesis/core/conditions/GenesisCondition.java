package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public interface GenesisCondition {

    boolean meetsCondition(GenesisShopHolder holder, GenesisBuy buy, Player p);

}
