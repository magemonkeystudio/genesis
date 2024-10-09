package studio.magemonkey.genesis.core.prices;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.misc.MathTools;

public class GenesisPriceTypePoints extends GenesisPriceTypeNumber {
    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String itemName, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The price object needs to be a valid number. Example: '7' or '12'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setPointsEnabled(true);
    }

    @Override
    public boolean hasPrice(Player p,
                            GenesisBuy buy,
                            Object price,
                            ClickType clickType,
                            int multiplier,
                            boolean messageOnFailure) {
        double points = ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        if (ClassManager.manager.getPointsManager().getPoints(p) < points) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Points", p);
            }
            return false;
        }
        return true;
    }

    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType, int multiplier) {
        double points = ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Double) price) * multiplier;
        ClassManager.manager.getPointsManager().takePoints(p, points);

        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        double balancePoints = ClassManager.manager.getPointsManager().getPoints(p);
        return ClassManager.manager.getMessageHandler()
                .get("Display.Points")
                .replace("%points%", MathTools.displayNumber(balancePoints, Points));
    }

    @Override
    public String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        String newPoints = ClassManager.manager.getMultiplierHandler()
                .calculatePriceDisplayWithMultiplier(p,
                        buy,
                        clickType,
                        (Double) price,
                        ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", "%number%"));
        double oldPoints = (double) buy.getPrice(clickType);
        return GenesisPriceTypeMoney.getRenewedFormat(newPoints,
                MathTools.displayNumber(oldPoints, MathTools.getFormatting(this), isIntegerValue()));
    }

    @Override
    public String[] createNames() {
        return new String[]{"points", "point"};
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
