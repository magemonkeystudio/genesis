package org.black_ixx.bossshop.core.prices;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BSPriceTypeItem extends BSPriceType {


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
                        + "! The price object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-rewardtypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }


    @SuppressWarnings("unchecked")
    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        List<ItemStack> items = (List<ItemStack>) price;
        for (ItemStack i : items) {
            if (!ClassManager.manager.getItemStackChecker().inventoryContainsItem(p, i, buy)) {
                if (messageOnFailure) {
                    ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Item", p);
                }
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        List<ItemStack> items = (List<ItemStack>) price;
        for (ItemStack i : items) {
            ClassManager.manager.getItemStackChecker().takeItem(i, p, buy);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        List<ItemStack> items           = (List<ItemStack>) price;
        String          itemsFormatted = ClassManager.manager.getItemStackTranslator().getFriendlyText(p, items);
        return ClassManager.manager.getMessageHandler().get("Display.Item").replace("%items%", itemsFormatted);
    }


    @Override
    public String[] createNames() {
        return new String[]{"item", "items"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
