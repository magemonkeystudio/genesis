package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class ItemDataPartBanner extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9")) {  //TODO: ADD Documentation and test this feature out
            if (item.getType() != Material.LEGACY_BANNER) {
                ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'.");
                return item;
            }
            String[] parts = argument.split("#", 2);
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            DyeColor basecolor = DyeColor.valueOf(parts[0]);
            meta.addPattern(new Pattern(basecolor, PatternType.valueOf(parts[1])));
            item.setItemMeta(meta);
            return item;
        }else {
            if (!Tag.BANNERS.isTagged(item.getType())) {
                ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'.");
                return item;
            }
            BannerMeta meta = (BannerMeta) item.getItemMeta();
            String[] parts = argument.split("#", 2);
            DyeColor color = DyeColor.valueOf(parts[0]);
            Pattern pattern = new Pattern(color, PatternType.valueOf(parts[1].toUpperCase()));
            meta.addPattern(pattern);
            item.setItemMeta(meta);
        }
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
        return new String[]{"banner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        BannerMeta meta = (BannerMeta) i.getItemMeta();
        if (!meta.getPatterns().isEmpty()) {
            for (Pattern p : meta.getPatterns()){
                output.add("banner:"+p.getColor().name()+"#"+p.getPattern().name());
            }
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return true; //Banner color does not matter
    }

}
