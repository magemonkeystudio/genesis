package com.promcteam.genesis.core.rewards;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;


public class GenesisRewardTypeShopPage extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readString(o, true);
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be either 'next', 'previous' or a page number like '0' (first page), '1' or '2'.");
        return false;
    }

    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        int page = calculatePage(p, (String) reward);

        if (page != -1) {
            Inventory         inventory = p.getOpenInventory().getTopInventory();
            GenesisShopHolder holder    = (GenesisShopHolder) inventory.getHolder();
            holder.getShop()
                    .updateInventory(inventory, holder, p, ClassManager.manager, page, holder.getHighestPage(), false);
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        int page = calculatePage(p, (String) reward);
        return ClassManager.manager.getMessageHandler().get("Display.Page").replace("%page%", String.valueOf(page + 1));
    }


    private int calculatePage(Player p, String reward) {
        InventoryView inventoryview = p.getOpenInventory();
        if (inventoryview != null) {
            Inventory       inventory = inventoryview.getTopInventory();
            InventoryHolder holder    = inventory.getHolder();

            if (holder instanceof GenesisShopHolder) {
                GenesisShopHolder shopholder = (GenesisShopHolder) holder;
                if (reward.equalsIgnoreCase("next") || reward.equalsIgnoreCase("+")) {
                    int page = Math.min(shopholder.getPage() + 1, shopholder.getHighestPage());
                    return page;
                }
                if (reward.equalsIgnoreCase("previous") || reward.equalsIgnoreCase("-")) {
                    int page = Math.max(shopholder.getPage() - 1, 0);
                    return page;
                }

                try {
                    int page = Math.max(0, Math.min(Integer.valueOf(reward), shopholder.getHighestPage()));
                    return page - 1;

                } catch (NumberFormatException e) {
                    ClassManager.manager.getBugFinder()
                            .warn("Was not able to detect shop page. Unable to read Reward '" + reward
                                    + "'. Please use either 'next', 'previous' or a page number like '1' or '2'.");
                }

            }
        }
        return -1;
    }


    @Override
    public String[] createNames() {
        return new String[]{"shoppage", "page", "openpage"};
    }

    public boolean logTransaction() {
        return false;
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean isPlayerDependend(GenesisBuy buy, ClickType clickType) {
        return true;
    }

    @Override
    public boolean isActualReward() {
        return false;
    }

}
