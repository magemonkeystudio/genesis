package studio.magemonkey.genesis.core;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.misc.userinput.GenesisUserInput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.concurrent.Callable;

public enum GenesisInputType {

    PLAYER {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p,
                               final GenesisShop shop,
                               final GenesisBuy buy,
                               final GenesisShopHolder holder,
                               final ClickType clickType,
                               final GenesisRewardType rewardtype,
                               final GenesisPriceType priceType,
                               final InventoryClickEvent event,
                               final Genesis plugin) {

            new GenesisUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    if (Bukkit.getServer().getPlayer(text) == null) {
                        ClassManager.manager.getMessageHandler()
                                .sendMessage("Main.PlayerNotFound", p, text, null, shop, holder, buy);
                        shop.openInventory(p, holder.getPage(), true);
                        return;
                    }
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            buy.purchase(p, shop, holder, clickType, rewardtype, priceType, event, plugin, false);
                            return true;
                        }
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clickType));


        }
    },
    TEXT {
        @Override
        @SuppressWarnings("deprecation")
        public void forceInput(final Player p,
                               final GenesisShop shop,
                               final GenesisBuy buy,
                               final GenesisShopHolder holder,
                               final ClickType clickType,
                               final GenesisRewardType rewardtype,
                               final GenesisPriceType priceType,
                               final InventoryClickEvent event,
                               final Genesis plugin) {

            new GenesisUserInput() {
                @Override
                public void receivedInput(final Player p, String text) {
                    ClassManager.manager.getPlayerDataHandler().enteredInput(p, text);
                    Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            buy.purchase(p, shop, holder, clickType, rewardtype, priceType, event, plugin, false);
                            return true;
                        }
                    });
                }
            }.getUserInput(p, null, null, buy.getInputText(clickType));

        }
    };


    public abstract void forceInput(final Player p,
                                    final GenesisShop shop,
                                    final GenesisBuy buy,
                                    final GenesisShopHolder holder,
                                    final ClickType clickType,
                                    final GenesisRewardType rewardtype,
                                    final GenesisPriceType priceType,
                                    final InventoryClickEvent event,
                                    final Genesis plugin);


}
