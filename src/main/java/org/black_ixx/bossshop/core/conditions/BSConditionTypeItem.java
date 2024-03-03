package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;

public class BSConditionTypeItem extends BSConditionTypeMatch {


    @Override
    public boolean matches(Player p, String singleCondition) {
        return p.getInventory().contains(InputReader.readMaterial(singleCondition));
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"item", "inventoryitem", "hasitem", "material"};
    }


    @Override
    public void enableType() {
    }


}
