package studio.magemonkey.genesis.managers.item;

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.settings.Settings;

import java.util.List;
import java.util.Map;


public class ItemDataPartEnchantment extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 2) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It has to look like this: '<enchantment name/id>#<level>'. For example 'DAMAGE_ALL#5'.");
            return item;
        }

        String enchantment = parts[0].trim();
        int    level       = InputReader.getInt(parts[1].trim(), -1);

        if (level == -1) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. The level of the enchantment is invalid.");
            return item;
        }

        Enchantment e = InputReader.readEnchantment(enchantment);

        if (e == null && Bukkit.getPluginManager().isPluginEnabled("TokenEnchant")) {
            TokenEnchantAPI te = TokenEnchantAPI.getInstance();
            item = te.enchant(null, item, enchantment, level, true, 0, false);
            return item;
        }

        if (e == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + enchantment + "' is not a valid '" + usedName
                            + "'. The name/id of the enchantment is not known.");
            return item;
        }

        if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.addStoredEnchant(e, level, true);
            item.setItemMeta(meta);
        } else {
            item.addUnsafeEnchantment(e, level);
        }

        return item;
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
        return new String[]{"enchantment", "enchantmentid"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getEnchantments() != null) {
            Map<Enchantment, Integer> enchantments = i.getEnchantments();
            for (Enchantment enchantment : enchantments.keySet()) {
                output.add("enchantment:" + enchantment.getKey().getKey() + "#" + enchantments.get(enchantment));
            }
        }
        if (i.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta    meta         = (EnchantmentStorageMeta) i.getItemMeta();
            Map<Enchantment, Integer> enchantments = meta.getStoredEnchants();
            for (Enchantment enchantment : enchantments.keySet()) {
                output.add("enchantment:" + enchantment.getKey().getKey() + "#" + enchantments.get(enchantment));
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        //normal enchantments
        if (!containsEnchantments(shopItem.getEnchantments(), playerItem.getEnchantments(), buy)) {
            return false;
        }


        //enchantmentbook enchantments
        if (shopItem.getItemMeta() instanceof EnchantmentStorageMeta) {

            if (!(playerItem.getItemMeta() instanceof EnchantmentStorageMeta)) {
                return false;
            }

            EnchantmentStorageMeta ms = (EnchantmentStorageMeta) shopItem.getItemMeta();
            EnchantmentStorageMeta mp = (EnchantmentStorageMeta) playerItem.getItemMeta();
            return containsEnchantments(ms.getStoredEnchants(), mp.getStoredEnchants(), buy);

        }


        return true;
    }


    private boolean containsEnchantments(Map<Enchantment, Integer> es, Map<Enchantment, Integer> ep, GenesisBuy buy) {
        for (Enchantment e : es.keySet()) {
            if (!ep.containsKey(e)) {
                return false;
            }
            if (ep.get(e) < es.get(e)) {
                return false;
            }
        }
        if (!ClassManager.manager.getSettings().getPropertyBoolean(Settings.ALLOW_SELLING_GREATER_ENCHANTS, buy)) {
            for (Enchantment e : ep.keySet()) {
                if (!es.containsKey(e)) {
                    return false;
                }
                if (es.get(e) < ep.get(e)) {
                    return false;
                }
            }
        }

        return true;
    }


}
