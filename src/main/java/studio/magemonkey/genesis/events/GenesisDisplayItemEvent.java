package studio.magemonkey.genesis.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;

public class GenesisDisplayItemEvent extends GenesisEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    private final GenesisBuy buy;

    private final GenesisShop shop;

    private final Inventory inventory;

    private boolean cancelled = false;

    public GenesisDisplayItemEvent(Player player, GenesisShop shop, GenesisBuy buy, Inventory inventory) {
        this.player = player;
        this.buy = buy;
        this.shop = shop;
        this.inventory = inventory;
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

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}