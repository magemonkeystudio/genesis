package org.black_ixx.bossshop.events;

import lombok.Getter;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.event.HandlerList;

public class BSCheckStringForFeaturesEvent extends BSEvent {

    private static final HandlerList handlers = new HandlerList();


    @Getter
    private String text;
    @Getter
    private BSShop shop;
    private BSBuy  buy;

    private boolean containsFeature;


    public BSCheckStringForFeaturesEvent(String text, BSBuy item, BSShop shop) {
        this.text = text;
        this.buy = item;
        this.shop = shop;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BSBuy getShopItem() {
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