package com.promcteam.genesis.inbuiltaddons;

import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.inbuiltaddons.advancedshops.AdvancedShops;
import com.promcteam.genesis.inbuiltaddons.logictypes.LogicTypes;

public class InbuiltAddonLoader {

    public void load(Genesis plugin) {
        new AdvancedShops().enable(plugin);
        new LogicTypes().enable(plugin);
    }

}
