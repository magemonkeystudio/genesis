package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;

import java.util.List;

public class ItemDataPartAxolotl extends ItemDataPart{
    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if(!item.getType().equals(Material.AXOLOTL_BUCKET)){
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The material must be AXOLOTL_BUCKET.");
            return item;
        }
        AxolotlBucketMeta meta = (AxolotlBucketMeta) item.getItemMeta();
        meta.setVariant(Axolotl.Variant.valueOf(argument.toUpperCase()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        AxolotlBucketMeta meta = (AxolotlBucketMeta) i.getItemMeta();
        Axolotl.Variant variant = meta.getVariant();
        output.add("axolotl:"+variant.name());
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
        return new String[]{"axolotl"};
    }
}
