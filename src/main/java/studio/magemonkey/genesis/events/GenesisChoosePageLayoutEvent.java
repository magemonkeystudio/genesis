package studio.magemonkey.genesis.events;


import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.managers.features.PageLayoutHandler;
import org.bukkit.event.HandlerList;


public class GenesisChoosePageLayoutEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final GenesisShop       shop;
    private final String            name;
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

    public GenesisShop getShop() {
        return shop;
    }

    public PageLayoutHandler getLayout() {
        return layout;
    }

    public void setLayout(PageLayoutHandler layout) {
        this.layout = layout;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}