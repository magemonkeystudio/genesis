package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;

public abstract class GenesisConditionTypeMatch extends GenesisConditionType {

    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditionType,
                                  String condition) {
        if (conditionType.equalsIgnoreCase("match")) {
            return isCorrect(p, true, condition);
        }
        if (conditionType.equalsIgnoreCase("dontmatch")) {
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
