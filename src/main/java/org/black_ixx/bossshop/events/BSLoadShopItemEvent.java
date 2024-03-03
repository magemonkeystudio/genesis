package org.black_ixx.bossshop.events;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class BSLoadShopItemEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop               shop;
    private final String               shopItemName;
    private final ConfigurationSection configurationSection;

    private BSBuy customShopItem;

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