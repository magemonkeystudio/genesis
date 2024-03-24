package com.promcteam.genesis.core.conditions;


import org.bukkit.entity.Player;

public class GenesisConditionTypeWorld extends GenesisConditionTypeMatch {

    @Override
    public boolean matches(Player p, String singleCondition) {
        return p.getWorld().getName().equalsIgnoreCase(singleCondition);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"world", "worldname"};
    }


    @Override
    public void enableType() {
    }


}
