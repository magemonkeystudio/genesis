package studio.magemonkey.genesis.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.managers.features.PageLayoutHandler;

public class GenesisChoosePageLayoutEvent extends GenesisEvent {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final GenesisShop       shop;
    private final String            name;
    @Getter
    @Setter
    private       PageLayoutHandler layout;


    public GenesisChoosePageLayoutEvent(GenesisShop shop, String name, PageLayoutHandler layout) {
        this.shop = shop;
        this.name = name;
        this.layout = layout;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getShopItemName() {
        return name;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}