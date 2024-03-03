package org.black_ixx.bossshop.settings;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ShopItemProperty extends ShopProperty {


    private Map<BSBuy, Object> shopItemSettings;

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
        for (BSShop shop : ClassManager.manager.getShops().getShops().values()) {
            if (shop instanceof BSConfigShop) {
                BSConfigShop configShop = (BSConfigShop) shop;

                for (BSBuy buy : shop.getItems()) {
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
        if (o instanceof BSBuy) {
            BSBuy buy = (BSBuy) o;
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
    public void readShopItem(BSBuy buy, Class<?> type) {
        if (buy.getShop() instanceof BSConfigShop) {
            BSConfigShop configShop = (BSConfigShop) buy.getShop();
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
    public void readShopItem(BSBuy buy, BSConfigShop configShop, Class<?> type) {
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
    public void readShop(BSShop shop, Class<?> type) {
        super.readShop(shop, type);
        for (BSBuy buy : shop.getItems()) {
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
        if (input instanceof BSBuy) {
            BSBuy buy = (BSBuy) input;
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
    public boolean containsValueShopItem(BSBuy buy, Object value) {
        if (shopItemSettings != null && shopItemSettings.containsKey(buy)) {
            if (isIdentical(shopItemSettings.get(buy), value)) {
                return true;
            }
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
            for (BSBuy buy : shopItemSettings.keySet()) {
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
        if (input instanceof BSBuy) {
            BSBuy buy = (BSBuy) input;
            if (shopItemSettings != null && shopItemSettings.containsKey(buy)) {
                return shopItemSettings.get(buy);
            }
            return super.getObject(buy.getShop());
        }

        return super.getObject(input);
    }

}
