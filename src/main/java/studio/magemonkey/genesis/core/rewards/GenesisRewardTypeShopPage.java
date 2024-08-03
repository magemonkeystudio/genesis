package studio.magemonkey.genesis.core.rewards;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import studio.magemonkey.genesis.api.InventoryUtil;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;


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
        if (page == -1) return;

        Inventory         inventory = InventoryUtil.getTopInventory(p.getOpenInventory());
        GenesisShopHolder holder    = (GenesisShopHolder) inventory.getHolder();
        if (holder != null) {
            holder.getShop()
                    .updateInventory(inventory,
                            holder,
                            p,
                            ClassManager.manager,
                            page,
                            holder.getHighestPage(),
                            false);
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        int page = calculatePage(p, (String) reward);
        return ClassManager.manager.getMessageHandler().get("Display.Page").replace("%page%", String.valueOf(page + 1));
    }


    private int calculatePage(Player p, String reward) {
        Inventory       inventory     = InventoryUtil.getTopInventory(p.getOpenInventory());
        InventoryHolder holder        = inventory.getHolder();

        if (holder instanceof GenesisShopHolder) {
            GenesisShopHolder shopHolder = (GenesisShopHolder) holder;
            if (reward.equalsIgnoreCase("next") || reward.equalsIgnoreCase("+")) {
                return Math.min(shopHolder.getPage() + 1, shopHolder.getHighestPage());
            }
            if (reward.equalsIgnoreCase("previous") || reward.equalsIgnoreCase("-")) {
                return Math.max(shopHolder.getPage() - 1, 0);
            }

            try {
                int page = Math.max(0, Math.min(Integer.parseInt(reward), shopHolder.getHighestPage()));
                return page - 1;
            } catch (NumberFormatException e) {
                ClassManager.manager.getBugFinder()
                        .warn("Was not able to detect shop page. Unable to read Reward '" + reward
                                + "'. Please use either 'next', 'previous' or a page number like '1' or '2'.");
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
