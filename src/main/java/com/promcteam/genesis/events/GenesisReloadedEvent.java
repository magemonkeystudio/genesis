package com.promcteam.genesis.events;

import com.promcteam.genesis.Genesis;
import org.bukkit.event.HandlerList;

public class GenesisReloadedEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final Genesis plugin;


    public GenesisReloadedEvent(Genesis plugin) {
        this.plugin = plugin;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Genesis getBossShop() {
        return plugin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}