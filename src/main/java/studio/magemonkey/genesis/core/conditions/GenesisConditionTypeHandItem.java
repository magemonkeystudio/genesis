package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.misc.Misc;
import org.bukkit.entity.Player;

public class GenesisConditionTypeHandItem extends GenesisConditionTypeMatch {


    @Override
    public boolean matches(Player p, String singleCondition) {
        return Misc.getItemInMainHand(p).getType().equals(InputReader.readMaterial(singleCondition));
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"handitem", "itemhand", "mainitem", "iteminhand"};
    }


    @Override
    public void enableType() {
    }


}
