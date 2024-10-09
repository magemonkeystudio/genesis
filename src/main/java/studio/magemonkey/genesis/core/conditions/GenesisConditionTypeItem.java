package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;
import studio.magemonkey.genesis.managers.misc.InputReader;

public class GenesisConditionTypeItem extends GenesisConditionTypeMatch {


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
