package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class GenesisConditionTypeRealYear extends GenesisConditionTypeNumber {

    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"realyear", "year"};
    }


    @Override
    public void enableType() {
    }


}
