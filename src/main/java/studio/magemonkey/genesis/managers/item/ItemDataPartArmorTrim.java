package studio.magemonkey.genesis.managers.item;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.List;

public class ItemDataPartArmorTrim extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 2) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It has to look like this: '<trim-pattern>#<trim-material>'.");
            return item;
        }

        TrimPattern trimPattern = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(parts[0]));
        if (trimPattern == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + parts[0] + "' is not a valid '" + usedName + "' pattern!");
            return item;
        }
        TrimMaterial trimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(parts[1]));
        if (trimMaterial == null) {
            trimMaterial = TrimMaterial.IRON;
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + parts[1] + "' is not a valid '" + usedName + "' material!");
        }
        if (item.getItemMeta() instanceof ArmorMeta) {
            ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
            ArmorMeta itemMeta  = (ArmorMeta) item.getItemMeta();
            itemMeta.setTrim(armorTrim);
            item.setItemMeta(itemMeta);
            return item;
        }
        ClassManager.manager.getBugFinder()
                .severe("Mistake in Config: Unable to add '" + usedName + "' to '" + item.getType().name() + "'!");
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.getItemMeta() instanceof ArmorMeta) {
            if (!(playerItem.getItemMeta() instanceof ArmorMeta)) {
                return false;
            }
            ArmorMeta shopMeta = (ArmorMeta) shopItem.getItemMeta();
            ArmorMeta itemMeta = (ArmorMeta) playerItem.getItemMeta();
            if (shopMeta.hasTrim()) {
                if (!itemMeta.hasTrim()) {
                    return false;
                }
                ArmorTrim shopTrim = shopMeta.getTrim();
                ArmorTrim itemTrim = itemMeta.getTrim();
                return shopTrim.getPattern() == itemTrim.getPattern()
                        && shopTrim.getMaterial() == itemTrim.getMaterial();
            }
        }
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof ArmorMeta) {
            ArmorMeta armorMeta = (ArmorMeta) i.getItemMeta();
            if (armorMeta.getTrim() != null) {
                String pattern  = armorMeta.getTrim().getPattern().getKey().getKey();
                String material = armorMeta.getTrim().getMaterial().getKey().getKey();
                output.add("armortrim:" + pattern + "#" + material);
            }
        }
        return output;
    }

    @Override
    public int getPriority() {
        return PRIORITY_LATE;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"armortrim", "trim"};
    }
}
