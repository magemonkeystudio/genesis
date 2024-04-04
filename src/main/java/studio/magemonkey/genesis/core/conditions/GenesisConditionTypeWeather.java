package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;

public class GenesisConditionTypeWeather extends GenesisConditionTypeMatch {


    @Override
    public boolean matches(Player p, String singleCondition) {
        if (singleCondition.equalsIgnoreCase("storm")) {
            return p.getWorld().hasStorm();
        } else if (singleCondition.equalsIgnoreCase("clear")) {
            return !p.getWorld().hasStorm();
        }
        return false;
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"weather"};
    }


    @Override
    public void enableType() {
    }


}
