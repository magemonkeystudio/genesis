package studio.magemonkey.genesis.inbuiltaddons.advancedshops;

import studio.magemonkey.genesis.core.GenesisInputType;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.events.GenesisCheckStringForFeaturesEvent;
import studio.magemonkey.genesis.events.GenesisCreateShopItemEvent;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;


public class ShopItemCreationListener implements Listener {


    @EventHandler
    public void onCreate(GenesisCreateShopItemEvent event) {
        ConfigurationSection c = event.getConfigurationSection();

        Map<ClickType, ActionSet> map = null;

        for (ClickType clickType : ClickType.values()) {
            String s = clickType.name().toLowerCase();
            if (c.contains("RewardType_" + s)) {

                if (map == null) {
                    map = new HashMap<>();
                }

                String priceType  = c.getString("PriceType_" + s);
                String rewardType = c.getString("RewardType_" + s);
                String message    = c.getString("Message_" + s);
                String permission = c.getString("ExtraPermission_" + s);
                if (permission == null || permission == "") {
                    permission = null;
                }

                GenesisRewardType rewardT = GenesisRewardType.detectType(rewardType);
                GenesisPriceType  priceT  = GenesisPriceType.detectType(priceType);

                if (rewardT == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Was not able to create advanced BuyItem '" + event.getShopItemName() + "'! '"
                                    + rewardType
                                    + "' is not a valid RewardType! Switching back to simple kind of BuyItem.");
                    ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
                    for (GenesisRewardType type : GenesisRewardType.values()) {
                        ClassManager.manager.getBugFinder().severe("-" + type.name());
                    }
                    return;
                }

                if (priceT == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Was not able to create advanced BuyItem '" + event.getShopItemName() + "!' '"
                                    + priceType
                                    + "' is not a valid PriceType! Switching back to simple kind of BuyItem.");
                    ClassManager.manager.getBugFinder().severe("Valid PriceTypes:");
                    for (GenesisPriceType type : GenesisPriceType.values()) {
                        ClassManager.manager.getBugFinder().severe("-" + type.name());
                    }
                    return;
                }

                rewardT.enableType();
                priceT.enableType();

                Object price  = c.get("Price_" + s);
                Object reward = c.get("Reward_" + s);


                price = priceT.createObject(price, true);
                reward = rewardT.createObject(reward, true);

                if (!priceT.validityCheck(event.getShopItemName(), price)) {
                    return;
                }
                if (!rewardT.validityCheck(event.getShopItemName(), reward)) {
                    return;
                }


                String           inputTypename = c.getString("ForceInput_" + s);
                String           inputtext     = c.getString("ForceInputMessage_" + s);
                GenesisInputType inputType     = null;
                if (inputTypename != null) {
                    for (GenesisInputType it : GenesisInputType.values()) {
                        if (it.name().equalsIgnoreCase(inputTypename)) {
                            inputType = it;
                            break;
                        }
                    }
                    if (inputType == null) {
                        ClassManager.manager.getBugFinder()
                                .warn("Invalid advanced ForceInput type: '" + inputTypename + "' of shopitem '"
                                        + event.getShopItemName());
                    }
                }


                map.put(clickType,
                        new ActionSet(rewardT, priceT, reward, price, message, permission, inputType, inputtext));

            }
        }


        GenesisBuyAdvanced buy = new GenesisBuyAdvanced(event.getRewardType(),
                event.getPriceType(),
                event.getReward(),
                event.getPrice(),
                event.getMessage(),
                event.getInventoryLocation(),
                event.getExtraPermission(),
                event.getShopItemName(),
                event.getCondition(),
                event.getInputType(),
                event.getInputText(),
                map);
        event.useCustomShopItem(buy);

    }


    @EventHandler
    public void onCheckStringForFeatures(GenesisCheckStringForFeaturesEvent event) {
        if (event.getShopItem() instanceof GenesisBuyAdvanced) {
            for (ClickType clickType : ClickType.values()) {
                String s = clickType.name().toLowerCase();
                if (event.getText().contains("%price_" + s + "%")) {
                    if (event.getShopItem().getPriceType(clickType).isPlayerDependend(event.getShopItem(), clickType)) {
                        event.approveFeature();
                        return;
                    }
                }
                if (event.getText().contains("%reward_" + s + "%")) {
                    if (event.getShopItem()
                            .getRewardType(clickType)
                            .isPlayerDependend(event.getShopItem(), clickType)) {
                        event.approveFeature();
                        return;
                    }
                }
            }
        }
    }

}
