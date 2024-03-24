package com.promcteam.genesis.managers.external;

import com.promcteam.genesis.Genesis;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIHandler {

    public PlaceholderAPIHandler() {
        Genesis.log("Hooked into PlaceholderAPI.");
    }

    public String transformString(String s, Player p) {
        if (containsPlaceholder(s)) {
            s = PlaceholderAPI.setPlaceholders(p, s);
        }
        return s;
    }

    public boolean containsPlaceholder(String s) {
        return PlaceholderAPI.containsPlaceholders(s);
    }


}
