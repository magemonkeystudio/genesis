package studio.magemonkey.genesis;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.magemonkey.genesis.api.GenesisAPI;
import studio.magemonkey.genesis.api.GenesisAddon;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.events.GenesisReloadedEvent;
import studio.magemonkey.genesis.folia.CrossScheduler;
import studio.magemonkey.genesis.inbuiltaddons.InbuiltAddonLoader;
import studio.magemonkey.genesis.listeners.InventoryListener;
import studio.magemonkey.genesis.listeners.PlayerListener;
import studio.magemonkey.genesis.listeners.SignListener;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.CommandManager;
import studio.magemonkey.genesis.managers.config.ConfigKeyCompleter;

public class Genesis extends JavaPlugin {

    public final static String            NAME = "Genesis";
    private             ClassManager      manager;
    private             InventoryListener il;

    //////////////////////////////////////////////
    private SignListener   sl;
    private PlayerListener pl;
    private GenesisAPI     api;

    public static void log(String s) {
        Bukkit.getLogger().info("[" + NAME + "] " + s);
    }

    public static void debug(String s) {
        if (ClassManager.manager.getSettings().isDebugEnabled()) {
            log(s);
        }
    }

    ////////////////////////////////////////////////

    @Override
    public void onEnable() {
        log("Loading data...");
        manager = new ClassManager(this);
        api = new GenesisAPI(this);

        CommandManager commander = new CommandManager();

        if (getCommand("gen") != null) {
            getCommand("gen").setExecutor(commander);
            getCommand("gen").setTabCompleter(commander);
        }
        if (getCommand("genesis") != null) {
            getCommand("genesis").setExecutor(commander);
            getCommand("genesis").setTabCompleter(commander);
        }
        if (getCommand("shop") != null) {
            getCommand("shop").setExecutor(commander);
            getCommand("shop").setTabCompleter(commander);
        }


        ////////////////<- Listeners

        il = new InventoryListener(this);
        getServer().getPluginManager().registerEvents(il, this);

        sl = new SignListener(manager.getSettings().getSignsEnabled(), this);
        getServer().getPluginManager().registerEvents(sl, this);

        pl = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(pl, this);

        ////////////////
        CrossScheduler.runTaskLaterAsync(() -> {
            new InbuiltAddonLoader().load(Genesis.this);
            getClassManager().setupDependentClasses();
        }, 3);

        ////////////////<- File key complete

        ConfigKeyCompleter.checkConfig();
        ConfigKeyCompleter.checkLanguages();
    }

    @Override
    public void onDisable() {
        closeShops();
        unloadClasses();
        log("Disabling... bye!");
    }

    public ClassManager getClassManager() {
        return manager;
    }

    public SignListener getSignListener() {
        return sl;
    }

    public InventoryListener getInventoryListener() {
        return il;
    }

    /////////////////////////////////////////////////

    public PlayerListener getPlayerListener() {
        return pl;
    }

    public GenesisAPI getAPI() {
        return api;
    }

    public void reloadPlugin(CommandSender sender) {
        closeShops();

        reloadConfig();
        manager.getMessageHandler().reloadConfig();

        if (manager.getShops() != null) {
            for (String s : manager.getShops().getShopIds().keySet()) {
                GenesisShop shop = manager.getShops().getShops().get(s);
                if (shop != null) {
                    shop.reloadShop();
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (api.isValidShop(p.getOpenInventory())) {
                p.closeInventory();
            }
        }

        sl.setSignsEnabled(false); // Wird durch ConfigHandler umgesetzt (ClassManager laedt ConfigHandler)

        unloadClasses();

        manager = new ClassManager(this);

        if (api.getEnabledAddons() != null) {
            for (GenesisAddon addon : api.getEnabledAddons()) {
                addon.reload(sender);
            }
        }

        manager.setupDependentClasses();


        GenesisReloadedEvent event = new GenesisReloadedEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    private void unloadClasses() {
        CrossScheduler.cancelTasks(this);

        if (manager == null) {
            return;
        }

        if (manager.getSettings() == null) {
            return;
        }

        if (manager.getStorageManager() != null) {
            manager.getStorageManager().saveConfig();
        }
        if (manager.getItemDataStorage() != null) {
            manager.getItemDataStorage().saveConfig();
        }

        if (manager.getSettings().getTransactionLogEnabled()) {
            manager.getTransactionLog().saveConfig();
        }

        if (manager.getSettings().getServerPingingEnabled(true)) {
            manager.getServerPingingManager().getServerPingingRunnableHandler().stop();
            manager.getServerPingingManager().clear();
        }

        if (manager.getAutoRefreshHandler() != null) {
            manager.getAutoRefreshHandler().stop();
        }
    }

    private void closeShops() {
        if (manager == null || manager.getShops() == null || manager.getShops().getShops() == null) {
            return;
        }
        for (int i : manager.getShops().getShops().keySet()) {
            GenesisShop shop = manager.getShops().getShops().get(i);
            if (shop != null) {
                shop.close();
            }
        }
    }
}
