package studio.magemonkey.genesis.core.rewards;


import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GenesisRewardTypeClose extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return null;
    }

    public boolean validityCheck(String itemName, Object o) { // Here can't be any mistakes
        return true;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        p.closeInventory();
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Close");
    }

    @Override
    public String[] createNames() {
        return new String[]{"close"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean isActualReward() {
        return false;
    }

}
