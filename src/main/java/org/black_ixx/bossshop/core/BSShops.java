package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.config.FileHandler;
import org.black_ixx.bossshop.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.HashMap;

public class BSShops {

    private HashMap<Integer, BSShop> shops;
    private HashMap<String, Integer> shopsIds;

    /////////////////////////////// <- Variables
    private int id = 0;

    public BSShops(BossShop plugin, Settings settings) {
        shops = new HashMap<>();
        shopsIds = new HashMap<>();

        File folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "shops" + File.separator);
        new FileHandler().exportShops(plugin);

        boolean enableShopCommands = loadShops(folder, settings, "");
        ClassManager.manager.getSettings().setShopCommandsEnabled(enableShopCommands);

        BossShop.log("Loaded " + shops.size() + " Shops!");
    }

    private boolean loadShops(File folder, Settings settings, String parentPath) {
        boolean enableShopCommands = false;

        for (File f : folder.listFiles()) {
            if (f != null) {
                if (f.isDirectory()) {
                    if (settings.getLoadSubFoldersEnabled()) {
                        if (loadShops(f, settings, f.getName() + File.separator)) {
                            enableShopCommands = true;
                        }
                    }
                    continue;
                }

                if (f.isFile()) {
                    if (f.getName().contains(".yml")) {
                        BSShop shop = loadShop(f, parentPath);

                        if (shop.getCommands() != null) {
                            enableShopCommands = true;
                        }
                    }

                }
            }
        }
        return enableShopCommands;
    }

    /////////////////////////////// <- Load Shop

    public void addShop(BSShop shop) {
        shops.put(shop.getShopId(), shop);

        if (shopsIds.containsKey(shop.getShopName().toLowerCase())) {
            ClassManager.manager.getBugFinder()
                    .warn("Two Shops with the same Name (" + shop.getShopName().toLowerCase()
                            + ") are loaded. When opening a Shop via Name, only one of this Shops will be opened!");
        }

        shopsIds.put(shop.getShopName().toLowerCase(), shop.getShopId());
    }

    public BSShop loadShop(File f, String parentPath) {
        String name = parentPath + f.getName();
        BSShop shop = new BSConfigShop(createId(), name, this);

        addShop(shop);

        return shop;
    }

    public void unloadShop(BSShop shop) {
        int id = getShopId(shop.getShopName());
        shopsIds.remove(shop.getShopName());
        shops.remove(id);
        shop.close();
    }

    public void reloadShop(BSShop shop) {
        BSShop reloadedShop = new BSConfigShop(shop.getShopId(), ((BSConfigShop) shop).getYmlName(), this);
        unloadShop(shop);
        addShop(reloadedShop);
    }

    /////////////////////////////// <- Simple Methods

    public void openShop(Player p, String name) {
        if (!isShop(name)) {
            ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", p);
            return;
        }
        openShop(p, getShopFast(name));
    }

    public void openShop(Player p, BSShop shop) {
        int page = 0;
        boolean rememberCurrentShop = true;

        InventoryView view = p.getOpenInventory();
        if (view != null && view.getTopInventory() != null && view.getTopInventory()
                .getHolder() instanceof BSShopHolder) {
            BSShopHolder holder = (BSShopHolder) view.getTopInventory().getHolder();
            BSShopHolder oldShopHolder = holder.getPreviousShopHolder();
            if (oldShopHolder != null) {
                // Going back to previous shop
                if (oldShopHolder.getShop() == shop) {
                    page = oldShopHolder.getPage();

                    /* If going back to parent shop, children shop should not be remembered
                     *  That way it can be prevented that all previous shops are kept in memory when players keep switching between shops
                     *  Note: This might cause confusion in some causes because some pages are restored and some are not.
                     */
                    rememberCurrentShop = false;
                }
            }
        }


        shop.openInventory(p, page, rememberCurrentShop);
    }

    public BSShop getShop(String name) {
        return getShop(getShopId(name));
    }

    public BSShop getShopFast(String name) {
        return getShopFast(getShopId(name));
    }

    public BSShop getShopByCommand(String playerCommand) {
        if (playerCommand != null && playerCommand.length() > 0) {
            for (BSShop shop : shops.values()) {
                String[] commands = shop.getCommands();
                if (commands != null) {
                    for (String command : commands) {
                        if (command.equalsIgnoreCase(playerCommand)) {
                            return shop;
                        }
                    }
                }
            }
        }
        return null;
    }

    public BSShop getShop(int id) {
        return shops.getOrDefault(id, null);
    }

    public BSShop getShopFast(int id) {
        return shops.get(id);
    }

    public int getShopId(String name) {
        name = name.toLowerCase();
        if (!shopsIds.containsKey(name)) {
            // ClassManager.manager.getBugFinder().warn("Was not able to get id of the "+name+" Shop.");
            return -1; // Was return 0 before. Changed because I think then it returns no shop for sure!
        }
        return shopsIds.get(name);
    }

    public boolean isShop(String name) {
        return shopsIds.containsKey(name);
    }

    public boolean isShop(int id) {
        return shops.containsKey(id);
    }

    public HashMap<Integer, BSShop> getShops() {
        return shops;
    }

    public HashMap<String, Integer> getShopIds() {
        return shopsIds;
    }

    public int createId() {
        id++;
        return id;
    }


    ////////////////////////////////////////////////////////////////////////////

    public void refreshShops(boolean serverPinging) {
        for (Player p : Bukkit.getOnlinePlayers()) { // If players have a customizable inventory open it needs an update
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                Inventory openInventory = p.getOpenInventory().getTopInventory();
                BSShopHolder h = (BSShopHolder) openInventory.getHolder();

                if (h.getShop().isCustomizable()) {
                    if (!serverPinging) {
                        if (ClassManager.manager.getSettings().getServerPingingEnabled(true)) {
                            if (ClassManager.manager.getServerPingingManager().containsServerpinging(h.getShop())) {
                                continue;
                            }
                        }
                        h.getShop()
                                .updateInventory(openInventory,
                                        h,
                                        p,
                                        ClassManager.manager,
                                        h.getPage(),
                                        h.getHighestPage(),
                                        true);
                    }
                }
            }
        }
    }
}
