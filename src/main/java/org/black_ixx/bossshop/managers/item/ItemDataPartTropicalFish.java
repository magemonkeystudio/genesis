package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

import java.util.List;

public class ItemDataPartTropicalFish extends ItemDataPart{
    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if(!item.getType().equals(Material.TROPICAL_FISH_BUCKET)){
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The material must be TROPICAL_FISH_BUCKET.");
            return item;
        }
        TropicalFishBucketMeta meta = (TropicalFishBucketMeta) item.getItemMeta();
        String[] parts = argument.split("#");
        DyeColor color = DyeColor.valueOf(parts[0].toUpperCase());
        TropicalFish.Pattern p = TropicalFish.Pattern.valueOf(parts[1].toUpperCase());
        meta.setPatternColor(color);
        meta.setPattern(p);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        TropicalFishBucketMeta meta = (TropicalFishBucketMeta) i.getItemMeta();
        TropicalFish.Pattern p = meta.getPattern();
        DyeColor color = meta.getPatternColor();
        output.add("tropicalfish:"+color.name()+"#"+p.name());
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
        return new String[]{"tropicalfish"};
    }
}
