package studio.magemonkey.genesis.events;


import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisInputType;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.core.conditions.GenesisCondition;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;


@Getter
@RequiredArgsConstructor
public class GenesisCreateShopItemEvent extends GenesisEvent {
    private static final HandlerList handlers = new HandlerList();

    private final GenesisShop          shop;
    private final String               shopItemName;
    private final ConfigurationSection configurationSection;
    private final GenesisRewardType    rewardType;
    private final GenesisPriceType     priceType;
    private final Object               reward, price;
    private final String           message;
    private final int              inventoryLocation;
    private final String           extraPermission;
    private final GenesisCondition condition;
    private final GenesisInputType inputType;
    private final String           inputText;
    private       GenesisBuy       customShopItem;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void useCustomShopItem(GenesisBuy buy) {
        this.customShopItem = buy;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}