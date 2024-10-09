package studio.magemonkey.genesis.events;

import org.bukkit.event.HandlerList;
import studio.magemonkey.genesis.Genesis;

public class GenesisReloadedEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final Genesis plugin;


    public GenesisReloadedEvent(Genesis plugin) {
        this.plugin = plugin;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Genesis getGenesis() {
        return plugin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}