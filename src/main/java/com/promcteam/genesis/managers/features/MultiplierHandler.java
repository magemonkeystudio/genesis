package com.promcteam.genesis.managers.features;

import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisMultiplier;
import com.promcteam.genesis.core.prices.GenesisPriceType;
import com.promcteam.genesis.core.rewards.GenesisRewardType;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.misc.MathTools;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class MultiplierHandler {
    private final Set<GenesisMultiplier> multipliers = new HashSet<>();

    private List<String> lines;

    public MultiplierHandler(Genesis plugin) {
        if (!plugin.getConfig().getBoolean("MultiplierGroups.Enabled")) {
            return;
        }
        lines = plugin.getConfig().getStringList("MultiplierGroups.List");
        setup();
    }

    public void setup() {
        multipliers.clear();
        for (String s : lines) {
            GenesisMultiplier m = new GenesisMultiplier(s);
            if (m.isValid()) {
                multipliers.add(m);
            }
        }
    }


    public String calculatePriceDisplayWithMultiplier(Player p,
                                                      GenesisBuy buy,
                                                      ClickType clickType,
                                                      double d,
                                                      String message) {
        GenesisPriceType t = buy.getPriceType(clickType);
        return calculatePriceDisplayWithMultiplier(p,
                buy,
                clickType,
                d,
                message,
                MathTools.getFormatting(t),
                MathTools.isIntegerValue(t));
    }

    public String calculatePriceDisplayWithMultiplier(Player p,
                                                      GenesisBuy buy,
                                                      ClickType clickType,
                                                      double d,
                                                      String message,
                                                      List<String> formatting,
                                                      boolean integerValue) {
        d = calculatePriceWithMultiplier(p, buy, clickType, d);

        if (buy.getRewardType(clickType) == GenesisRewardType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i     = (ItemStack) buy.getReward(clickType);
                int       count = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, i);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler()
                            .get("Display.ItemAllEach")
                            .replace("%value%",
                                    message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler()
                        .get("Display.ItemAllEach")
                        .replace("%value%",
                                message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue));
    }

    public double calculatePriceWithMultiplier(Player p, GenesisBuy buy, ClickType clickType, double d) {
        return calculatePriceWithMultiplier(p, buy, buy.getPriceType(clickType), d);
    }

    public double calculatePriceWithMultiplier(Player p,
                                               GenesisBuy buy,
                                               GenesisPriceType priceType,
                                               double d) { //Used for prices
        for (GenesisMultiplier m : multipliers) {
            if (m.isAcceptedShopItem(buy)) {
                d = m.calculateValue(p, priceType, d, GenesisMultiplier.RANGE_PRICE_ONLY);
            }
        }
        return MathTools.round(d, 2);
    }

    public String calculateRewardDisplayWithMultiplier(Player p,
                                                       GenesisBuy buy,
                                                       ClickType clickType,
                                                       double d,
                                                       String message) {
        GenesisPriceType t = GenesisPriceType.detectType(buy.getRewardType(clickType).name());
        return calculateRewardDisplayWithMultiplier(p,
                buy,
                clickType,
                d,
                message,
                MathTools.getFormatting(t),
                MathTools.isIntegerValue(t));
    }

    public String calculateRewardDisplayWithMultiplier(Player p,
                                                       GenesisBuy buy,
                                                       ClickType clickType,
                                                       double d,
                                                       String message,
                                                       List<String> formatting,
                                                       boolean integerValue) {
        d = calculateRewardWithMultiplier(p, buy, clickType, d);

        if (buy.getPriceType(clickType) == GenesisPriceType.ItemAll) {
            if (ClassManager.manager.getSettings().getItemAllShowFinalReward() && p != null) {
                ItemStack i     = (ItemStack) buy.getPrice(clickType);
                int       count = ClassManager.manager.getItemStackChecker().getAmountOfSameItems(p, i, buy);

                if (count == 0) {
                    return ClassManager.manager.getMessageHandler()
                            .get("Display.ItemAllEach")
                            .replace("%value%",
                                    message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
                }

                d *= count;
            } else {
                return ClassManager.manager.getMessageHandler()
                        .get("Display.ItemAllEach")
                        .replace("%value%",
                                message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue)));
            }
        }

        return message.replace("%number%", MathTools.displayNumber(d, formatting, integerValue));
    }

    public double calculateRewardWithMultiplier(Player p,
                                                GenesisBuy buy,
                                                ClickType clickType,
                                                double d) { //Used for reward; Works the other way around
        return calculateRewardWithMultiplier(p, buy, buy.getRewardType(clickType), d);
    }

    public double calculateRewardWithMultiplier(Player p,
                                                GenesisBuy buy,
                                                GenesisRewardType rewardtype,
                                                double d) { //Used for reward; Works the other way around
        for (GenesisMultiplier m : multipliers) {
            if (m.isAcceptedShopItem(buy)) {
                d = m.calculateValue(p,
                        GenesisPriceType.detectType(rewardtype.name()),
                        d,
                        GenesisMultiplier.RANGE_REWARD_ONLY);
            }
        }
        return MathTools.round(d, 2);
    }

    public boolean hasMultipliers() {
        return !multipliers.isEmpty();
    }
}
