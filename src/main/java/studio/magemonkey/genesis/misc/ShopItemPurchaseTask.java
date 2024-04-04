package studio.magemonkey.genesis.misc;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class ShopItemPurchaseTask implements Runnable {


    private final UUID                uuid;
    private final GenesisBuy          buy;
    private final GenesisShop         shop;
    private final GenesisShopHolder   holder;
    private final ClickType           clickType;
    private final GenesisRewardType   rewardtype;
    private final GenesisPriceType    priceType;
    private final InventoryClickEvent event;

    public ShopItemPurchaseTask(Player p,
                                GenesisBuy buy,
                                GenesisShop shop,
                                GenesisShopHolder holder,
                                ClickType clickType,
                                GenesisRewardType rewardtype,
                                GenesisPriceType priceType,
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
