package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;


public class ItemDataPartWrittenBookInformation extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!(item.getItemMeta() instanceof BookMeta)) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: You can not add book information to an item with material '"
                            + item.getType().name() + "'! Following line is invalid: '" + usedName + ":" + argument
                            + "'.");
            return item;
        }

        String[] parts = argument.split("#", 2);
        if (parts.length != 2) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: Following line is invalid: '" + usedName + ":" + argument
                            + "'. It should look like this: 'book:<title>#<author>'.");
            return item;
        }

        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setTitle(ClassManager.manager.getStringManager().transform(parts[0]));
        meta.setAuthor(ClassManager.manager.getStringManager().transform(parts[1]));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_NORMAL;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"book"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if ((i.getItemMeta() instanceof BookMeta)) {
            BookMeta meta = (BookMeta) i.getItemMeta();
            if (meta.hasAuthor() || meta.hasTitle()) {
                output.add("book:" + meta.getTitle().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&") + "#"
                        + meta.getAuthor().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.getItemMeta() instanceof BookMeta) {
            if (!(playerItem.getItemMeta() instanceof BookMeta)) {
                return false;
            }

            BookMeta ms = (BookMeta) shopItem.getItemMeta();
            BookMeta mp = (BookMeta) playerItem.getItemMeta();

            if (ms.hasAuthor()) {
                if (!mp.hasAuthor()) {
                    return false;
                }

                if (!mp.getAuthor().equals(ms.getAuthor())) {
                    return false;
                }

            }

            if (ms.hasTitle()) {
                if (!mp.hasTitle()) {
                    return false;
                }

                return mp.getTitle().equals(ms.getTitle());

            }

        }
        return true;
    }


}
