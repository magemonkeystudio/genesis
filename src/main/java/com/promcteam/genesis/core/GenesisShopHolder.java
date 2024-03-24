package com.promcteam.genesis.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class GenesisShopHolder implements InventoryHolder {

    @Getter
    private       GenesisShopHolder previousShopHolder;
    @Getter
    private final GenesisShop       shop;
    @Getter
    @Setter
    private       int               page, highestPage;
    private Map<Integer, GenesisBuy> items;

    public GenesisShopHolder(GenesisShop shop, HashMap<Integer, GenesisBuy> items) {
        this.shop = shop;
        this.items = items;
    }

    public GenesisShopHolder(GenesisShop shop, GenesisShopHolder previousShopHolder) {
        this(shop);
        this.previousShopHolder = previousShopHolder;
    }

    public GenesisShopHolder(GenesisShop shop) {
        this.shop = shop;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public GenesisBuy getShopItem(int i) {
        return items.get(i);
    }

    public int getSlot(GenesisBuy buy) {
        for (int slot : items.keySet()) {
            GenesisBuy value = items.get(slot);
            if (value == buy) {
                return slot;
            }
        }
        return -1;
    }


    public int getDisplayPage() {
        return page + 1;
    }

    public int getDisplayHighestPage() {
        return highestPage + 1;
    }

    public void setItems(Map<Integer, GenesisBuy> items, int page, int highestPage) {
        this.items = items;
        this.page = page;
        this.highestPage = highestPage;
    }
}
