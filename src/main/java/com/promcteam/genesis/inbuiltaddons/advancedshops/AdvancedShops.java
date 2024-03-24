package com.promcteam.genesis.inbuiltaddons.advancedshops;

import com.promcteam.genesis.Genesis;
import org.bukkit.Bukkit;

public class AdvancedShops {

    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new ShopItemCreationListener(), plugin);
    }

}
