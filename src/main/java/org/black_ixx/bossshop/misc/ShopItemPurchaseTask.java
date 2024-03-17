package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class ShopItemPurchaseTask implements Runnable {


    private UUID                uuid;
    private BSBuy               buy;
    private BSShop              shop;
    private BSShopHolder        holder;
    private ClickType           clickType;
    private BSRewardType        rewardtype;
    private BSPriceType         priceType;
    private InventoryClickEvent event;

    public ShopItemPurchaseTask(Player p,
                                BSBuy buy,
                                BSShop shop,
                                BSShopHolder holder,
                                ClickType clickType,
                                BSRewardType rewardtype,
                                BSPriceType priceType,
                                InventoryClickEvent event) {
        this.uuid = p.getUniqueId();
        this.buy = buy;
        this.shop = shop;
        this.holder = holder;
        this.clickType = clickType;
        this.rewardtype = rewardtype;
        this.priceType = priceType;
        this.event = event;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        Player p = Bukkit.getPlayer(uuid);

        if (p != null) {
            buy.purchase(p,
                    shop,
                    holder,
                    clickType,
                    rewardtype,
                    priceType,
                    event,
                    ClassManager.manager.getPlugin(),
                    true);
        }
    }

}
