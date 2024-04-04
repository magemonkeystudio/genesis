package studio.magemonkey.genesis.inbuiltaddons.advancedshops;

import studio.magemonkey.genesis.Genesis;
import org.bukkit.Bukkit;

public class AdvancedShops {

    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new ShopItemCreationListener(), plugin);
    }

}
