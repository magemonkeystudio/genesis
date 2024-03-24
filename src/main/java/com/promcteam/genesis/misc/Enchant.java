package com.promcteam.genesis.misc;

import org.bukkit.enchantments.Enchantment;

public class Enchant {

    private final Enchantment type;
    private final int         lvl;

    public Enchant(Enchantment type, int lvl) {
        this.type = type;
        this.lvl = lvl;
    }

    public Enchantment getType() {
        return type;
    }

    public int getLevel() {
        return lvl;
    }

}
