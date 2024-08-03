package studio.magemonkey.genesis.managers.serverpinging;

import studio.magemonkey.genesis.core.GenesisBuy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConnectedBuyItem {
    private List<String> originalLore;
    private String       originalName;
    private GenesisBuy   shopItem;

    public ConnectedBuyItem(GenesisBuy originalBuy, ItemStack menuItem) {
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
