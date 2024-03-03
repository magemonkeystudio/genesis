package org.black_ixx.bossshop.managers.serverpinging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.black_ixx.bossshop.core.BSBuy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConnectedBuyItem {
    private List<String> originalLore;
    private String originalName;
    private BSBuy  shopItem;

    public ConnectedBuyItem(BSBuy originalBuy, ItemStack menuItem) {
        this.shopItem = originalBuy;
        if (menuItem != null) {
            ItemMeta meta = menuItem.getItemMeta();
            if (meta != null) {
                this.originalLore = meta.getLore();
                this.originalName = meta.getDisplayName();
            }
        }
    }
}
