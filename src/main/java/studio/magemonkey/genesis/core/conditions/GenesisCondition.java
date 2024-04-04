package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public interface GenesisCondition {

    boolean meetsCondition(GenesisShopHolder holder, GenesisBuy buy, Player p);

}
