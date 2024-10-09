package studio.magemonkey.genesis.core.rewards;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;


public class GenesisRewardTypeExp extends GenesisRewardTypeNumber {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.getInt(o, -1);
    }

    public boolean validityCheck(String itemName, Object o) {
        if ((Integer) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a valid Integer number. Example: '7' or '12'.");
        return false;
    }

    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType, int multiplier) {
        int exp = (int) ClassManager.manager.getMultiplierHandler()
                .calculateRewardWithMultiplier(p, buy, clickType, ((Integer) reward)) * multiplier;
        if (ClassManager.manager.getSettings().getExpUseLevel()) {
            p.setLevel(p.getLevel() + exp);
        } else {
            p.giveExp(exp);
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler()
                .calculateRewardDisplayWithMultiplier(p,
                        buy,
                        clickType,
                        ((Integer) reward),
                        ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", "%number%"));
    }

    @Override
    public String[] createNames() {
        return new String[]{"exp", "xp"};
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
