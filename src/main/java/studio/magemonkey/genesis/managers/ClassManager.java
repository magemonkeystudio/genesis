package studio.magemonkey.genesis.managers;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.api.GenesisAddon;
import studio.magemonkey.genesis.core.GenesisShops;
import studio.magemonkey.genesis.core.conditions.GenesisConditionType;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.events.GenesisRegisterTypesEvent;
import studio.magemonkey.genesis.managers.config.ConfigHandler;
import studio.magemonkey.genesis.managers.config.FileHandler;
import studio.magemonkey.genesis.managers.external.BungeeCordManager;
import studio.magemonkey.genesis.managers.external.LanguageManager;
import studio.magemonkey.genesis.managers.external.PlaceholderAPIHandler;
import studio.magemonkey.genesis.managers.external.VaultHandler;
import studio.magemonkey.genesis.managers.external.spawners.ISpawnEggHandler;
import studio.magemonkey.genesis.managers.external.spawners.ISpawnerHandler;
import studio.magemonkey.genesis.managers.external.spawners.SpawnersHandlerEpicSpawners;
import studio.magemonkey.genesis.managers.external.spawners.SpawnersHandlerSilkSpawners;
import studio.magemonkey.genesis.managers.item.ItemDataPart;
import studio.magemonkey.genesis.managers.item.ItemStackChecker;
import studio.magemonkey.genesis.managers.item.ItemStackCreator;
import studio.magemonkey.genesis.managers.item.ItemStackTranslator;
import studio.magemonkey.genesis.managers.misc.StringManager;
import studio.magemonkey.genesis.managers.features.*;
import studio.magemonkey.genesis.managers.serverpinging.ServerPingingManager;
import studio.magemonkey.genesis.misc.MathTools;
import studio.magemonkey.genesis.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ClassManager {

    public static ClassManager     manager;
    private final ItemStackChecker itemstackChecker;
    private final StringManager    stringmanager;


    ///////////////////////////////
    private       PointsManager         pointsmanager;
    private       VaultHandler          vaulthandler;
    private       PlaceholderAPIHandler placeholderhandler;
    private final MessageHandler        messagehandler;
    private final ItemStackCreator      itemstackCreator;
    private final ItemStackTranslator   itemstackTranslator;
    private final BuyItemHandler        buyItemHandler;
    private final ConfigHandler        configHandler;
    private final BugFinder            bugfinder;
    private final Genesis              plugin;
    private final Settings              settings;
    private       GenesisShops          shops;
    private       PageLayoutHandler     pagelayoutHandler;
    private       BungeeCordManager     bungeeCordManager;
    private       ShopCustomizer        customizer;
    private       TransactionLog        transactionLog;
    private       ServerPingingManager serverPingingManager;
    private       AutoRefreshHandler   autoRefreshHandler;
    private final MultiplierHandler    multiplierHandler;
    private final StorageManager       storageManager;
    private       ISpawnEggHandler      spawnEggHandler;
    private       ISpawnerHandler       spawnerHandler;
    private       LanguageManager      languageManager;
    private final ItemDataStorage      itemdataStorage;
    private final PlayerDataHandler    playerdataHandler;

    public ClassManager(Genesis plugin) {
        this.plugin = plugin;
        manager = this;
        settings = new Settings();

        new FileHandler().exportConfigs(plugin);

        GenesisRewardType.loadTypes();
        GenesisPriceType.loadTypes();
        GenesisConditionType.loadTypes();
        ItemDataPart.loadTypes();

        //////////////// <- Independent Classes

        playerdataHandler = new PlayerDataHandler();
        configHandler = new ConfigHandler(plugin);
        MathTools.init(settings.getNumberLocale(), settings.getNumberGroupingSize());
        storageManager = new StorageManager(plugin);
        bugfinder = new BugFinder(plugin);
        itemdataStorage = new ItemDataStorage(plugin);
        multiplierHandler = new MultiplierHandler(plugin);
        stringmanager = new StringManager();
        itemstackCreator = new ItemStackCreator();
        itemstackTranslator = new ItemStackTranslator();
        buyItemHandler = new BuyItemHandler();
        itemstackChecker = new ItemStackChecker();
        messagehandler = new MessageHandler(plugin);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderhandler = new PlaceholderAPIHandler();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("LangUtils")) {
            languageManager = new LanguageManager();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("SilkSpawners")) {
            try {
                Class.forName("de.dustplanet.util.SilkUtil");
                SpawnersHandlerSilkSpawners h = new SpawnersHandlerSilkSpawners();
                spawnerHandler = h;
                spawnEggHandler = h;
            } catch (ClassNotFoundException e) {
                getBugFinder().warn(
                        "It seems like you have 'SilkSpawners' installed, but Genesis does not recognize the API of the plugin. "
                                +
                                "Note: There are different SilkSpawners plugins around. The one Genesis can hook into is https://www.spigotmc.org/resources/7811/. "
                                +
                                "Others are simply ignored.");
            }
        }

        if (Bukkit.getPluginManager().isPluginEnabled("EpicSpawners")) {
            spawnerHandler = new SpawnersHandlerEpicSpawners();
        }

    }

    /**
     * Setup the dependent classes
     */
    public void setupDependentClasses() {
        Bukkit.getPluginManager().callEvent(new GenesisRegisterTypesEvent());

        FileConfiguration config = plugin.getConfig();
        plugin.getInventoryListener()
                .init(config.getInt("ClickDelay"),
                        config.getInt("ClickSpamKick.ClickDelay"),
                        config.getInt("ClickSpamKick.Warnings"),
                        config.getInt("ClickSpamKick.ForgetTime"));

        pagelayoutHandler = new PageLayoutHandler(plugin);

        //if (settings.getPointsEnabled()){ Is not known because shops are not yet loaded. But is required before shops are loaded in order to be able to display items properly.
        pointsmanager = new PointsManager();
        //}

        shops = new GenesisShops(plugin, settings);

        if (settings.getVaultEnabled()) {
            Plugin VaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
            if (VaultPlugin == null) {
                ClassManager.manager.getBugFinder()
                        .warn("Vault was not found... You need it if you want to work with Permissions, Permission Groups or Money! Get it there: https://www.spigotmc.org/resources/vault.34315/");
            } else {
                vaulthandler = new VaultHandler(settings.getMoneyEnabled(), settings.getPermissionsEnabled());
            }
        }

        if (settings.getBalanceVariableEnabled() || settings.getBalancePointsVariableEnabled() || settings.getProperty(
                Settings.HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR).containsValueAny(true)) {
            customizer = new ShopCustomizer();
        }

        if (settings.getTransactionLogEnabled()) {
            transactionLog = new TransactionLog(plugin);
        }

        if (settings.getServerPingingEnabled(false)) {
            serverPingingManager = new ServerPingingManager(plugin);
            getServerPingingManager().getServerPingingRunnableHandler()
                    .start(settings.getServerPingingSpeed(), plugin, getServerPingingManager());
            getServerPingingManager().setReadyToTransform(true);
        }

        if (settings.getBungeeCordServerEnabled()) { //Depends on ServerPinging
            bungeeCordManager = new BungeeCordManager();
            Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", bungeeCordManager);
        }

        if (settings.getAutoRefreshSpeed() > 0) {
            autoRefreshHandler = new AutoRefreshHandler();
            autoRefreshHandler.start(settings.getAutoRefreshSpeed(), plugin);
        }


        if (plugin.getAPI().getEnabledAddons() != null) {
            for (GenesisAddon addon : plugin.getAPI().getEnabledAddons()) {
                addon.genesisFinishedLoading();
            }
        }
    }

    ///////////////////////////////

    public Settings getSettings() {
        return settings;
    }

    public ItemStackChecker getItemStackChecker() {
        return itemstackChecker;
    }

    public StringManager getStringManager() {
        return stringmanager;
    }

    public PointsManager getPointsManager() {
        return pointsmanager;
    }

    public VaultHandler getVaultHandler() {
        if (vaulthandler == null) {
            return new VaultHandler(ClassManager.manager.getSettings().getMoneyEnabled(),
                    ClassManager.manager.getSettings().getPointsEnabled());
        }
        return vaulthandler;
    }

    public PlaceholderAPIHandler getPlaceholderHandler() {
        return placeholderhandler;
    }

    public MessageHandler getMessageHandler() {
        return messagehandler;
    }

    public ItemStackCreator getItemStackCreator() {
        return itemstackCreator;
    }

    public ItemStackTranslator getItemStackTranslator() {
        return itemstackTranslator;
    }

    public BuyItemHandler getBuyItemHandler() {
        return buyItemHandler;
    }

    @SuppressWarnings("unused")
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public BugFinder getBugFinder() {
        return bugfinder;
    }

    public Genesis getPlugin() {
        return plugin;
    }

    public GenesisShops getShops() {
        return shops;
    }

    public PageLayoutHandler getPageLayoutHandler() {
        return pagelayoutHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerdataHandler;
    }

    public BungeeCordManager getBungeeCordManager() {
        if (bungeeCordManager == null) {
            bungeeCordManager = new BungeeCordManager();
        }
        return bungeeCordManager;
    }

    public ShopCustomizer getShopCustomizer() {
        if (customizer == null) {
            customizer = new ShopCustomizer();
        }
        return customizer;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public ServerPingingManager getServerPingingManager() {
        return serverPingingManager;
    }

    public MultiplierHandler getMultiplierHandler() {
        return multiplierHandler;
    }

    public AutoRefreshHandler getAutoRefreshHandler() {
        return autoRefreshHandler;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public ISpawnerHandler getSpawnerHandler() {
        return spawnerHandler;
    }

    public ISpawnEggHandler getSpawnEggHandler() {
        return spawnEggHandler;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public ItemDataStorage getItemDataStorage() {
        return itemdataStorage;
    }


}
