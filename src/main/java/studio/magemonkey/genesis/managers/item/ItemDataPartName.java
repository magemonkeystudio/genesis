package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataPartName extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(argument);
        item.setItemMeta(meta);
        return item;
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
        return new String[]{"name", "text", "title"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        ItemMeta meta = i.getItemMeta();
        if (meta.hasDisplayName()) {
            output.add("name:" + meta.getDisplayName().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        ItemMeta ms = shopItem.getItemMeta();
        ItemMeta mp = playerItem.getItemMeta();
        if (ms.hasDisplayName()) {
            if (!mp.hasDisplayName()) {
                return false;
            }
            String shopItemName = ms.getDisplayName();
            if (ClassManager.manager.getStringManager()
                    .checkStringForFeatures(buy == null ? null : buy.getShop(),
                            buy,
                            buy == null ? null : buy.getItem(),
                            shopItemName)) {
                shopItemName = ClassManager.manager.getStringManager()
                        .transform(shopItemName, buy, buy == null ? null : buy.getShop(), null, p);
            }
            return shopItemName.equalsIgnoreCase(mp.getDisplayName());
        }
        return true;
    }


}
