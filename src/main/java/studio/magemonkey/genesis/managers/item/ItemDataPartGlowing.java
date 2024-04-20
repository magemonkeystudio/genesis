package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.misc.InputReader;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataPartGlowing extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        boolean b = false;
        if (argument != null) {
            b = InputReader.getBoolean(argument, false);
        }
        if (b) {
            item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        ItemMeta shop   = shopItem.getItemMeta();
        ItemMeta player = playerItem.getItemMeta();
        return (shop.hasEnchant(Enchantment.OXYGEN) && shop.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) == (
                player.hasEnchant(Enchantment.OXYGEN) && player.hasItemFlag(ItemFlag.HIDE_ENCHANTS));
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        ItemMeta meta = i.getItemMeta();
        if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) & meta.hasEnchant(Enchantment.OXYGEN)) {
            output.add("glowing:true");
        }
        return output;
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
        return new String[]{"glowing"};
    }
}
