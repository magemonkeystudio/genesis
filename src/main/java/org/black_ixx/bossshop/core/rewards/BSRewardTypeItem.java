package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BSRewardTypeItem extends BSRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        if (forceFinalState) {
            return InputReader.readItemList(o, false);
        } else {
            return InputReader.readStringListList(o);
        }
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-rewardtypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        if (!ClassManager.manager.getSettings().getInventoryFullDropItems()) {
            List<ItemStack> items = (List<ItemStack>) reward;
            if (!ClassManager.manager.getItemStackChecker().hasFreeSpace(p, items)) {
                if (messageIfNoSuccess) {
                    ClassManager.manager.getMessageHandler()
                            .sendMessage("Main.InventoryFull", p, null, p, buy.getShop(), null, buy);
                }
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<ItemStack> items = (List<ItemStack>) reward;

        for (ItemStack i : items) {
            ClassManager.manager.getItemStackCreator().giveItem(p, buy, i, i.getAmount(), true);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<ItemStack> items = (List<ItemStack>) reward;
        String itemsFormatted = ClassManager.manager.getItemStackTranslator().getFriendlyText(p, items);
        return ClassManager.manager.getMessageHandler().get("Display.Item").replace("%items%", itemsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"item", "items"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean allowAsync() {
        return false;
    }

}
