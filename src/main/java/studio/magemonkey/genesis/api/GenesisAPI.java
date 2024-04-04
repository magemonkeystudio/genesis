package studio.magemonkey.genesis.api;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.core.*;
import studio.magemonkey.genesis.core.conditions.GenesisConditionType;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.config.GenesisConfigShop;
import studio.magemonkey.genesis.managers.item.ItemDataPart;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenesisAPI {
    private final Genesis            plugin;
    @Getter
    private       List<GenesisAddon> enabledAddons;


    public GenesisAPI(Genesis plugin) {
        this.plugin = plugin;
    }


    /**
     * Gets the name of the addon for BossShop
     *
     * @param addonname the name of the addon
     * @return addon
     */
    public GenesisAddon getAddon(String addonname) {
        if (enabledAddons != null) {
            for (GenesisAddon addon : enabledAddons) {
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
        return (i.getHolder() instanceof GenesisShopHolder);
    }

    /**
     * Get a BossShop Shop
     *
     * @param name the name of the shop
     * @return shop
     */
    public GenesisShop getShop(String name) {
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
        GenesisShop shop = getShop(name);
        if (shop == null) {
            Genesis.log("[API] Error: Tried to open Shop " + name + " but it was not found...");
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
    public void openShop(Player p, GenesisShop shop) {
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
            GenesisShopHolder holder = (GenesisShopHolder) p.getOpenInventory().getTopInventory().getHolder();
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
    public GenesisShops getShopHandler() {
        return plugin.getClassManager().getShops();
    }


    /**
     * Add a shop to the plugin
     *
     * @param shop the shop to add
     */
    // Modify Shop/Shops
    public void addShop(GenesisShop shop) {
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
    public void addItemToShop(ItemStack menuItem, GenesisBuy shopItem, GenesisShop shop) {
        shop.addShopItem(shopItem, menuItem, ClassManager.manager);
    }

    /**
     * Add items to shop
     *
     * @param shop the shop to add to
     */
    public void finishedAddingItemsToShop(GenesisShop shop) {
        shop.finishedAddingItems();
    }


    /**
     * Register a condition type
     *
     * @param type the type of condition to register
     */
    // Register things
    public void registerConditionType(GenesisConditionType type) {
        type.register();
    }

    /**
     * Register a price type
     *
     * @param type the price type to register
     */
    public void registerPriceType(GenesisPriceType type) {
        type.register();
    }

    /**
     * Register a reward type
     *
     * @param type the reward type to register
     */
    public void registerRewardType(GenesisRewardType type) {
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
    public GenesisBuy createGenesisBuy(String name,
                                       GenesisRewardType rewardType,
                                       GenesisPriceType priceType,
                                       Object reward,
                                       Object price,
                                       String msg,
                                       int location,
                                       String permission) {
        return new GenesisBuy(rewardType, priceType, reward, price, msg, location, permission, name);
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
    public GenesisBuy createGenesisBuyCustom(String name,
                                             GenesisRewardType rewardType,
                                             GenesisPriceType priceType,
                                             GenesisCustomLink reward,
                                             Object price,
                                             String msg,
                                             int location,
                                             String permission) {
        return new GenesisBuy(rewardType, priceType, reward, price, msg, location, permission, name);
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
    public GenesisBuy createGenesisBuy(GenesisRewardType rewardType,
                                       GenesisPriceType priceType,
                                       Object reward,
                                       Object price,
                                       String msg,
                                       int location,
                                       String permission) {
        return new GenesisBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public GenesisBuy createGenesisBuyCustom(GenesisRewardType rewardType,
                                             GenesisPriceType priceType,
                                             GenesisCustomLink reward,
                                             Object price,
                                             String msg,
                                             int location,
                                             String permission) {
        return new GenesisBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public GenesisCustomLink createGenesisCustomLink(GenesisCustomActions actions, int actionId) {
        return new GenesisCustomLink(actionId, actions);
    }


    /**
     * Get all items in all shops
     *
     * @return list of all items
     */
    // Get shop items
    public HashMap<GenesisShop, List<GenesisBuy>> getAllShopItems() {
        HashMap<GenesisShop, List<GenesisBuy>> all = new HashMap<>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            GenesisShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null) {
                continue;
            }
            List<GenesisBuy> items = new ArrayList<>();
            for (GenesisBuy buy : shop.getItems()) {
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
    public HashMap<GenesisConfigShop, List<GenesisBuy>> getAllShopItems(String configOption) {
        HashMap<GenesisConfigShop, List<GenesisBuy>> all = new HashMap<>();
        for (int i : plugin.getClassManager().getShops().getShops().keySet()) {
            GenesisShop shop = plugin.getClassManager().getShops().getShop(i);
            if (shop == null | !(shop instanceof GenesisConfigShop)) {
                continue;
            }
            GenesisConfigShop sho   = (GenesisConfigShop) shop;
            List<GenesisBuy>  items = new ArrayList<>();
            for (GenesisBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                if (!buy.getConfigurationSection(sho).getBoolean(configOption)
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
    protected void addEnabledAddon(GenesisAddon addon) {
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
