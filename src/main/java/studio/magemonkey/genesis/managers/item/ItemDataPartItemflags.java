package studio.magemonkey.genesis.managers.item;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.magemonkey.codex.core.Version;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;

import java.util.List;

public class ItemDataPartItemflags extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        ItemMeta meta = item.getItemMeta();

        if (argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("all")) {
            meta.addItemFlags(ItemFlag.values());
        } else {
            String[] flags = argument.split("#");

            for (String flag : flags) {
                flag = flag.toUpperCase().replaceAll(" ", "_");
                if (!flag.startsWith("HIDE_")) {
                    flag = "HIDE_" + flag;
                }
                try {
                    ItemFlag itemflag = ItemFlag.valueOf(flag.toUpperCase());

                    if (itemflag == ItemFlag.HIDE_ATTRIBUTES && Version.CURRENT.isAtLeast(Version.V1_20_R4)) {
                        // We have to add a default attribute in order to hide attributes
                        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE,
                                new AttributeModifier(Attribute.ATTACK_DAMAGE.getKey().getKey(),
                                        0,
                                        AttributeModifier.Operation.ADD_NUMBER));
                    }

                    meta.addItemFlags(itemflag);
                } catch (Exception e) {
                    ClassManager.manager.getBugFinder()
                            .warn("Mistake in Config: '" + flag + "' is not a valid '" + usedName + "'.");
                }
            }

        }

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
        return new String[]{"itemflag", "hideflag", "flag", "itemflags", "hideflags", "flags"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        ItemMeta meta = i.getItemMeta();
        if (meta.getItemFlags() != null) {
            for (ItemFlag flag : meta.getItemFlags()) {
                output.add("itemflag:" + flag.name());
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return true; // Does not matter
    }

}
