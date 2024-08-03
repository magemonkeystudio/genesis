package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class GenesisConditionTypeRealMillisecond extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        return Calendar.getInstance().get(Calendar.MILLISECOND);
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"realmillisecond", "millisecond"};
    }


    @Override
    public void enableType() {
    }


}
