package com.promcteam.genesis.inbuiltaddons.logictypes;

import com.promcteam.genesis.Genesis;
import org.bukkit.Bukkit;

public class LogicTypes {


    public void enable(Genesis plugin) {
        Bukkit.getPluginManager().registerEvents(new TypeRegisterListener(), plugin);
    }

}
