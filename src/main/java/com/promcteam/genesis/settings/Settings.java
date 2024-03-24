package com.promcteam.genesis.settings;

import com.promcteam.genesis.managers.ClassManager;
import lombok.Getter;
import lombok.Setter;
import com.promcteam.genesis.managers.features.PointsManager.PointsPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Settings {

    public final static int
            HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR = 0,
            ALLOW_UNSAFE_ENCHANTMENTS                    = 1,
            ALLOW_SELLING_GREATER_ENCHANTS               = 2,
            CLOSE_SHOP_AFTER_PURCHASE                    = 3,
            CLICK_DELAY                                  = 4,
            SOUND_SHOPITEM_PURCHASE                      = 5, //Actions with a price that is not nothing
            SOUND_SHOPITEM_CLICK                         = 6, //Actions with priceType nothing
            SOUND_SHOPITEM_NOPERMISSION                  = 7,
            SOUND_SHOPITEM_NOTENOUGHMONEY                = 8,
            SOUND_SHOP_OPEN                              = 9,
            SOUND_SHOP_CLOSE                             = 10,
            SOUND_SHOP_CHANGE_PAGE                       = 11,
            SOUND_SHOP_CHANGE_SHOP                       = 12;

    @Setter
    private boolean moneyEnabled, pointsEnabled, vaultEnabled, permissionsEnabled, bungeeCordServerEnabled,
            loadSubFoldersEnabled, transactionLogEnabled, checkStackSize, expUseLevel, serverPingingFixConnector,
            itemAllShowFinalReward, inventoryFullDropItems, allowSellingDamagedItems, reloadAfterCreateShop;
    private boolean signs, pointsDisplay, moneyDisplay, serverPinging, shopCommands, purchaseAsync;
    private boolean metrics = true;
    @Getter
    @Setter
    private int     serverPingingSpeed, serverPingingWaitTime, serverPingingTimeout, autoRefreshSpeed, maxLineLength,
            numberGroupingSize, inputTimeout;
    @Getter
    @Setter
    private String mainShop, numberLocale;
    @Setter
    @Getter
    private PointsPlugin pointsPlugin;
    @Getter
    @Setter
    private List<String> moneyFormatting, pointsFormatting;

    @Getter
    @Setter
    private boolean debugEnabled;

    private final Map<Integer, SettingsProperty> properties = new LinkedHashMap<>();
    @Setter
    @Getter
    private       String                         language;


    /**
     * Load configuration settings from config
     *
     * @param config config to load from
     */
    public void loadConfig(ConfigurationSection config) {
        properties.clear();
        properties.put(HIDE_ITEMS_PLAYERS_DONT_HAVE_PERMISSIONS_FOR,
                new ShopItemProperty(config, "HideItemsPlayersDoNotHavePermissionsFor", Boolean.class));
        properties.put(ALLOW_UNSAFE_ENCHANTMENTS,
                new ShopItemProperty(config, "AllowUnsafeEnchantments", Boolean.class));
        properties.put(ALLOW_SELLING_GREATER_ENCHANTS,
                new ShopItemProperty(config, "CanPlayersSellItemsWithGreaterEnchants", Boolean.class));
        properties.put(CLOSE_SHOP_AFTER_PURCHASE,
                new ShopItemProperty(config, "CloseShopAfterPurchase", Boolean.class));
        properties.put(CLICK_DELAY, new ShopItemProperty(config, "ClickDelay", Integer.class));
        properties.put(SOUND_SHOPITEM_PURCHASE, new ShopItemProperty(config, "Sound.Shopitem.Purchase", String.class));
        properties.put(SOUND_SHOPITEM_CLICK, new ShopItemProperty(config, "Sound.Shopitem.Click", String.class));
        properties.put(SOUND_SHOPITEM_NOPERMISSION,
                new ShopItemProperty(config, "Sound.Shopitem.NoPermission", String.class));
        properties.put(SOUND_SHOPITEM_NOTENOUGHMONEY,
                new ShopItemProperty(config, "Sound.Shopitem.NotEnoughMoney", String.class));
        properties.put(SOUND_SHOP_OPEN, new ShopProperty(config, "Sound.Shop.Open", String.class));
        properties.put(SOUND_SHOP_CLOSE, new ShopProperty(config, "Sound.Shop.Close", String.class));
        properties.put(SOUND_SHOP_CHANGE_PAGE, new ShopProperty(config, "Sound.Shop.ChangePage", String.class));
        properties.put(SOUND_SHOP_CHANGE_SHOP, new ShopProperty(config, "Sound.Shop.ChangeShop", String.class));
    }

    /**
     * Update a config
     *
     * @param o config
     */
    public void update(Object o) {
        for (SettingsProperty property : properties.values()) {
            property.update(o);
        }
    }

    /**
     * Update config
     */
    public void update() {
        Configuration mainConfig = ClassManager.manager.getPlugin().getConfig();
        for (SettingsProperty property : properties.values()) {
            property.load(mainConfig);
        }
    }

    /**
     * Get property
     *
     * @param id id of property
     * @return property
     */
    public SettingsProperty getProperty(int id) {
        return properties.get(id);
    }

    /**
     * Get property boolean
     *
     * @param id    id of property
     * @param input object to get
     * @return boolean
     */
    public boolean getPropertyBoolean(int id, Object input) {
        SettingsProperty property = getProperty(id);
        if (property != null) {
            return property.getBoolean(input);
        }
        return false;
    }

    /**
     * Get property int
     *
     * @param id    id of property
     * @param input object to get
     * @param def   default
     * @return int
     */
    public int getPropertyInt(int id, Object input, int def) {
        SettingsProperty property = getProperty(id);
        if (property != null) {
            return property.getInt(input);
        }
        return def;
    }

    /**
     * Get property string
     *
     * @param id    id of property
     * @param input object to get
     * @param def   default
     * @return string
     */
    public String getPropertyString(int id, Object input, String def) {
        SettingsProperty property = getProperty(id);
        if (property != null) {
            return property.getString(input);
        }
        return def;
    }

    public void setPurchaseAsyncEnabled(boolean b) {
        purchaseAsync = b;
    }

    public void setServerPingingEnabled(boolean b) {
        serverPinging = b;
    }

    public boolean getMetricsEnabled() {
        return metrics;
    }

    public void setMetricsEnabled(boolean b) {
        metrics = b;
    }

    public boolean getShopCommandsEnabled() {
        return shopCommands;
    }

    public void setShopCommandsEnabled(boolean b) {
        shopCommands = b;
    }

    public boolean getSignsEnabled() {
        return signs;
    }

    public void setSignsEnabled(boolean b) {
        signs = b;
        if (ClassManager.manager.getPlugin().getSignListener() != null) {
            ClassManager.manager.getPlugin().getSignListener().setSignsEnabled(b);
        }
    }

    public boolean getMoneyEnabled() {
        return moneyEnabled;
    }

    public boolean getPointsEnabled() {
        return pointsEnabled;
    }

    public boolean getVaultEnabled() {
        return vaultEnabled;
    }

    public boolean getPermissionsEnabled() {
        return permissionsEnabled;
    }

    public boolean getPurchaseAsync() {
        return purchaseAsync;
    }

    public boolean getExpUseLevel() {
        return expUseLevel;
    }

    public boolean getBungeeCordServerEnabled() {
        return bungeeCordServerEnabled;
    }

    public boolean getBalanceVariableEnabled() {//TODO: probably remove this
        return moneyDisplay;
    }

    public void setBalanceVariableEnabled(boolean b) {
        moneyDisplay = b;
    }

    public boolean getBalancePointsVariableEnabled() {
        return pointsDisplay;
    }

    public void setBalancePointsVariableEnabled(boolean b) {
        pointsDisplay = b;
    }

    public boolean getTransactionLogEnabled() {
        return transactionLogEnabled;
    }

    public boolean getServerPingingEnabled(boolean checkIfLoadedAlready) {
        if (checkIfLoadedAlready) {
            return ClassManager.manager.getServerPingingManager() != null;
        }
        return serverPinging;
    }

    public boolean getServerPingingFixConnector() {
        return serverPingingFixConnector;
    }

    public boolean getInventoryFullDropItems() {
        return inventoryFullDropItems;
    }

    public boolean getLoadSubFoldersEnabled() {
        return loadSubFoldersEnabled;
    }

    public boolean getCheckStackSize() {
        return checkStackSize;
    }

    public boolean getItemAllShowFinalReward() {
        return itemAllShowFinalReward;
    }

    public boolean getAllowSellingDamagedItems() {
        return allowSellingDamagedItems;
    }

    public boolean getReloadAfterCreateShop() {return reloadAfterCreateShop;}
}
