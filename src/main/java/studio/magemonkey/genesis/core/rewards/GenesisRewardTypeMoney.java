package studio.magemonkey.genesis.core.rewards;


import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class GenesisRewardTypeMoney extends GenesisRewardTypeNumber {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.getDouble(o, -1);
    }

    public boolean validityCheck(String itemName, Object o) {
        if ((Double) o != -1) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a valid number. Example: '4.0' or '10'.");
        return false;
    }

    public void enableType() {
        ClassManager.manager.getSettings().setMoneyEnabled(true);
        ClassManager.manager.getSettings().setVaultEnabled(true);
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType, int multiplier) {
        double money = ClassManager.manager.getMultiplierHandler()
                .calculateRewardWithMultiplier(p, buy, clickType, (double) reward) * multiplier;

        if (ClassManager.manager.getVaultHandler() == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Unable to give " + p.getName() + " his/her money: Vault manager not loaded. Property: "
                            + ClassManager.manager.getSettings().getVaultEnabled());
            return;
        }
        if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Unable to give " + p.getName() + " his/her money: Economy manager not loaded. Property: "
                            + ClassManager.manager.getSettings().getMoneyEnabled());
            return;
        }

        if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
            ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
            ClassManager.manager.getBugFinder()
                    .severe("Unable to give " + p.getName()
                            + " his/her money: He/She does not have an economy account.");
            return;
        }

        ClassManager.manager.getVaultHandler().getEconomy().depositPlayer(p.getName(), money);
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMultiplierHandler()
                .calculateRewardDisplayWithMultiplier(p,
                        buy,
                        clickType,
                        (double) reward,
                        ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", "%number%"));
    }

    @Override
    public String[] createNames() {
        return new String[]{"money"};
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean isIntegerValue() {
        return false;
    }

    @Override
    public boolean allowAsync() {
        return true;
    }


}
