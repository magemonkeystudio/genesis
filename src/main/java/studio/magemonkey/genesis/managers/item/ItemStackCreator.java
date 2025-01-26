package studio.magemonkey.genesis.managers.item;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.config.GenesisConfigShop;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.managers.misc.StringManipulationLib;
import studio.magemonkey.genesis.misc.Misc;

import java.util.ArrayList;
import java.util.List;


public class ItemStackCreator {


    public ItemStack createItemStack(List<String> itemData,
                                     GenesisBuy buy,
                                     GenesisShop shop,
                                     boolean finalVersion) { // This allows to work with %rewarditem_<id>% and %priceitem_<id>%

        if (shop instanceof GenesisConfigShop) {
            GenesisConfigShop cShop = (GenesisConfigShop) shop;

            List<String> newList = null;
            for (String line : itemData) {

                String rewardLine = StringManipulationLib.figureOutVariable(line, "rewarditem", 0);
                if (rewardLine != null) {
                    if (newList == null) {
                        newList = cloneList(itemData);
                    }
                    int i = InputReader.getInt(rewardLine, -1) - 1;
                    newList = transform(line, i, newList, buy, cShop, "Reward");
                }

                String priceLine = StringManipulationLib.figureOutVariable(line, "priceitem", 0);
                if (priceLine != null) {
                    if (newList == null) {
                        newList = cloneList(itemData);
                    }
                    int i = InputReader.getInt(rewardLine, -1) - 1;
                    newList = transform(line, i, newList, buy, cShop, "Price");
                }

                if (newList != null) {
                    return createItemStack(newList, finalVersion);
                }

            }


        }

        return createItemStack(itemData, finalVersion);
    }

    private List<String> transform(String line,
                                   int index,
                                   List<String> newList,
                                   GenesisBuy buy,
                                   GenesisConfigShop shop,
                                   String path) {
        if (index != -1) {
            newList.remove(line);

            List<List<String>> list = InputReader.readStringListList(buy.getConfigurationSection(shop).get(path));
            if (list != null) {
                if (list.size() > index) {
                    for (String entry : list.get(index)) {
                        newList.add(entry);
                    }
                } else {
                    ClassManager.manager.getBugFinder()
                            .warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName()
                                    + "' in shop '" + shop.getShopName() + "' but your " + path + " does not contain a "
                                    + index + ". item!");
                }
            } else {
                ClassManager.manager.getBugFinder()
                        .warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName()
                                + "' in shop '" + shop.getShopName() + "' but your " + path
                                + " is not an item list!");
            }
        }
        return newList;
    }

    private List<String> cloneList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        List<String> clone = new ArrayList<>(list);
        return clone;
    }

    public ItemStack createItemStack(List<String> itemData, boolean finalVersion) {
        ItemStack i = new ItemStack(Material.STONE);

        itemData = Misc.fixLore(itemData);

        i = ItemDataPart.transformItem(i, itemData);
        ClassManager.manager.getItemStackTranslator().translateItemStack(null, null, null, i, null, finalVersion);
        return i;
    }

    /**
     * Gives the selected item to the player.
     *
     * @param p         Player to give the item to.
     * @param buy       Shopitem linked to the item.
     * @param i         Item to add to the player.
     * @param cloneItem Whether the item selected can be modified or if a clone of the selected item should be used instead, keeping the original item unchanged.
     * @apiNote If cloneItem = false the item is modified (placeholders adapted to player and amount changed).
     */
    public void giveItem(Player p, GenesisBuy buy, ItemStack i, boolean cloneItem) {
        giveItem(p, buy, i, i.getAmount(), cloneItem);
    }


    /**
     * Gives the selected item to the player.
     *
     * @param p         Player to give the item to.
     * @param buy       Shopitem linked to the item.
     * @param i         Item to add to the player.
     * @param amount    Amount of the item to add to the player.
     * @param cloneItem Whether the item selected can be modified or if a clone of the selected item should be used instead, keeping the original item unchanged.
     * @apiNote If cloneItem = false the item is modified (placeholders adapted to player and amount changed).
     */
    public void giveItem(Player p, GenesisBuy buy, ItemStack i, int amount, boolean cloneItem) {
        if (cloneItem) {
            i = i.clone();
        }

        int toGive    = amount;
        int stackSize = ClassManager.manager.getItemStackChecker().getMaxStackSize(i);

        //First of all translate item
        i = ClassManager.manager.getItemStackTranslator()
                .translateItemStack(buy, null, null, (cloneItem ? i.clone() : i), p, true);

        while (toGive > 0) {
            i.setAmount(Math.min(stackSize, toGive));
            giveItem(p, i.clone(), stackSize);
            toGive -= i.getAmount();
        }
    }


    /**
     * Gives an unmodified version of the item to the player.
     *
     * @param p         Player to give the item to.
     * @param i         ItemStack to give to the player.
     * @param stackSize Maximum stack size determined using {@link ItemStackChecker#getMaxStackSize(ItemStack)}.
     * @pre The item needs to be translated and adapted to the player already.
     */
    private void giveItem(Player p, ItemStack i, int stackSize) {
        if (i.getAmount() > stackSize) {
            throw new RuntimeException(
                    "Can not give an itemstack with a higher amount than the maximum stack size at once to a player.");
        }
        int free = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, i);

        if (free < i.getAmount()) { //Not enough space?
            dropItem(p, i, stackSize);
        } else {
            p.getInventory().addItem(i);
        }
    }

    private void dropItem(Player p, ItemStack i, int stackSize) {
        int toTake = i.getAmount();
        int amount;
        i = i.clone();

        while (toTake > 0) {
            amount = Math.min(toTake, stackSize);
            i.setAmount(amount);
            p.getWorld().dropItem(p.getLocation(), i);// Drop Item!
            toTake -= amount;
        }
    }

}
