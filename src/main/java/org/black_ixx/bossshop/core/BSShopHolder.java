package org.black_ixx.bossshop.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class BSShopHolder implements InventoryHolder {

    @Getter
    private       BSShopHolder previousShopHolder;
    @Getter
    private final BSShop       shop;
    @Getter
    @Setter
    private       int          page, highestPage;
    private Map<Integer, BSBuy> items;

    public BSShopHolder(BSShop shop, HashMap<Integer, BSBuy> items) {
        this.shop = shop;
        this.items = items;
    }

    public BSShopHolder(BSShop shop, BSShopHolder previousShopHolder) {
        this(shop);
        this.previousShopHolder = previousShopHolder;
    }

    public BSShopHolder(BSShop shop) {
        this.shop = shop;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public BSBuy getShopItem(int i) {
        return items.get(i);
    }

    public int getSlot(BSBuy buy) {
        for (int slot : items.keySet()) {
            BSBuy value = items.get(slot);
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

    public void setItems(Map<Integer, BSBuy> items, int page, int highestPage) {
        this.items = items;
        this.page = page;
        this.highestPage = highestPage;
    }
}
