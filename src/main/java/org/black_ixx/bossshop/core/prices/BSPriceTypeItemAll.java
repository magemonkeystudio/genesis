package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.rewards.BSRewardTypeNumber;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BSPriceTypeItemAll extends BSPriceType {


    public Object createObject(Object o, boolean forceFinalState) {
        if (forceFinalState) {
            ItemStack i = InputReader.readItem(o, false);
            i.setAmount(1);
            return i;
        } else {
            return InputReader.readStringList(o);
        }
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The price object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-pricetypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        ItemStack item         = (ItemStack) price;
        int       itemsAmount = ClassManager.manager.getItemStackChecker().getAmountOfSameItems(p, item, buy);

        if (itemsAmount < 1) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Item", p);
            }
            return false;
        }

        return true;
    }

    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        ItemStack item         = (ItemStack) price;
        int       itemsAmount = ClassManager.manager.getItemStackChecker().getAmountOfSameItems(p, item, buy);

        item = item.clone();
        item.setAmount(itemsAmount);
        ClassManager.manager.getItemStackChecker().takeItem(item, p, buy);

        BSRewardTypeNumber rewardtype = (BSRewardTypeNumber) buy.getRewardType(clickType);
        rewardtype.giveReward(p, buy, buy.getReward(clickType), clickType, itemsAmount);
        return null;
    }

    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        ItemStack item      = (ItemStack) price;
        String    itemName = ClassManager.manager.getItemStackTranslator().readMaterial(p, item);
        return ClassManager.manager.getMessageHandler().get("Display.ItemAll").replace("%item%", itemName);
    }


    @Override
    public String[] createNames() {
        return new String[]{"itemall", "sellall"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean overridesReward() {
        return true;
    }

}
