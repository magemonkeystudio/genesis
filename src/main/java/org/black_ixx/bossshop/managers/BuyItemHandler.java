package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.conditions.BSConditionSet;
import org.black_ixx.bossshop.core.conditions.BSConditionType;
import org.black_ixx.bossshop.core.conditions.BSSingleCondition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSCreateShopItemEvent;
import org.black_ixx.bossshop.events.BSCreatedShopItemEvent;
import org.black_ixx.bossshop.events.BSLoadShopItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyItemHandler {

    /**
     * Load an item from a config into a shop
     *
     * @param itemsSection the config section for the item
     * @param shop         the shop to load it into
     * @param name         the name of the config
     * @return shop item
     */
    public List<BSBuy> loadItem(ConfigurationSection itemsSection, BSShop shop, String name) {
        if (itemsSection.getConfigurationSection(name) == null) {
            String shopName = shop == null ? "none" : shop.getShopName();
            ClassManager.manager.getBugFinder()
                    .severe("Error when trying to create BuyItem " + name + "! (1) [Shop: " + shopName + "]");
            return null;
        }
        ConfigurationSection c = itemsSection.getConfigurationSection(name);


        BSLoadShopItemEvent event = new BSLoadShopItemEvent(shop, name, c);
        Bukkit.getPluginManager().callEvent(event); //Allow addons to create a custom BSBuy
        BSBuy buy = event.getCustomShopItem();

        List<BSBuy> buyItems = new ArrayList<>();
        if (buy == null) { //If addons did not create own item create a default one here!
            buyItems.addAll(createBuyItem(shop, name, c));
        }


        if (shop != null) {
            for(BSBuy buyItem : buyItems)
                shop.addShopItem(buyItem, buyItem.getItem(), ClassManager.manager);
        }

        return buyItems;
    }

    /**
     * Create item to buy
     *
     * @param shop   name of shop to load from
     * @param name   name of item
     * @param config part of config to get from
     * @return shop item
     */
    public List<BSBuy> createBuyItem(BSShop shop, String name, ConfigurationSection config) {
        String stage = "Basic Data";
        String shopName = shop == null ? "none" : shop.getShopName();

        try {
            String priceType = config.getString("PriceType");
            String rewardType = config.getString("RewardType");
            String message = config.getString("Message");
            String permission = config.getString("ExtraPermission");
            if (permission == null || permission.isEmpty()) {
                permission = null;
            }

            List<Integer> inventoryLocations = new ArrayList<>();
            if (config.isInt("InventoryLocation")) {
                int inventoryLocation = config.getInt("InventoryLocation");
                inventoryLocations.add(inventoryLocation - 1);
            } else if (config.isList("InventoryLocation")) {
                for (int inventoryLocation : config.getIntegerList("InventoryLocation")) {
                    inventoryLocations.add(inventoryLocation - 1);
                }
            } else if (config.isString("InventoryLocation")) {
                String inventoryLocation = config.getString("InventoryLocation");
                if (inventoryLocation != null) {
                    // Split this string on ":" so that for example "1:3" gives the locations 1,2 & 3 and adds them to the list
                    String[] split = inventoryLocation.split(":");
                    try {
                        if (split.length == 2) {
                            int start = Integer.parseInt(split[0]);
                            int end = Integer.parseInt(split[1]);
                            for (int i = start; i <= end; i++) {
                                inventoryLocations.add(i - 1);
                            }
                        } else {
                            int loc = Integer.parseInt(inventoryLocation);
                            inventoryLocations.add(loc - 1);
                        }
                    } catch (Exception e) {
                        ClassManager.manager.getBugFinder()
                                .warn("The InventoryLocation of the shopitem '" + name + "' is '" + inventoryLocation
                                        + "'. It has to be either higher than '0' or it has to be '0' if you want to it to automatically pick the next empty slot. [Shop: "
                                        + shopName + "]");
                    }
                }
            }

            for(int inventoryLocation : inventoryLocations) {
                if (inventoryLocation < 0) {
                    ClassManager.manager.getBugFinder()
                            .warn("The InventoryLocation of the shopitem '" + name + "' is '" + inventoryLocation
                                    + "'. It has to be either higher than '0' or it has to be '0' if you want to it to automatically pick the next empty slot. [Shop: "
                                    + shopName + "]");

                }
            }

            stage = "Price- and RewardType Detection";

            BSRewardType rewardT = BSRewardType.detectType(rewardType);
            BSPriceType priceT = BSPriceType.detectType(priceType);

            if (rewardT == null) {
                ClassManager.manager.getBugFinder()
                        .severe("Was not able to create shopitem '" + name + "'! '" + rewardType
                                + "' is not a valid RewardType! [Shop: " + shopName + "]");
                ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
                for (BSRewardType type : BSRewardType.values()) {
                    ClassManager.manager.getBugFinder().severe("-" + type.name());
                }
                return null;
            }

            if (priceT == null) {
                ClassManager.manager.getBugFinder()
                        .severe("Was not able to create shopitem '" + name + "'! '" + priceType
                                + "' is not a valid PriceType! [Shop: " + shopName + "]");
                ClassManager.manager.getBugFinder().severe("Valid PriceTypes:");
                for (BSPriceType type : BSPriceType.values()) {
                    ClassManager.manager.getBugFinder().severe("-" + type.name());
                }
                return null;
            }

            stage = "ForceInput Detection";
            BSInputType inputType = null;
            String inputTypeName = config.getString("ForceInput");
            String inputText = config.getString("ForceInputMessage");
            if (inputTypeName != null) {
                for (BSInputType it : BSInputType.values()) {
                    if (it.name().equalsIgnoreCase(inputTypeName)) {
                        inputType = it;
                        break;
                    }
                }
                if (inputType == null) {
                    ClassManager.manager.getBugFinder()
                            .warn("Invalid ForceInput type: '" + inputTypeName + "' of shopitem '" + name + ". [Shop: "
                                    + shopName + "]");
                }
            }


            stage = "Price- and RewardType Enabling";
            rewardT.enableType();
            priceT.enableType();


            Object price = config.get("Price");
            Object reward = config.get("Reward");

            stage = "Price- and RewardType Adaption";
            price = priceT.createObject(price, true);
            reward = rewardT.createObject(reward, true);

            if (!priceT.validityCheck(name, price)) {
                return null;
            }
            if (!rewardT.validityCheck(name, reward)) {
                return null;
            }


            stage = "Optional: Conditions";
            BSConditionSet conditionsSet = null;

            List<String> conditions = config.getStringList("Condition");
            if (conditions != null) {
                BSConditionSet set = new BSConditionSet();
                BSConditionType type = null;
                for (String s : conditions) {
                    Pattern pattern = Pattern.compile("^(%.+?%|[^:]+?):(.+)$");
                    if (!pattern.matcher(s).matches()) {
                        ClassManager.manager.getBugFinder()
                                .severe("Unable to add condition '" + s + "' to shopitem '" + name
                                        + "'! It has to look like following: '<conditiontype>:<condition>'. [Shop: "
                                        + shopName + "]");
                        continue;
                    }

                    Matcher matcher = pattern.matcher(s);
                    matcher.find();

                    String a = matcher.group(1).trim();
                    String b = matcher.group(2).trim();

                    if (a.equalsIgnoreCase("type")) {
                        type = BSConditionType.detectType(b);
                        continue;
                    }
                    if (type == null) {
                        ClassManager.manager.getBugFinder()
                                .severe("Unable to add condition '" + s + "' to shopitem '" + name
                                        + "'! You need to define a conditiontype before you start listing conditions! [Shop: "
                                        + shopName + "]");
                        continue;
                    }

                    a = ClassManager.manager.getStringManager().transform(a, null, shop, null, null);
                    b = ClassManager.manager.getStringManager().transform(b, null, shop, null, null);

                    type.enableType();
                    set.addCondition(new BSSingleCondition(type, a, b));
                }
                if (!set.isEmpty()) {
                    conditionsSet = set;
                }
            }

            List<BSBuy> buyItems = new ArrayList<>();
            for(int inventoryLocation : inventoryLocations) {
                BSCreateShopItemEvent event = new BSCreateShopItemEvent(shop,
                        name,
                        config,
                        rewardT,
                        priceT,
                        reward,
                        price,
                        message,
                        inventoryLocation,
                        permission,
                        conditionsSet,
                        inputType,
                        inputText);
                Bukkit.getPluginManager().callEvent(event); //Allow addons to create a custom BSBuy

                BSBuy buy = event.getCustomShopItem();
                if (buy == null) { //If addons did not create own item create a default one here!
                    buy = new BSBuy(rewardT,
                            priceT,
                            reward,
                            price,
                            message,
                            inventoryLocation,
                            permission,
                            name,
                            conditionsSet,
                            inputType,
                            inputText);
                }
                buy.setShop(shop);

                stage = "MenuItem creation";
                if (config.getStringList("MenuItem") == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Error when trying to create shopitem " + name + "! MenuItem is not existing?! [Shop: "
                                    + shopName + "]");
                    return null;
                }

                ItemStack i = ClassManager.manager.getItemStackCreator()
                        .createItemStack(config.getStringList("MenuItem"), buy, shop, false);
                buy.setItem(i, false);


                ClassManager.manager.getSettings()
                        .update(buy); //TODO: Not tested if inheritance works fine yet. Order of methods matters!

                Bukkit.getPluginManager().callEvent(new BSCreatedShopItemEvent(shop, buy, config));
                buyItems.add(buy);
            }

            return buyItems;

        } catch (Exception e) {
            ClassManager.manager.getBugFinder()
                    .severe("Was not able to create BuyItem " + name + "! Error at Stage '" + stage + "'. [Shop: "
                            + shopName + "]");
            e.printStackTrace();
            ClassManager.manager.getBugFinder().severe("Probably caused by Config Mistakes.");
            ClassManager.manager.getBugFinder().severe("For more help please send me a PM at Spigot.");
            return null;
        }
    }


}
