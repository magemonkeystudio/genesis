package org.black_ixx.bossshop.managers.item;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemStackChecker {

    public final static int INVENTORY_SLOT_START = 0;
    public final static int INVENTORY_SLOT_END   = 35;

    private List<String>   toolSuffixes;
    private List<Material> toolsComplete;

    public ItemStackChecker() {
        toolSuffixes = new ArrayList<>();
        toolSuffixes.add("AXE");
        toolSuffixes.add("HOE");
        toolSuffixes.add("PICKAXE");
        toolSuffixes.add("SPADE");
        toolSuffixes.add("SWORD");

        toolSuffixes.add("BOOTS");
        toolSuffixes.add("CHESTPLATE");
        toolSuffixes.add("HELMET");
        toolSuffixes.add("LEGGINGS");

        toolSuffixes.add("BARDING");

        toolsComplete = new ArrayList<>();
        toolsComplete.add(Material.BOW);
        toolsComplete.add(Material.FLINT_AND_STEEL);
        toolsComplete.add(Material.SHEARS);
        toolsComplete.add(Material.FISHING_ROD);
    }


    public boolean inventoryContainsItem(Player p, ItemStack i, BSBuy buy) {
        if (getAmountOfSameItems(p, i, buy) >= i.getAmount()) {
            return true;
        }
        return false;
    }

    public void takeItem(ItemStack shopItem, Player p, BSBuy buy) {
        int a    = 0;
        int slot = -1;

        for (ItemStack playerItem : p.getInventory().getContents()) {
            slot++;
            if (playerItem != null && playerItem.getType() != Material.AIR) {
                if (canSell(p, playerItem, shopItem, slot, buy)) {

                    playerItem = playerItem.clone(); // New
                    playerItem.setAmount(Math.min(playerItem.getAmount(), shopItem.getAmount() - a)); //New

                    a += playerItem.getAmount();
                    //remove(p, playerItem); //Old
                    p.getInventory().removeItem(playerItem); //New

                    if (a >= shopItem.getAmount()) { //Reached amount. Can stop!
                        break;
                    }
                }
            }
        }

        a -= shopItem.getAmount();
        if (a > 0) {
            ClassManager.manager.getBugFinder()
                    .warn("Player " + p.getName() + " lost " + a + " items of type " + shopItem.getType().name()
                            + ". How would that happen?");
        }
        return;
    }

    public int getAmountOfSameItems(Player p, ItemStack shopItem, BSBuy buy) {
        int a    = 0;
        int slot = -1;

        for (ItemStack playerItem : p.getInventory().getContents()) {
            slot++;
            if (playerItem != null) {
                if (canSell(p, playerItem, shopItem, slot, buy)) {
                    a += playerItem.getAmount();
                }
            }
        }
        return a;
    }

    public boolean hasFreeSpace(Player p, ItemStack item) {
        return getAmountOfFreeSpace(p, item) >= item.getAmount();
    }

    public int getAmountOfFreeSpace(Player p, ItemStack item) {
        int freeSlots = 0;
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem == null || slotItem.getType() == Material.AIR) {
                freeSlots += item.getMaxStackSize();
            } else {
                if (slotItem.isSimilar(item)) {
                    freeSlots += Math.max(0, slotItem.getMaxStackSize() - slotItem.getAmount());
                }
            }
        }
        return freeSlots;
    }

    public boolean hasFreeSpace(Player p, List<ItemStack> items) {
        HashMap<ItemStack, Integer> amounts = new HashMap<>();

        //Make amounts ready
        for (ItemStack item : items) {
            int amount = item.getAmount();
            if (amounts.containsKey(item)) {
                amount += amounts.get(item);
            }
            amounts.put(item, amount);
        }

        //Decrease amounts using already filled inventory slots
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem != null) {

                for (ItemStack item : amounts.keySet()) {
                    if (slotItem.isSimilar(item)) {
                        int spaceLeft  = slotItem.getMaxStackSize() - slotItem.getAmount();
                        int amountLeft = Math.max(0, amounts.get(item) - spaceLeft);
                        if (amountLeft == 0) {
                            amounts.remove(item);
                        } else {
                            amounts.put(item, amountLeft);
                        }
                        break; //break for loop because slot is already used
                    }
                }

            }
        }

        //Decrease amounts using empty inventory slots
        for (int slot = INVENTORY_SLOT_START; slot <= INVENTORY_SLOT_END; slot++) {
            ItemStack slotItem = p.getInventory().getItem(slot);
            if (slotItem == null) {

                for (ItemStack item : amounts.keySet()) {
                    int amountLeft = amounts.get(item);
                    if (amountLeft > 0) {
                        amountLeft = Math.max(0, amountLeft - item.getMaxStackSize());
                        if (amountLeft == 0) {
                            amounts.remove(item);
                        } else {
                            amounts.put(item, amountLeft);
                        }
                        break; //break for loop because slot is already used
                    }
                }

            }
        }

        return amounts.isEmpty();
    }


    private boolean canSell(Player p, ItemStack playerItem, ItemStack shopItem, int slot, BSBuy buy) {
        if (slot < INVENTORY_SLOT_START || slot > INVENTORY_SLOT_END) { //Has to be inside normal inventory
            return false;
        }

        ItemDataPart exceptionDurability =
                isTool(playerItem) && ClassManager.manager.getSettings().getAllowSellingDamagedItems()
                        ? ItemDataPart.DURABILITY : null;
        ItemDataPart[] exceptions = new ItemDataPart[]{exceptionDurability};

        return ItemDataPart.isSimilar(shopItem, playerItem, exceptions, buy, false, p);
    }


    public boolean isEqualShopItemAdvanced(ItemStack a, ItemStack b, boolean compareText, Player p) {
        return isEqualShopItemAdvanced(a, b, compareText, true, true, p);
    }

    public boolean isEqualShopItemAdvanced(ItemStack a,
                                           ItemStack b,
                                           boolean compareText,
                                           boolean compareAmount,
                                           boolean compareItemMetaExistence,
                                           Player p) {
        if (a != null && b != null) {
            if (compareItemMetaExistence && a.hasItemMeta() != b.hasItemMeta()) {
                return false;
            }

            ItemDataPart[] exceptions;
            ItemDataPart exceptionDurability =
                    isTool(a) && ClassManager.manager.getSettings().getAllowSellingDamagedItems()
                            ? ItemDataPart.DURABILITY : null;
            if (!compareText) {
                exceptions =
                        new ItemDataPart[]{exceptionDurability, ItemDataPart.NAME, ItemDataPart.LORE, ItemDataPart.PLAYERHEAD};
            } else {
                exceptions = new ItemDataPart[]{exceptionDurability};
            }


            return ItemDataPart.isSimilar(a, b, exceptions, null, compareAmount, p);
        }
        return false;
    }

    public boolean isTool(ItemStack i) {
        if (toolsComplete.contains(i.getType())) {
            return true;
        }
        for (String toolSuffix : toolSuffixes) {
            if (i.getType().name().endsWith(toolSuffix)) {
                return true;
            }
        }
        return false;
    }

    public int getMaxStackSize(ItemStack i) {
        if (ClassManager.manager.getSettings().getCheckStackSize()) {
            return i.getMaxStackSize();
        }
        return 64;
    }

}
