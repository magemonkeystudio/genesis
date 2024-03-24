package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GenesisConditionSet implements GenesisCondition {

    private final List<GenesisCondition> conditions;


    public GenesisConditionSet() {
        conditions = new ArrayList<>();
    }

    public GenesisConditionSet(List<GenesisCondition> conditions) {
        this.conditions = conditions;
    }


    public void addCondition(GenesisCondition c) {
        conditions.add(c);
    }

    @Override
    public boolean meetsCondition(GenesisShopHolder holder, GenesisBuy buy, Player p) {
        for (GenesisCondition c : conditions) {
            if (!c.meetsCondition(holder, buy, p)) {
                return false;
            }
        }
        return true;
    }


    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public List<GenesisCondition> getConditions() {
        return conditions;
    }


}
