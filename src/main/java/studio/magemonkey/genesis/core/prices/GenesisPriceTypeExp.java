package studio.magemonkey.genesis.core.prices;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.misc.MathTools;

public class GenesisPriceTypeExp extends GenesisPriceTypeNumber {

    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.getInt(o, -1);
    }

    public boolean validityCheck(String itemName, Object o) {
        if ((Integer) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The price object needs to be a valid Integer number. Example: '10' or '30'.");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p,
                            GenesisBuy buy,
                            Object price,
                            ClickType clickType,
                            int multiplier,
                            boolean messageOnFailure) {
        int exp = (int) ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Integer) price) * multiplier;
        if ((p.getLevel() < exp)) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Exp", p);
            }
            return false;
        }
        return true;
    }

    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType, int multiplier) {
        int exp = (int) ClassManager.manager.getMultiplierHandler()
                .calculatePriceWithMultiplier(p, buy, clickType, (Integer) price) * multiplier;
        p.setLevel(p.getLevel() - exp);

        return getDisplayBalance(p, buy, price, clickType);
    }

    @Override
    public String getDisplayBalance(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        int balanceExp = p.getLevel();
        return ClassManager.manager.getMessageHandler()
                .get("Display.Exp")
                .replace("%levels%", MathTools.displayNumber(balanceExp, Exp));
    }

    @Override
    public String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler()
                .calculatePriceDisplayWithMultiplier(p,
                        buy,
                        clickType,
                        (Integer) price,
                        ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", "%number%"));
    }


    @Override
    public String[] createNames() {
        return new String[]{"exp", "xp", "level", "levels"};
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean isIntegerValue() {
        return true;
    }


}
