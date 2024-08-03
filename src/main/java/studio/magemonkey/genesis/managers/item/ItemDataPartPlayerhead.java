package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemDataPartPlayerhead extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            ClassManager.manager.getBugFinder()
                    .warn("Mistake in Config: Itemdata of type '" + usedName + "' with value '" + argument
                            + "' can not be added to an item with material '" + item.getType().name()
                            + "'. Don't worry I'll automatically transform the material into '"
                            + Material.PLAYER_HEAD.name() + ".");
            item.setType(Material.PLAYER_HEAD);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (ClassManager.manager.getStringManager().checkStringForFeatures(null, null, null, argument)) {
            NamespacedKey key = new NamespacedKey(ClassManager.manager.getPlugin(), "skullOwnerPlaceholder");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, argument); //argument = placeholder
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(argument);
            meta.setOwningPlayer(player);
        }

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"playerhead", "head", "owner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            if (i.getType() == Material.PLAYER_HEAD) {
                if (meta.hasOwner()) {
                    output.add("playerhead:" + meta.getOwner());
                }
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.getType() == Material.PLAYER_HEAD) {
            if (playerItem.getType() != Material.PLAYER_HEAD) {
                return false;
            }

            SkullMeta ms = (SkullMeta) shopItem.getItemMeta();
            SkullMeta mp = (SkullMeta) playerItem.getItemMeta();

            if (ms.hasOwner()) {

                if (!mp.hasOwner()) {
                    return false;
                }

                return ms.getOwningPlayer().getUniqueId().equals(mp.getOwningPlayer().getUniqueId());
            }
        }
        return true;
    }


}
