package studio.magemonkey.genesis.managers.misc;

import com.google.gson.JsonParseException;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.misc.Enchant;
import studio.magemonkey.genesis.misc.MathTools;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InputReader {


    /**
     * Get a string from an object
     *
     * @param o         object to check
     * @param lowercase lowercase or not
     * @return string
     */
    public static String readString(Object o, boolean lowercase) {
        if (o == null) {
            return null;
        }
        String s = String.valueOf(o);
        if (s != null && lowercase) {
            s = s.toLowerCase();
        }
        return s;
    }

    /**
     * Get a string list from an object
     *
     * @param o object to check
     * @return string list
     */
    @SuppressWarnings("unchecked")
    public static List<String> readStringList(Object o) {
        if (o instanceof List<?>) {
            return (List<String>) o;
        }
        if (o instanceof String) {
            ArrayList<String> list = new ArrayList<>();
            list.add((String) o);
            return list;
        }
        return null;
    }

    /**
     * Get a list of string list from an object
     *
     * @param o object to check
     * @return list of string list
     */
    @SuppressWarnings("unchecked")
    public static List<List<String>> readStringListList(Object o) {
        if (!(o instanceof List<?>)) {
            return null;
        }
        List<?> list = (List<?>) o;
        if (list.isEmpty()) {
            return null;
        }
        if (list.get(0) instanceof List<?>) { //Everything perfect: Having a list inside a list
            return (List<List<String>>) o;
        } else { //Having one list only
            ArrayList<List<String>> main = new ArrayList<>();
            main.add((List<String>) o);
            return main;
        }
    }

    /**
     * Get list of ItemStacks from object
     *
     * @param o            object to check
     * @param finalVersion final version or not
     * @return list of ItemStacks
     */
    public static List<ItemStack> readItemList(Object o, boolean finalVersion) {
        List<List<String>> list = readStringListList(o);
        if (list != null) {
            List<ItemStack> items = new ArrayList<>();
            for (List<String> s : list) {
                items.add(ClassManager.manager.getItemStackCreator().createItemStack(s, finalVersion));
            }
            return items;
        }
        return null;
    }

    /**
     * Get itemstack from object
     *
     * @param o            object to check
     * @param finalVersion final version or not
     * @return itemstack
     */
    public static ItemStack readItem(Object o, boolean finalVersion) {
        List<ItemStack> list = readItemList(o, finalVersion);
        if (list != null & !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Get enchant from an object
     *
     * @param o object to check
     * @return enchant
     */
    public static Enchant readEnchant(Object o) {
        String s = readString(o, false);
        if (s != null) {
            String[] parts = s.split("#", 2);
            if (parts.length == 2) {
                String      pName  = parts[0].trim();
                String      pLevel = parts[1].trim();
                int         lvl;
                Enchantment e;

                try {
                    lvl = Integer.parseInt(pLevel);
                } catch (NumberFormatException ex) {
                    ClassManager.manager.getBugFinder()
                            .severe("Mistake in Config: '" + pLevel + "' is not a valid enchantment level.");
                    return null;
                }
                e = readEnchantment(pName);

				/* Enchantment seems to somehow not be detected.
				if(e == null && Bukkit.getPluginManager().isPluginEnabled("TokenEnchant")){
					TokenEnchantAPI te = TokenEnchantAPI.getInstance();
					pName = pName.substring(0,1).toUpperCase()+pName.substring(1).toLowerCase();
					System.out.println("Enchantment for " + pName+": " + te.getEnchant(pName));
					System.out.println("PE for " + pName+": " + te.getPotion(pName));
					e = te.getEnchant(pName);
				}*/

                if (e == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Mistake in Config: '" + pName + "' is not a valid enchantment name/id.");
                    return null;
                }

                return new Enchant(e, lvl);

            }
        }
        return null;
    }


    /**
     * Get enchant by name
     *
     * @param name name of enchant
     * @return enchant
     */
    public static Enchantment readEnchantment(String name) {
        if (name != null) {
            return EnchantmentWrapper.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
        }
        return null;
    }


    /**
     * Get boolean from string
     *
     * @param s   string to get from
     * @param def default value
     * @return boolean
     */
    public static boolean getBoolean(String s, boolean def) {
        if (s != null) {
            if (s.equalsIgnoreCase(Boolean.TRUE.toString()) || s.equalsIgnoreCase("yes")) {
                return true;
            }
            if (s.equalsIgnoreCase(Boolean.FALSE.toString()) || s.equalsIgnoreCase("no")) {
                return false;
            }
        }
        return def;
    }

    /**
     * Get a double from an object
     *
     * @param o         objecct to get from
     * @param exception exception
     * @return double
     */
    public static double getDouble(Object o, double exception) {
        if (o instanceof String) {
            String s = (String) o;
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return MathTools.calculate(s, exception);
            }
        }
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            return (Long) o;
        }
        return exception;
    }

    /**
     * Get an int from an object
     *
     * @param o         object to get from
     * @param exception exception
     * @return int
     */
    public static int getInt(Object o, int exception) {
        if (o instanceof String) {
            String s = (String) o;
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) { //String does not represent an integer? Maybe a double value!
                return (int) getDouble(s, exception);
            }
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Double) {
            double d = (Double) o;
            return (int) d;
        }
        return exception;
    }

    /**
     * Get timed commands from an object
     *
     * @param o object to check
     * @return timed commands
     */
    public static HashMap<Integer, String> readTimedCommands(Object o) {
        List<String> list = readStringList(o);
        if (list != null) {
            HashMap<Integer, String> cmds = new HashMap<Integer, String>();
            for (String s : list) {
                try {
                    String[] parts = s.split(":", 2);
                    String   a1    = parts[0].trim();
                    int      i     = Integer.parseInt(a1);
                    String   cmd   = parts[1].trim();
                    cmds.put(i, cmd);
                } catch (Exception e) {
                    return null;
                }
            }
            return cmds;
        }
        return null;
    }


    /**
     * Read material from string
     *
     * @param s string to check
     * @return material
     */
    public static Material readMaterial(String s) {
        Material m = Material.matchMaterial(s, false);
        if (m == null) {
            m = Material.matchMaterial(s, true);
        }
        return m;
    }

    /**
     * Read entity type from string
     *
     * @param s string to check
     * @return entity type
     */
    public static EntityType readEntityType(String s) {
        for (EntityType e : EntityType.values()) {
            if (e.name().replace("_", "").equalsIgnoreCase(s.replace("_", ""))) {
                return e;
            }
        }
        return null;
    }

    /**
     * Reads a ChatComponent from its raw json string.
     *
     * @param rawJson JSON String containing the chat components,
     *                may be a single component or an array of components
     * @return a single BaseComponent. If the provided JSON String is an array,
     * a single {@link TextComponent} will be returned,
     * with the components in the String attached as extras.
     * Null if a {@link JsonParseException} is caught in the process.
     */
    @Nullable
    public static BaseComponent readChatComponent(String rawJson) {
        try {
            BaseComponent[] array = ComponentSerializer.parse(rawJson);
            if (array.length == 0) {
                return new TextComponent();
            } else if (array.length == 1) {
                return array[0];
            } else {
                BaseComponent component = new TextComponent();
                for (BaseComponent baseComponent : array) {
                    component.addExtra(baseComponent);
                }
                return component;
            }
        } catch (JsonParseException e) {
            return null;
        }
    }

}
