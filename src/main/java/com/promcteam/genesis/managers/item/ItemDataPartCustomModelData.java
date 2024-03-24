package com.promcteam.genesis.managers.item;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataPartCustomModelData extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        int custommodeldata = InputReader.getInt(argument, -1);
        if (custommodeldata == -1) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It needs to be a number like '1', '12' or '64'.");
            return item;
        }
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(custommodeldata);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.hasItemMeta() && playerItem.hasItemMeta()) {
            return shopItem.getItemMeta().getCustomModelData() == playerItem.getItemMeta().getCustomModelData();
        }
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.hasItemMeta()) {
            int d = i.getItemMeta().getCustomModelData();
            if (d != -1) {
                output.add("CustomModelData:" + d);
            }
        }
        return output;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"CustomModelData"};
    }
}
