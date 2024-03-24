package com.promcteam.genesis.events;

import org.bukkit.event.HandlerList;

public class GenesisRegisterTypesEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    public GenesisRegisterTypesEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}