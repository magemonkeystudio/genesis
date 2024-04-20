package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataPartUnbreaking extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        boolean b = true;
        if (argument != null) {
            b = InputReader.getBoolean(argument, true);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(b);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"unbreaking", "unbreakable"};
    }

    @Override
    public boolean needsArgument() {
        return false;
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        ItemMeta meta = i.getItemMeta();
        if (meta.isUnbreakable()) {
            output.add("unbreakable:true");
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return true; //Does not matter
    }

}
