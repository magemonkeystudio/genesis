package studio.magemonkey.genesis.core;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.config.FileHandler;
import studio.magemonkey.genesis.managers.config.GenesisConfigShop;
import studio.magemonkey.genesis.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.util.HashMap;

public class GenesisShops {

    private final HashMap<Integer, GenesisShop> shops;
    private final HashMap<String, Integer>      shopsIds;

    /////////////////////////////// <- Variables
    private int id = 0;

    public GenesisShops(Genesis plugin, Settings settings) {
        shops = new HashMap<>();
        shopsIds = new HashMap<>();

        File folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "shops" + File.separator);
        new FileHandler().exportShops(plugin);

        boolean enableShopCommands = loadShops(folder, settings, "");
        ClassManager.manager.getSettings().setShopCommandsEnabled(enableShopCommands);

        Genesis.log("Loaded " + shops.size() + " Shops!");
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
                        GenesisShop shop = loadShop(f, parentPath);

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

    public void addShop(GenesisShop shop) {
        shops.put(shop.getShopId(), shop);

        if (shopsIds.containsKey(shop.getShopName().toLowerCase())) {
            ClassManager.manager.getBugFinder()
                    .warn("Two Shops with the same Name (" + shop.getShopName().toLowerCase()
                            + ") are loaded. When opening a Shop via Name, only one of this Shops will be opened!");
        }

        shopsIds.put(shop.getShopName().toLowerCase(), shop.getShopId());
    }

    public GenesisShop loadShop(File f, String parentPath) {
        String      name = parentPath + f.getName();
        GenesisShop shop = new GenesisConfigShop(createId(), name, this);

        addShop(shop);

        return shop;
    }

    public void unloadShop(GenesisShop shop) {
        int id = getShopId(shop.getShopName());
        shopsIds.remove(shop.getShopName());
        shops.remove(id);
        shop.close();
    }

    public GenesisShop reloadShop(GenesisShop shop) {
        GenesisShop reloadedShop =
                new GenesisConfigShop(shop.getShopId(), ((GenesisConfigShop) shop).getYmlName(), this);
        unloadShop(shop);
        addShop(reloadedShop);
        return reloadedShop;
    }

    /////////////////////////////// <- Simple Methods

    public void openShop(Player p, String name) {
        if (!isShop(name)) {
            ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", p);
            return;
        }
        openShop(p, getShopFast(name));
    }

    public void openShop(Player p, GenesisShop shop) {
        int     page                = 0;
        boolean rememberCurrentShop = true;

        InventoryView view = p.getOpenInventory();
        if (view != null && view.getTopInventory() != null && view.getTopInventory()
                .getHolder() instanceof GenesisShopHolder) {
            GenesisShopHolder holder        = (GenesisShopHolder) view.getTopInventory().getHolder();
            GenesisShopHolder oldShopHolder = holder.getPreviousShopHolder();
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

    public GenesisShop getShop(String name) {
        return getShop(getShopId(name));
    }

    public GenesisShop getShopFast(String name) {
        return getShopFast(getShopId(name));
    }

    public GenesisShop getShopByCommand(String playerCommand) {
        if (playerCommand != null && playerCommand.length() > 0) {
            for (GenesisShop shop : shops.values()) {
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

    public GenesisShop getShop(int id) {
        return shops.getOrDefault(id, null);
    }

    public GenesisShop getShopFast(int id) {
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

    public HashMap<Integer, GenesisShop> getShops() {
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
                Inventory         openInventory = p.getOpenInventory().getTopInventory();
                GenesisShopHolder h             = (GenesisShopHolder) openInventory.getHolder();

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
