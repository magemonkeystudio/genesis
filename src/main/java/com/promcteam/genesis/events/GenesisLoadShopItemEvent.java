package com.promcteam.genesis.events;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class GenesisLoadShopItemEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final GenesisShop          shop;
    private final String               shopItemName;
    private final ConfigurationSection configurationSection;

    private GenesisBuy customShopItem;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void useCustomShopItem(GenesisBuy buy) {
        this.customShopItem = buy;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}