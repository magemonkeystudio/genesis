package studio.magemonkey.genesis.events;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;
import lombok.Getter;
import org.bukkit.event.HandlerList;

public class GenesisCheckStringForFeaturesEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    @Getter
    private final String      text;
    @Getter
    private final GenesisShop shop;
    private final GenesisBuy  buy;

    private boolean containsFeature;


    public GenesisCheckStringForFeaturesEvent(String text, GenesisBuy item, GenesisShop shop) {
        this.text = text;
        this.buy = item;
        this.shop = shop;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GenesisBuy getShopItem() {
        return buy;
    }

    public void approveFeature() {
        containsFeature = true;
    }

    public boolean containsFeature() {
        return containsFeature;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}