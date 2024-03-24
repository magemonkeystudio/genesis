package com.promcteam.genesis.settings;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShop;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.config.GenesisConfigShop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ShopItemProperty extends ShopProperty {


    private Map<GenesisBuy, Object> shopItemSettings;

    public ShopItemProperty(ConfigurationSection config, String path, Class<?> type) {
        super(config, path, type);
    }


    /**
     * Load in a shop item property
     *
     * @param config the config to load from
     */
    @Override
    public void load(ConfigurationSection config) {
        super.load(config);
        if (ClassManager.manager.getShops() != null && ClassManager.manager.getShops().getShops() != null) {
            readShopItems(type);
        }
    }

    public void readShopItems(Class<?> type) {
        for (GenesisShop shop : ClassManager.manager.getShops().getShops().values()) {
            if (shop instanceof GenesisConfigShop) {
                GenesisConfigShop configShop = (GenesisConfigShop) shop;

                for (GenesisBuy buy : shop.getItems()) {
                    readShopItem(buy, configShop, type);
                }

            }
        }
    }

    /**
     * Update a shop item property
     *
     * @param o shop
     */
    @Override
    public void update(Object o) {
        if (o instanceof GenesisBuy) {
            GenesisBuy buy = (GenesisBuy) o;
            readShopItem(buy, type);
        }
        super.update(o);
    }

    /**
     * Read in a shop item
     *
     * @param buy  the item
     * @param type the type
     */
    public void readShopItem(GenesisBuy buy, Class<?> type) {
        if (buy.getShop() instanceof GenesisConfigShop) {
            GenesisConfigShop configShop = (GenesisConfigShop) buy.getShop();
            readShopItem(buy, configShop, type);
        }
    }

    /**
     * REad in a shop item
     *
     * @param buy        the item
     * @param configShop the shop to read from
     * @param type       the type
     */
    public void readShopItem(GenesisBuy buy, GenesisConfigShop configShop, Class<?> type) {
        ConfigurationSection section = buy.getConfigurationSection(configShop);
        if (section != null && section.contains(path)) {
            if (shopItemSettings == null) {
                shopItemSettings = new HashMap<>();
            }
            shopItemSettings.put(buy, read(section));
        }
    }

    /**
     * Read a shop
     *
     * @param shop the shop to read
     * @param type the type
     */
    @Override
    public void readShop(GenesisShop shop, Class<?> type) {
        super.readShop(shop, type);
        for (GenesisBuy buy : shop.getItems()) {
            readShopItem(buy, type);
        }
    }


    /**
     * Check if an object contains a value
     *
     * @param input where to check it
     * @param value what to check
     * @return
     */
    public boolean containsValue(Object input, Object value) {
        if (input instanceof GenesisBuy) {
            GenesisBuy buy = (GenesisBuy) input;
            if (containsValueShopItem(buy, value)) {
                return true;
            }
            return super.containsValue(buy.getShop(), value);
        }

        return super.containsValue(input, value);
    }

    /**
     * Check if a shop item contains a value
     *
     * @param buy
     * @param value
     * @return contains or not
     */
    public boolean containsValueShopItem(GenesisBuy buy, Object value) {
        if (shopItemSettings != null && shopItemSettings.containsKey(buy)) {
            return isIdentical(shopItemSettings.get(buy), value);
        }
        return false;
    }

    /**
     * Check if a shop item contains any value
     *
     * @param value the value to check
     * @return contains or not
     */
    @Override
    public boolean containsValueAny(Object value) {
        if (shopItemSettings != null) {
            for (GenesisBuy buy : shopItemSettings.keySet()) {
                if (containsValueShopItem(buy, value)) {
                    return true;
                }
            }
        }
        return super.containsValueAny(value);
    }


    /**
     * Get the shop item object
     *
     * @param input the shop to check
     * @return item
     */
    @Override
    public Object getObject(Object input) {
        if (input instanceof GenesisBuy) {
            GenesisBuy buy = (GenesisBuy) input;
            if (shopItemSettings != null && shopItemSettings.containsKey(buy)) {
                return shopItemSettings.get(buy);
            }
            return super.getObject(buy.getShop());
        }

        return super.getObject(input);
    }

}
