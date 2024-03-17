package org.black_ixx.bossshop.api;

import lombok.Getter;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.*;
import org.black_ixx.bossshop.core.conditions.BSConditionType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossShopAPI {


    private BossShop            plugin;
    @Getter
    private List<BossShopAddon> enabledAddons;


    public BossShopAPI(BossShop plugin) {
        this.plugin = plugin;
    }


    /**
     * Gets the name of the addon for BossShop
     *
     * @param addonname the name of the addon
     * @return addon
     */
    public BossShopAddon getAddon(String addonname) {
        if (enabledAddons != null) {
            for (BossShopAddon addon : enabledAddons) {
                if (addon.getAddonName().equalsIgnoreCase(addonname)) {
                    return addon;
                }
            }
        }
        return null;
    }

    /**
     * Check if a shop is valid
     *
     * @param v the inventory to check
     * @return valid shop or not
     */
    // For single shop
    public boolean isValidShop(InventoryView v) {
        if (v != null) {
            if (v.getTopInventory() != null) {
                return isValidShop(v.getTopInventory());
            }
        }
        return false;
    }

    /**
     * Check if a shop is valid
     *
     * @param i inventory
     * @return valid or not
     */
    public boolean isValidShop(Inventory i) {
        if (i == null) {
            return false;
        }
        return (i.getHolder() instanceof BSShopHolder);
    }

    /**
     * Get a BossShop Shop
     *
     * @param name the name of the shop
     * @return shop
     */
    public BSShop getShop(String name) {
        if (plugin.getClassManager() == null) {
            return null;
        }
        if (plugin.getClassManager().getShops() == null) {
            return null;
        }
        return plugin.getClassManager().getShops().getShop(name.toLowerCase());
    }

    /**
     * Open a shop for a player by the name of the shop
     *
     * @param p    the player to open the shop for
     * @param name the name of the shop
     */
    public void openShop(Player p, String name) {
        BSShop shop = getShop(name);
        if (shop == null) {
            BossShop.log("[API] Error: Tried to open Shop " + name + " but it was not found...");
            return;
        }
        openShop(p, shop);
    }

    /**
     * Opens a shop for a player by the shop instance
     *
     * @param p    the player to open for
     * @param shop the shop to open
     */
    public void openShop(Player p, BSShop shop) {
        plugin.getClassManager().getShops().openShop(p, shop);
    }

    /**
     * Updates the inventory for a player
     *
     * @param p player to update for
     */
    public void updateInventory(Player p) {
        updateInventory(p, false);
    }

    /**
     * Updates the inventory
     *
     * @param p                the player to update for
     * @param forceNewCreation should it be forced
     */
    public void updateInventory(Player p, boolean forceNewCreation) {
        if (isValidShop(p.getOpenInventory())) {
            BSShopHolder holder = (BSShopHolder) p.getOpenInventory().getTopInventory().getHolder();
            if (forceNewCreation) {
                holder.getShop().openInventory(p, holder.getPage(), false);
            } else {
                holder.getShop()
                        .updateInventory(p.getOpenInventory().getTopInventory(),
                                holder,
                                p,
                                ClassManager.manager,
                                holder.getPage(),
                                holder.getHighestPage(),
                                false);
            }
        }
    }


    /**
     * Get the managers for the shop
     *
     * @return managers
     */
    // Get managers
    public BSShops getShopHandler() {
        return plugin.getClassManager().getShops();
    }


    /**
     * Add a shop to the plugin
     *
     * @param shop the shop to add
     */
    // Modify Shop/Shops
    public void addShop(BSShop shop) {
        getShopHandler().addShop(shop);
    }

    /**
     * Create the next id of a shop
     *
     * @return the id
     */
    public int createNextShopId() {
        return getShopHandler().createId();
    }

    /**
     * Add a new item to a shop
     *
     * @param menuItem the item to add
     * @param shopItem shop item
     * @param shop     the shop to add to
     */
    public void addItemToShop(ItemStack menuItem, BSBuy shopItem, BSShop shop) {
        shop.addShopItem(shopItem, menuItem, ClassManager.manager);
    }

    /**
     * Add items to shop
     *
     * @param shop the shop to add to
     */
    public void finishedAddingItemsToShop(BSShop shop) {
        shop.finishedAddingItems();
    }


    /**
     * Register a condition type
     *
     * @param type the type of condition to register
     */
    // Register things
    public void registerConditionType(BSConditionType type) {
        type.register();
    }

    /**
     * Register a price type
     *
     * @param type the price type to register
     */
    public void registerPriceType(BSPriceType type) {
        type.register();
    }

    /**
     * Register a reward type
     *
     * @param type the reward type to register
     */
    public void registerRewardType(BSRewardType type) {
        type.register();
    }

    /**
     * Register an item data part
     *
     * @param part part to add
     */
    public void registerItemDataPart(ItemDataPart part) {
        part.register();
    }

    /**
     * Create a new item
     *
     * @param name       the name of the item
     * @param rewardType the reward type of the item
     * @param priceType  the price type of the item
     * @param reward     the reward of the item
     * @param price      the price of the item
     * @param msg        the message of the item
     * @param location   the location of the item
     * @param permission the permission of the item
     * @return created item
     */
    // Create things
    public BSBuy createBSBuy(String name,
                             BSRewardType rewardType,
                             BSPriceType priceType,
                             Object reward,
                             Object price,
                             String msg,
                             int location,
                             String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, name);
    }


    /**
     * Creates a custom item
     *
     * @param name       name of item
     * @param rewardType reward type
     * @param priceType  price type
     * @param reward     reward
     * @param price      price
     * @param msg        msg
     * @param location   location
     * @param permission permission
     * @return custom item
     */
    public BSBuy createBSBuyCustom(String name,
                                   BSRewardType rewardType,
                                   BSPriceType priceType,
                                   BSCustomLink reward,
                                   Object price,
                                   String msg,
                                   int location,
                                   String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, name);
    }

    /**
     * Create a buy item
     *
     * @param rewardType reward type
     * @param priceType  price type
     * @param reward     reward
     * @param price      price
     * @param msg        msg
     * @param location   location
     * @param permission permission
     * @return buy item
     */
    public BSBuy createBSBuy(BSRewardType rewardType,
                             BSPriceType priceType,
                             Object reward,
                             Object price,
                             String msg,
                             int location,
                             String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public BSBuy createBSBuyCustom(BSRewardType rewardType,
                                   BSPriceType priceType,
                                   BSCustomLink reward,
                                   Object price,
                                   String msg,
                                   int location,
                                   String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public BSCustomLink createBSCustomLink(BSCustomActions actions, int actionId) {
        return new BSCustomLink(actionId, actions);
    }


    /**
     * Get all items in all shops
     *
     * @return list of all items
     */
    // Get shop items
    public HashMap<BSShop, List<BSBuy>> getAllShopItems() {
        HashMap<BSShop, List<BSBuy>> all = new HashMap<>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null) {
                continue;
            }
            List<BSBuy> items = new ArrayList<>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                items.add(buy);
            }
            all.put(shop, items);
        }

        return all;
    }

    /**
     * Get all items from config
     *
     * @param configOption
     * @return
     */
    public HashMap<BSConfigShop, List<BSBuy>> getAllShopItems(String configOption) {
        HashMap<BSConfigShop, List<BSBuy>> all = new HashMap<>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null | !(shop instanceof BSConfigShop)) {
                continue;
            }
            BSConfigShop sho   = (BSConfigShop) shop;
            List<BSBuy>  items = new ArrayList<>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                if (buy.getConfigurationSection(sho).getBoolean(configOption) == false
                        && buy.getConfigurationSection(sho).getInt(configOption) == 0) {
                    continue;
                }
                items.add(buy);
            }
            all.put(sho, items);
        }

        return all;
    }

    /**
     * Add an addon to the plugin
     *
     * @param addon the addon to add
     */
    // Addon API
    protected void addEnabledAddon(BossShopAddon addon) {
        Plugin addonplugin = Bukkit.getPluginManager().getPlugin(addon.getAddonName());
        if (addonplugin == null) {
            return;
        }
        if (enabledAddons == null) {
            enabledAddons = new ArrayList<>();
        }
        if (enabledAddons.contains(addon)) {
            return;
        }
        enabledAddons.add(addon);
    }
}
