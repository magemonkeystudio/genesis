package studio.magemonkey.genesis.inbuiltaddons.advancedshops;

import org.bukkit.Bukkit;
import studio.magemonkey.genesis.Genesis;

public class AdvancedShops {

    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new ShopItemCreationListener(), plugin);
    }

}
