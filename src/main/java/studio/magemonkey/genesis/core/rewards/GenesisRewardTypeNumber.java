package studio.magemonkey.genesis.core.rewards;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.managers.ClassManager;

public abstract class GenesisRewardTypeNumber extends GenesisRewardType {


    public abstract boolean isIntegerValue();

    @Override
    public boolean isPlayerDependend(GenesisBuy buy, ClickType clickType) {
        return super.isPlayerDependend(buy, clickType) || (buy.getPriceType(clickType) == GenesisPriceType.ItemAll
                && ClassManager.manager.getSettings().getItemAllShowFinalReward());
    }

    @Override
    public boolean supportsMultipliers() {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        giveReward(p, buy, reward, clickType, 1);
    }

    public abstract void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType, int multiplier);

}
