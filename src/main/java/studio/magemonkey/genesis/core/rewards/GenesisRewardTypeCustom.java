package studio.magemonkey.genesis.core.rewards;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisCustomLink;
import studio.magemonkey.genesis.managers.ClassManager;

public class GenesisRewardTypeCustom extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return o; // Because nothing is known about the custom reward type
    }

    public boolean validityCheck(String itemName, Object o) { // Because nothing is known about the custom reward type
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
        GenesisCustomLink link = (GenesisCustomLink) reward;
        link.doAction(p);
    }

    @Override
    public String[] createNames() {
        return new String[]{"custom"};
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Custom");
    }


    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }


}
