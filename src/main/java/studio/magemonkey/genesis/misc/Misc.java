package studio.magemonkey.genesis.misc;

import studio.magemonkey.genesis.managers.misc.InputReader;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Misc {
    /**
     * Fix the lore from a StringList
     *
     * @param itemData the sting list
     * @return fixed lore list
     */
    public static List<String> fixLore(List<String> itemData) {
        Map<Integer, String> lore    = null;
        List<String>         newList = null;
        int                  highest = -1;

        for (String line : itemData) {
            if (line.toLowerCase().startsWith("lore")) {
                String[] parts = line.split(":", 2);
                String   start = parts[0];
                if (start.length() > "lore".length()) {

                    try {
                        int i = Integer.parseInt(start.replace("lore", "")) - 1;

                        if (lore == null) {
                            lore = new HashMap<>();
                            newList = new ArrayList<>();
                        }

                        lore.put(i, parts[1]);
                        highest = Math.max(highest, i);

                    } catch (NumberFormatException e) {
                        // Fail
                    }

                }
            }
        }

        if (newList != null) {
            for (String line : itemData) {
                if (!line.toLowerCase().startsWith("lore")) {
                    newList.add(line);
                }
            }
            for (int i = 0; i <= highest; i++) {
                String s = "lore:";
                if (lore.containsKey(i)) {
                    s += lore.get(i);
                }
                newList.add(s);
            }
        }


        if (newList != null) {
            return newList;
        }
        return itemData;
    }


    /**
     * Play a sound for a player
     *
     * @param p     the player to play the sound for
     * @param sound the sound to play
     */
    public static void playSound(Player p, String sound) {
        if (sound != null) {
            if (!sound.isEmpty()) {
                String[] parts = sound.split(":");
                String   s     = null;

                // If splitted into 3, its a normal bukkit sound, else its custom
                if (parts.length == 3) {
                    for (Sound e : Sound.values()) {
                        if (e.name().equalsIgnoreCase(parts[0]) || e.getKey().getKey().equalsIgnoreCase(parts[0])) {
                            s = e.getKey().getKey();
                            break;
                        }
                    }
                } else {
                    s = parts[0] + ":" + parts[1];
                }

                if (s != null) {
                    boolean isCustomSound = s.contains(":");
                    float   volume;
                    float   pitch         = 1;
                    // Parsing volume & pitch based on bukkit or custom sound
                    if (!isCustomSound) {
                        volume = (float) InputReader.getDouble(parts[1], 1);
                        if (parts.length >= 3) {
                            pitch = (float) InputReader.getDouble(parts[2], 1);
                        }
                    } else {
                        volume = (float) InputReader.getDouble(parts[2], 1);
                        if (parts.length >= 4) {
                            pitch = (float) InputReader.getDouble(parts[3], 1);
                        }
                    }
                    p.playSound(p.getLocation(), s, SoundCategory.NEUTRAL, volume, pitch);
                }
            }
        }
    }


    /**
     * Get the item in the player's main hand
     *
     * @param p player to get item from
     * @return item
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getItemInMainHand(Player p) {
        ItemStack item;
        try {
            item = p.getInventory().getItemInMainHand();
        } catch (NoSuchMethodError e) {
            item = p.getItemInHand();
        }
        return item;
    }

}
