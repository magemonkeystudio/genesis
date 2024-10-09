package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import org.bukkit.Bukkit;
import studio.magemonkey.genesis.Genesis;

public class LogicTypes {


    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new TypeRegisterListener(), plugin);
    }

}
