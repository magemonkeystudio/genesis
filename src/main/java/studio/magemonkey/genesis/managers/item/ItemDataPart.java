package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.misc.VersionManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemDataPart {

    public static int PRIORITY_MOST_EARLY = 0;
    public static int PRIORITY_EARLY      = 10;
    public static int PRIORITY_NORMAL     = 50;
    public static int PRIORITY_LATE       = 80;

    public static ItemDataPart
            MATERIAL,
            AMOUNT,
            DURABILITY,
            NAME,
            LORE,
            ENCHANTMENT,
            PLAYERHEAD,
            CUSTOMSKULL,
            CUSTOMMODELDATA,
            MOBSPAWNER,
            MOBEGG,
            ITEMFLAGS,
            COLOR,
            POTIONEFFECT,
            POTION,
            UNBREAKING,
            BOOK,
            BOOKPAGE,
            BANNER,
            AXOLOTL,
            GLOWING,
            KNOWLEDGEBOOK,
            SUSPICIOUSSTEW,
            TROPICALFISH,
            NBTTAG;

    private static List<ItemDataPart> types;
    private final  String[]           names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();

        MATERIAL = registerType(new ItemDataPartMaterial());
        AMOUNT = registerType(new ItemDataPartAmount());
        DURABILITY = registerType(new ItemDataPartDurability());
        NAME = registerType(new ItemDataPartName());
        LORE = registerType(new ItemDataPartLore());
        ENCHANTMENT = registerType(new ItemDataPartEnchantment());
        PLAYERHEAD = registerType(new ItemDataPartPlayerhead());
        CUSTOMMODELDATA = registerType(new ItemDataPartCustomModelData());
        MOBSPAWNER = registerType(new ItemDataPartMobspawner());
        MOBEGG = registerType(new ItemDataPartMobEgg());
        CUSTOMSKULL = registerType(new ItemDataPartCustomSkull());
        ITEMFLAGS = registerType(new ItemDataPartItemflags());
        COLOR = registerType(new ItemDataPartColor());
        POTIONEFFECT = registerType(new ItemDataPartPotionEffect());
        POTION = registerType(new ItemDataPartPotion());
        UNBREAKING = registerType(new ItemDataPartUnbreaking());
        BANNER = registerType(new ItemDataPartBanner());
        BOOK = registerType(new ItemDataPartWrittenBookInformation());
        BOOKPAGE = registerType(new ItemDataPartWrittenBookPage());
        if (VersionManager.isAtLeast(17)) {
            AXOLOTL = registerType(new ItemDataPartAxolotl());
        }
        if (VersionManager.isAtLeast(14)) {
            SUSPICIOUSSTEW = registerType(new ItemDataPartSuspiciousStew());
            KNOWLEDGEBOOK = registerType(new ItemDataPartKnowledgeBook());
            TROPICALFISH = registerType(new ItemDataPartTropicalFish());
        }
        if (VersionManager.isAtLeast(8)) {
            NBTTAG = registerType(new ItemDataPartNBTTag());
        }
        GLOWING = registerType(new ItemDataPartGlowing());
    }

    public static ItemDataPart registerType(ItemDataPart type) {
        types.add(type);
        return type;
    }

    public static ItemDataPart detectTypeSpecial(String wholeLine) {
        if (wholeLine == null) {
            return null;
        }
        String[] parts = wholeLine.split(":", 2);
        String   name  = parts[0].trim();
        return detectType(name);
    }

    public static ItemDataPart detectType(String s) {
        for (ItemDataPart type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return null;
    }

    public static ItemStack transformItem(ItemStack item, List<String> itemdata) {
        itemdata.sort((s1, s2) -> {// TODO: test sorting out
            ItemDataPart type1 = detectTypeSpecial(s1);
            ItemDataPart type2 = detectTypeSpecial(s2);
            if (type1 != null && type2 != null) {
                return Integer.compare(type1.getPriority(), type2.getPriority());
            }
            return 0;
        });
        for (String line : itemdata) {
            item = transformItem(item, line);
        }
        return item;
    }

    public static ItemStack transformItem(ItemStack item, String line) {
        if (line == null) {
            return item;
        }
        String[] parts    = line.split(":", 2);
        String   name     = parts[0].trim();
        String   argument = null;
        if (parts.length == 2) {
            argument = parts[1].trim();
        }

        ItemDataPart part = detectType(name);
        if (part == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: Unable to read itemdata '" + name + ":" + argument);
            return item;
        }

        return part.transformItem(item, name, argument);
    }

    public static List<String> readItem(ItemStack item) {
        if (item == null) {
            return null;
        }
        List<String> output = new ArrayList<>();
        for (ItemDataPart part : types) {
            try {
                output = part.read(item, output);
            } catch (Exception | NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            }
        }
        return output;
    }

    public static boolean isSimilar(ItemStack shopItem,
                                    ItemStack playerItem,
                                    ItemDataPart[] exceptions,
                                    GenesisBuy buy,
                                    boolean compareAmount,
                                    Player p) {
        if (shopItem == null || playerItem == null) {
            return false;
        }
        for (ItemDataPart part : types) {
            if (isException(exceptions, part)) {
                continue;
            }
            if (!compareAmount && part == AMOUNT) {
                continue;
            }
            try {
                if (!part.isSimilar(shopItem, playerItem, buy, p)) {
                    return false;
                }
            } catch (Exception | NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            }
        }
        return true;
    }

    private static boolean isException(ItemDataPart[] exceptions, ItemDataPart part) {
        if (exceptions != null) {
            for (ItemDataPart exception : exceptions) {
                if (exception == part) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<ItemDataPart> values() {
        return types;
    }

    public boolean isType(String s) {
        if (names != null) {
            for (String name : names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        ItemDataPart.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }


    public ItemStack transformItem(ItemStack item, String usedName, String argument) { //Return true in case of success
        if (argument == null && needsArgument()) {
            return item;
        }

        if (removeSpaces() && argument != null) {
            argument = argument.replaceAll(" ", "");
        }

        try {
            return transform(item, usedName.toLowerCase(), argument);
        } catch (NoClassDefFoundError | NoSuchMethodError e) { //Seems like that ItemDataPart is not supported yet
            ClassManager.manager.getBugFinder()
                    .severe("Unable to work with itemdata '" + usedName.toLowerCase() + ":" + argument
                            + ". Seems like it is not supported by your server version yet.");
            return item;
        }
    }


    @Deprecated
    public abstract ItemStack transform(ItemStack item,
                                        String usedName,
                                        String argument); //Return true in case of success

    public abstract boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p);

    public abstract List<String> read(ItemStack i, List<String> output);

    public abstract int getPriority(); //Parts with a lower priority (like material) are triggered before parts with higher priority are

    public abstract boolean removeSpaces();

    public abstract String[] createNames();

    public boolean needsArgument() {
        return true; //Can be overriden
    }

}
