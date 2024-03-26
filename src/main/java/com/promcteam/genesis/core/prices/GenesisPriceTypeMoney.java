package com.promcteam.genesis.core.prices;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import com.promcteam.genesis.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GenesisPriceTypeMoney extends GenesisPriceTypeNumber {

    public static String renewedFormat =
            ClassManager.manager.getPlugin().getConfig().getString("MultiplierGroups.RenewedPriceFormat");

    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String itemName, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The price object needs to be a valid number. Example: '4.0' or '10'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setMoneyEnabled(true);
        ClassManager.manager.getSettings().setVaultEnabled(true);
    }

    public static String getRenewedFormat(String newValue, String oldValue) {
        String reducedOldPrice = MathTools.removeNonNumeric(oldValue);
        String reducedNewPrice = MathTools.removeNonNumeric(newValue);
        return reducedNewPrice.equals(reducedOldPrice) ? newValue
                : renewedFormat.replace("%oldValue%", newValue.replace(reducedNewPrice, reducedOldPrice))
                        .replace("%newValue%", newValue);
    }

    @Override
    public boolean hasPrice(Player p,
                            GenesisBuy buy,
                            Object price,
                            ClickType clickType,
                            int multiplier,
                            boolean messageOnFailure) {
        double money = ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        if (ClassManager.manager.getVaultHandler() == null) {
            return false;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
            return false;
        }
        if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
            }
            return false;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName()) < money) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Money", p);
            }
            return false;
        }

        return true;
    }

    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType, int multiplier) {
        double money = ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;

        if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            ClassManager.manager.getBugFinder()
                    .severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + money + ")");
            return "";
        }

        ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), money);
        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        double balance = ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
        return ClassManager.manager.getMessageHandler()
                .get("Display.Money")
                .replace("%money%", MathTools.displayNumber(balance, Money));
    }

    @Override
    public String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        // TODO might need a better handling in future. I think we could skip a lot of the 'calculatePriceDisplayWithMultiplier' depth
        String newPrice = ClassManager.manager.getMultiplierHandler().calculatePriceDisplayWithMultiplier(p,
                buy,
                clickType,
                (Double) price,
                ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", "%number%"));
        double oldPrice = (double) buy.getPrice(clickType);
        return getRenewedFormat(newPrice,
                MathTools.displayNumber(oldPrice, MathTools.getFormatting(this), isIntegerValue()));
    }

    @Override
    public String[] createNames() {
        return new String[]{"money"};
    }


    @Override
    public boolean isIntegerValue() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }


}
