package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;

import java.util.List;

public class ItemDataPartAxolotl extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!item.getType().equals(Material.AXOLOTL_BUCKET)) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. The material must be AXOLOTL_BUCKET.");
            return item;
        }
        AxolotlBucketMeta meta = (AxolotlBucketMeta) item.getItemMeta();
        meta.setVariant(Axolotl.Variant.valueOf(argument.toUpperCase()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        AxolotlBucketMeta meta    = (AxolotlBucketMeta) i.getItemMeta();
        Axolotl.Variant   variant = meta.getVariant();
        output.add("axolotl:" + variant.name());
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
