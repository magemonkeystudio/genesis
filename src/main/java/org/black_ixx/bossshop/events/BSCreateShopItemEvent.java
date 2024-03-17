package org.black_ixx.bossshop.events;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.conditions.BSCondition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;


@Getter
@RequiredArgsConstructor
public class BSCreateShopItemEvent extends BSEvent {
    private static final HandlerList handlers = new HandlerList();

    private final BSShop               shop;
    private final String               shopItemName;
    private final ConfigurationSection configurationSection;
    private final BSRewardType         rewardType;
    private final BSPriceType          priceType;
    private final Object               reward, price;
    private final String      message;
    private final int         inventoryLocation;
    private final String      extraPermission;
    private final BSCondition condition;
    private final BSInputType inputType;
    private final String      inputText;
    private       BSBuy       customShopItem;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void useCustomShopItem(BSBuy buy) {
        this.customShopItem = buy;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}