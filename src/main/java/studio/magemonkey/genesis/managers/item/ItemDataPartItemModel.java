package studio.magemonkey.genesis.managers.item;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;

import java.util.List;
import java.util.Objects;

public class ItemDataPartItemModel extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        String itemModel = InputReader.readString(argument, true);
        if (itemModel == null || itemModel.isBlank()) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It needs to be a string.");
            return item;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.setItemModel(NamespacedKey.fromString(itemModel));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.hasItemMeta() && playerItem.hasItemMeta()) {
            return Objects.equals(shopItem.getItemMeta().getItemModel(), playerItem.getItemMeta().getItemModel());
        }
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.hasItemMeta() && i.getItemMeta().hasItemModel()) {
            String d = i.getItemMeta().getItemModel().getKey();
            output.add("ItemModel:" + d);
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
        return new String[]{"ItemModel"};
    }
}
