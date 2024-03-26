package com.promcteam.genesis.events;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShop;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

public class GenesisPlayerPurchasedEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player      player;
    private final GenesisBuy  buy;
    private final GenesisShop shop;
    private final ClickType   clickType;


    public GenesisPlayerPurchasedEvent(Player player, GenesisShop shop, GenesisBuy buy, ClickType clickType) {
        this.player = player;
        this.buy = buy;
        this.shop = shop;
        this.clickType = clickType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public GenesisBuy getShopItem() {
        return buy;
    }

    public GenesisShop getShop() {
        return shop;
    }

    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}