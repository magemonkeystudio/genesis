package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import java.util.List;

public class ItemDataPartKnowledgeBook extends ItemDataPart{
    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        if(!item.getType().equals(Material.KNOWLEDGE_BOOK)){
            ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + argument + "' is not a valid '" + used_name + "'. The material must be KNOWLEDGE_BOOK.");
            return item;
        }
        KnowledgeBookMeta meta = (KnowledgeBookMeta) item.getItemMeta();
        NamespacedKey key = NamespacedKey.fromString(argument);
        meta.addRecipe(key);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        return false;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        KnowledgeBookMeta meta = (KnowledgeBookMeta) i.getItemMeta();
        if(meta.hasRecipes()){
            for(NamespacedKey key:meta.getRecipes()){
                output.add("knowledgebook:"+key.getNamespace()+":"+key.getKey());
            }
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
        return new String[]{"knowledgebook"};
    }
}
