package com.promcteam.genesis.managers.item;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;


public class ItemDataPartWrittenBookPage extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!(item.getItemMeta() instanceof BookMeta)) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: You can not add book text to an item with material '" + item.getType()
                            .name() + "'! Following line is invalid: '" + usedName + ":" + argument + "'.");
            return item;
        }
        BookMeta     meta  = (BookMeta) item.getItemMeta();
        List<String> pages = new ArrayList<>();
        if (meta.getPages() != null) {
            for (String page : meta.getPages()) {
                pages.add(page);
            }
        }

        String[] parts = argument.split("#", 2);
        int      page  = InputReader.getInt(parts[0], -1);
        if (parts.length != 2 || page == -1) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: Invalid: line: '" + usedName + ":" + argument
                            + "'. It should look like following: 'bookpage:<page>#<text>'.");
            return item;
        }

        String text = "";

        //If already having that page, get existing content
        if (pages.size() >= page) {
            text = pages.get(page - 1);

            //Else create pages up to the required one
        } else {
            while (pages.size() < page) {
                pages.add("");
            }
        }

        //Having text already? New line!
        if (!text.isEmpty()) {
            text += "\n";
        }

        //Adding and setting text
        text += ClassManager.manager.getStringManager().transform(parts[1]);
        pages.set(page - 1, text);

        meta.setPages(pages);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_LATE;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"bookpage"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if ((i.getItemMeta() instanceof BookMeta)) {
            BookMeta meta = (BookMeta) i.getItemMeta();
            if (meta.hasPages()) {
                int a = 1;
                for (String page : meta.getPages()) {
                    for (String line : page.split("\n")) {
                        output.add("bookpage:" + a + "#" + line.replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&"));
                    }
                    a++;
                }
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

            if (ms.hasPages()) {
                if (!mp.hasPages()) {
                    return false;
                }


                if (ms.getPages().size() != mp.getPages().size()) {
                    return false;
                }
                for (int i = 0; i < ms.getPages().size(); i++) {
                    if (!mp.getPages().get(i).equals(ms.getPages().get(i))) {
                        return false;
                    }
                }

            }
        }
        return true;
    }


}
