package org.black_ixx.bossshop.managers.item;

import com.francobm.magicosmetics.api.MagicAPI;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.ModuleItem;

import java.util.List;
import java.util.Objects;

public class ItemDataPartMaterial extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        short durability = 0;
        Material m;

        if (argument.contains(":")) { //Can be used for durability
            String[] parts = argument.split(":");
            if (parts.length > 1) {
                durability = (short) InputReader.getInt(parts[1].trim(), 0);
            }
            argument = parts[0].trim();
        }

        if (argument.contains("/")) {
            String[] parts = argument.split("/");
            if (parts.length == 2) {
                String plugin = parts[0];
                String id = parts[1];
                switch (plugin){
                    case "ItemsAdder":
                    case "itemsadder":
                        ItemStack i = CustomStack.getInstance(parts[1]).getItemStack();
                        if (i != null){
                            return i;
                        }else {
                            ClassManager.manager.getBugFinder().warn("Mistake in Config: '"+ id + "' is not a valid ItemsAdder item.");
                            return item;
                        }
                    case "MythicMobs":
                    case "mythicmobs":
                        ItemStack mi = MythicBukkit.inst().getItemManager().getItemStack(id);
                        if (mi != null){
                            return mi;
                        }else {
                            ClassManager.manager.getBugFinder().warn("Mistake in Config: '"+ id + "' is not a valid MythicMobs item.");
                            return item;
                        }
                    case "MagicCosmetics":
                    case "magiccosmetics":
                        ItemStack ci = MagicAPI.getCosmeticItem(id);
                        if (ci != null){
                            return ci;
                        }else {
                            ClassManager.manager.getBugFinder().warn("Mistake in Config: '"+ id + "' is not a valid MagicCosmetics item.");
                            return item;
                        }
                    case "ProRPGItems":
                    case "prorpgitems":
                        ModuleItem pri = Objects.requireNonNull(QuantumRPG.instance.getModuleCache().getTierManager()).getItemById(id);
                        if (pri != null){
                            return pri.create();
                        }else {
                            ClassManager.manager.getBugFinder().warn("Mistake in Config: '"+ id + "' is not a valid ProRPGItems item.");
                            return item;
                        }
                    default:
                        ClassManager.manager.getBugFinder().warn("Mistake in Config: '"+ plugin + "' is not a valid plugin, or is not supported.");
                        return item;
                }
            }
        }


        m = InputReader.readMaterial(argument);

        if (m == null) {
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. Unable to find a fitting material.");
            return item;
        }

        item.setType(m);
        Damageable d = (Damageable) item.getItemMeta();
        if (d != null) d.setDamage(durability);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_MOST_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"type", "id", "material"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        output.add("type:" + i.getType().name());
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return shop_item.getType() == player_item.getType();
    }
}
