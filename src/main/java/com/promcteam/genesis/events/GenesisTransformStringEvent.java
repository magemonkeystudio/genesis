package com.promcteam.genesis.events;

import com.promcteam.genesis.core.GenesisShop;
import com.promcteam.genesis.core.GenesisShopHolder;
import com.promcteam.genesis.core.GenesisBuy;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class GenesisTransformStringEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private       String            text;
    private final Player            target;
    private final GenesisShop       shop;
    private final GenesisBuy        buy;
    private final GenesisShopHolder holder;


    public GenesisTransformStringEvent(String text,
                                       GenesisBuy item,
                                       GenesisShop shop,
                                       GenesisShopHolder holder,
                                       Player target) {
        this.text = text;
        this.buy = item;
        this.shop = shop;
        this.holder = holder;
        this.target = target;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Player getTarget() {
        return target;
    }

    public GenesisShop getShop() {
        return shop;
    }

    public GenesisBuy getShopItem() {
        return buy;
    }

    public GenesisShopHolder getShopHolder() {
        return holder;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}