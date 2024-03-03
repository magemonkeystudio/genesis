package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSChoosePageLayoutEvent;
import org.black_ixx.bossshop.events.BSDisplayItemEvent;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class ShopCustomizer {
    /**
     * Create a new inventory
     *
     * @param shop          the shop to create from
     * @param items         the items in the shop
     * @param p             the player opening the shop
     * @param m             the class manager
     * @param page          the page of the shop
     * @param highestPage   the highest page for the shop
     * @param oldShopHolder the holder of the shop
     * @return inventory
     */
    public Inventory createInventory(BSShop shop,
                                     Set<BSBuy> items,
                                     Player p,
                                     ClassManager m,
                                     int page,
                                     int highestPage,
                                     BSShopHolder oldShopHolder) {
        BSShopHolder holder = new BSShopHolder(shop, oldShopHolder);
        holder.setPage(page);
        holder.setHighestPage(highestPage);
        Inventory inventory =
                Bukkit.createInventory(holder, shop.getInventorySize(), shop.getValidDisplayName(p, holder));

        return createInventory(shop, items, p, m, inventory, holder, page);
    }


    /**
     * Create a new inventory
     *
     * @param shop        the shop to create from
     * @param items       the items for the inventory
     * @param p           the player opening
     * @param m           the class manager
     * @param inventory   the inventory being opened
     * @param page        the page of the shop
     * @param highestPage the amount of pages for shop
     * @return inventory
     */
    public Inventory createInventory(BSShop shop,
                                     Set<BSBuy> items,
                                     Player p,
                                     ClassManager m,
                                     Inventory inventory,
                                     int page,
                                     int highestPage) {
        if (inventory.getHolder() instanceof BSShopHolder) {
            BSShopHolder holder = (BSShopHolder) inventory.getHolder();
            return createInventory(shop, items, p, m, inventory, holder, page);
        } else {
            return inventory;
        }
    }

    /**
     * Create a new inventory
     *
     * @param shop      the shop to open as
     * @param items     the items in the shop
     * @param p         the player opening
     * @param m         the class manager
     * @param inventory the inventory to open as
     * @param holder    the holder of the inventory
     * @param page      the page of the inventory
     * @return inventory
     */
    public Inventory createInventory(BSShop shop,
                                     Set<BSBuy> items,
                                     Player p,
                                     ClassManager m,
                                     Inventory inventory,
                                     BSShopHolder holder,
                                     int page) {
        holder.setPage(page);
        //Adding all possible items in a map
        HashMap<Integer, BSBuy> everything = new LinkedHashMap<>();

        PageLayoutHandler layout = ClassManager.manager.getPageLayoutHandler();

        //Possibility for other plugins to change the layout
        BSChoosePageLayoutEvent event = new BSChoosePageLayoutEvent(shop, shop.getShopName(), layout);
        Bukkit.getPluginManager().callEvent(event);
        layout = event.getLayout();

        int maxRows             = layout.getMaxRows(); //Default: 6
        int itemsPerPage        = layout.getReservedSlotsStart() - 1; //Default: 45
        int itemsPerPageOnePage = BSShop.ROW_ITEMS * maxRows; //Default: 56
        int pageSlotStart       = itemsPerPage * page;
        int pageSlotEnd         = itemsPerPage * (page + 1) - 1;

        // 1. Add items to (empty) everything HashMap to determine the locations
        for (BSBuy buy : items) {
            if (buy != null) {

                if (!showItem(shop, holder, buy, p, inventory, everything)) {
                    continue;
                }

                int slot = getSlot(inventory, everything, buy);
                everything.put(slot, buy);
            }

        }

        // 2. Calculate highest slot
        int highestSlot = 0;
        for (int slot : everything.keySet()) {
            highestSlot = Math.max(highestSlot, slot);
        }


        boolean full = false; // full other second page anyway

        // 3. Determine whether pagelayout is needed
        if (page == 0 && highestSlot < itemsPerPageOnePage && layout.showIfMultiplePagesOnly()) {
            pageSlotEnd = itemsPerPageOnePage;
        } else {
            full = true;
        }


        // 4. Adding all actual items only
        Map<Integer, BSBuy> locs = new LinkedHashMap<>();

        for (int slot : everything.keySet()) {
            if (slot < pageSlotStart || slot > pageSlotEnd) { // Do not show items of other pages
                continue;
            }

            BSBuy buy      = everything.get(slot);
            int   realSlot = slot - pageSlotStart;
            locs.put(realSlot, buy);
            addItem(inventory, buy.getItem(), realSlot, shop.isDisplaying(), p, buy, shop, holder);
        }

        int highestPage = highestSlot / itemsPerPage; //in case the layout is active
        holder.setItems(locs, page, full ? highestPage : 0);

        // Add items here because pages get updated at the end
        if (full) {
            for (BSBuy buy : layout.getItems()) {
                if (!showItem(shop, holder, buy, p, inventory, locs)) {
                    continue;
                }
                if (buy.getInventoryLocation() < 0 || buy.getInventoryLocation() >= itemsPerPageOnePage) {
                    ClassManager.manager.getBugFinder()
                            .warn("Unable to add pagelayout item '" + buy.getName()
                                    + "': Inventory location needs to be between 1 and " + itemsPerPageOnePage
                                    + " with 'MaxRows' set to '" + layout.getMaxRows() + "'.");
                    continue;
                }
                locs.put(buy.getInventoryLocation(), buy);

                buy.updateShop(shop, buy.getItem(), m, false);
                addItem(inventory,
                        buy.getItem(),
                        buy.getInventoryLocation(),
                        shop.isDisplaying(),
                        p,
                        buy,
                        shop,
                        holder);
            }
        }


        // Deleting old items from inventory (there is no inventory.clear at start because that would make the inventory blink on every refresh)
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!locs.containsKey(i)) {
                inventory.setItem(i, null);
            }
        }

        return inventory;
    }


    /**
     * Add an item to an inventory
     *
     * @param inventory  the inventory to add to
     * @param item       the item to add
     * @param slot       the slot of the item
     * @param displaying display the item or not
     * @param p          the player opening inventory
     * @param buy        shop item
     * @param shop       the shop added to
     * @param holder     the holder of the shop
     */
    private void addItem(Inventory inventory,
                         ItemStack item,
                         int slot,
                         boolean displaying,
                         Player p,
                         BSBuy buy,
                         BSShop shop,
                         BSShopHolder holder) {
        if (displaying && p != null) {
            if (item.hasItemMeta()) {
                if (!buy.isFixItem()) {
                    item = ClassManager.manager.getItemStackTranslator()
                            .translateItemStack(buy, shop, holder, item.clone(), p, true);
                }
            }
        }

        ItemStack current = inventory.getItem(slot);
        if (current != null) {
            if (current.getType() == Material.PLAYER_HEAD) {
                if (ClassManager.manager.getItemStackChecker().isEqualShopItemAdvanced(item, current, true, p)) {
                    return;
                }
            }
        }
        inventory.setItem(slot, item);
    }


    /**
     * Show the item in an inventory
     *
     * @param shop       the shop to get from
     * @param holder     the holder of the shop
     * @param buy        the shop item
     * @param p          the player opening the shop
     * @param inventory  the inventory of the shop
     * @param filledLocs the filled locations of the shop
     * @return show or not
     */
    public boolean showItem(BSShop shop,
                            BSShopHolder holder,
                            BSBuy buy,
                            Player p,
                            Inventory inventory,
                            Map<Integer, BSBuy> filledLocs) {
        if (filledLocs.containsKey(buy.getInventoryLocation())) {
            if (filledLocs.get(buy.getInventoryLocation()) != buy) {
                return false; //Different items do not have space here
            }
            //Same item checking for being added again. Probably for an item refresh? Allow!
        }

        if (p == null) {
            return true;
        }

        if (ClassManager.manager.getSettings()
                .getPropertyBoolean(Settings.HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR, buy) & !buy.hasPermission(p,
                false,
                null)) {
            return false;
        }

        if (!buy.meetsCondition(holder, p)) {
            return false;
        }

        BSDisplayItemEvent event = new BSDisplayItemEvent(p, shop, buy, inventory);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();

    }

    /**
     * Get a slot position
     *
     * @param inventory  the inventory of the shop
     * @param everything all items loaded in the inventory
     * @param buy        the item being checked
     * @return slot of item
     */
    public int getSlot(Inventory inventory, Map<Integer, BSBuy> everything, BSBuy buy) {
        if (buy.getInventoryLocation() == -1) {
            return getFirstFreeSlot(inventory, everything);
        }
        return buy.getInventoryLocation();
    }

    /**
     * Get the first free slot in an inventory
     *
     * @param inventory  the inventory to check
     * @param everything the items loaded in the inventory
     * @return first free slot
     */
    public int getFirstFreeSlot(Inventory inventory, Map<Integer, BSBuy> everything) {
        for (int i = 0; i < 5000; i++) {
            if (!everything.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }


}
