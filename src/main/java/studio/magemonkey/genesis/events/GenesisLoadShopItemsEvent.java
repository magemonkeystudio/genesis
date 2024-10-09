package studio.magemonkey.genesis.events;


import org.bukkit.event.HandlerList;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.core.GenesisShops;


public class GenesisLoadShopItemsEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final GenesisShop  shop;
    private final GenesisShops shopHandler;


    public GenesisLoadShopItemsEvent(GenesisShops shopHandler, GenesisShop shop) {
        this.shop = shop;
        this.shopHandler = shopHandler;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GenesisShop getShop() {
        return shop;
    }

    public GenesisShops getShopHandler() {
        return shopHandler;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}