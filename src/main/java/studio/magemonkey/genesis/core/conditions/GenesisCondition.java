package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;

public interface GenesisCondition {

    boolean meetsCondition(GenesisShopHolder holder, GenesisBuy buy, Player p);

}
