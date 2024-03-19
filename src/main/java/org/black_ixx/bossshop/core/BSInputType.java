package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.userinput.BSUserInput;
import org.black_ixx.bossshoppro.folia.CrossScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public enum BSInputType {

    PLAYER {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p,
                               final BSShop shop,
                               final BSBuy buy,
                               final BSShopHolder holder,
                               final ClickType clickType,
                               final BSRewardType rewardtype,
                               final BSPriceType priceType,
                               final InventoryClickEvent event,
                               final BossShop plugin) {

            new BSUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    if (Bukkit.getServer().getPlayer(text) == null) {
                        ClassManager.manager.getMessageHandler()
                                .sendMessage("Main.PlayerNotFound", p, text, null, shop, holder, buy);
                        shop.openInventory(p, holder.getPage(), true);
                        return;
                    }
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    CrossScheduler.callSyncMethod(() -> {
                        buy.purchase(p, shop, holder, clickType, rewardtype, priceType, event, plugin, false);
                        return true;
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clickType));


        }
    },
    TEXT {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p,
                               final BSShop shop,
                               final BSBuy buy,
                               final BSShopHolder holder,
                               final ClickType clickType,
                               final BSRewardType rewardtype,
                               final BSPriceType priceType,
                               final InventoryClickEvent event,
                               final BossShop plugin) {

            new BSUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    CrossScheduler.callSyncMethod(() -> {
                        buy.purchase(p, shop, holder, clickType, rewardtype, priceType, event, plugin, false);
                        return true;
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clickType));

        }
    };


    public abstract void forceInput(final Player p,
                                    final BSShop shop,
                                    final BSBuy buy,
                                    final BSShopHolder holder,
                                    final ClickType clickType,
                                    final BSRewardType rewardtype,
                                    final BSPriceType priceType,
                                    final InventoryClickEvent event,
                                    final BossShop plugin);


}
