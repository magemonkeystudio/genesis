package studio.magemonkey.genesis.events;


import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class GenesisCreatedShopItemEvent extends GenesisEvent {

    private static final HandlerList handlers = new HandlerList();


    private final GenesisShop          shop;
    private final GenesisBuy           item;
    private final ConfigurationSection section;


    public GenesisCreatedShopItemEvent(GenesisShop shop, GenesisBuy item, ConfigurationSection section) {
        this.shop = shop;
        this.item = item;
        this.section = section;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    public GenesisBuy getShopItem() {
        return item;
    }

    public GenesisShop getShop() {
        return shop;
    }

    public void putSpecialInformation(Plugin plugin, Object information) {
        item.putSpecialInformation(plugin, information);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}