package studio.magemonkey.genesis.managers.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.List;

public class ItemDataPartKnowledgeBook extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!item.getType().equals(Material.KNOWLEDGE_BOOK)) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. The material must be KNOWLEDGE_BOOK.");
            return item;
        }
        KnowledgeBookMeta meta = (KnowledgeBookMeta) item.getItemMeta();
        NamespacedKey     key  = NamespacedKey.fromString(argument);
        meta.addRecipe(key);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        //TODO: implement comparison of two knowledge books.
        return shopItem.getType() != Material.KNOWLEDGE_BOOK && playerItem.getType() != Material.KNOWLEDGE_BOOK;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        KnowledgeBookMeta meta = (KnowledgeBookMeta) i.getItemMeta();
        if (meta.hasRecipes()) {
            for (NamespacedKey key : meta.getRecipes()) {
                output.add("knowledgebook:" + key.getNamespace() + ":" + key.getKey());
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
