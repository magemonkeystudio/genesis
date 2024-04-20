package studio.magemonkey.genesis.inbuiltaddons;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.inbuiltaddons.advancedshops.AdvancedShops;
import studio.magemonkey.genesis.inbuiltaddons.logictypes.LogicTypes;

public class InbuiltAddonLoader {

    public void load(Genesis plugin) {
        new AdvancedShops().enable(plugin);
        new LogicTypes().enable(plugin);
    }

}
