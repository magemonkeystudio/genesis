package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public abstract class GenesisConditionTypeMatch extends GenesisConditionType {

    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        if (conditiontype.equalsIgnoreCase("match")) {
            return isCorrect(p, true, condition);
        }
        if (conditiontype.equalsIgnoreCase("dontmatch")) {
            return isCorrect(p, false, condition);
        }
        return false;
    }


    private boolean isCorrect(Player p, boolean hasToMatch, String condition) {
        for (String singleCondition : condition.split(",")) {
            if (matches(p, singleCondition) == hasToMatch) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String[] showStructure() {
        return new String[]{"match:[string]", "dontmatch:[string]"};
    }


    public abstract boolean matches(Player p, String singleCondition);

}
