package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.Genesis;
import org.bukkit.Bukkit;

public class LogicTypes {


    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new TypeRegisterListener(), plugin);
    }

}
