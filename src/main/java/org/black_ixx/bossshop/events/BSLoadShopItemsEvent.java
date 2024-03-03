package org.black_ixx.bossshop.events;


import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.bukkit.event.HandlerList;


public class BSLoadShopItemsEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    private final BSShop  shop;
    private final BSShops shopHandler;


    public BSLoadShopItemsEvent(BSShops shopHandler, BSShop shop) {
        this.shop = shop;
        this.shopHandler = shopHandler;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BSShop getShop() {
        return shop;
    }

    public BSShops getShopHandler() {
        return shopHandler;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}