package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

public class GenesisSingleCondition implements GenesisCondition {

    private final GenesisConditionType type;
    private final String               conditiontype;
    private final String               condition;


    public GenesisSingleCondition(GenesisConditionType type, String conditiontype, String condition) {
        this.type = type;
        this.conditiontype = conditiontype;
        this.condition = condition;
    }

    public boolean meetsCondition(GenesisShopHolder holder, GenesisBuy buy, Player p) {
        return type.meetsCondition(holder, buy, p, conditiontype, condition);
    }

    public GenesisConditionType getType() {
        return type;
    }

    public String getConditionType() {
        return conditiontype;
    }

    public String getCondition() {
        return condition;
    }

}
