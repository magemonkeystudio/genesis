package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class ItemDataPartPotion extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        String[] parts = argument.split("#");
        if (parts.length != 3) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It has to look like this: '<potion name/id>#<extended? true/false><upgraded? true/false>'. For example 'potion:POISON#true#true'.");
            return item;
        }

        if (!(item.getItemMeta() instanceof PotionMeta)) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: You can not make potions out of an item with material '"
                            + item.getType().name() + "'! Following line is invalid: '" + usedName + ":" + argument
                            + "'.");
            return item;
        }

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        String  potiontype = parts[0].trim();
        boolean extended   = InputReader.getBoolean(parts[1].trim(), false);
        boolean upgraded   = InputReader.getBoolean(parts[2].trim(), false);

        PotionType type = null;
        for (PotionType t : PotionType.values()) {
            if (potiontype.equalsIgnoreCase(t.name())) {
                type = t;
                break;
            }
        }

        if (type == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. The name of the potion is not known.");
            return item;
        }

        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        item.setItemMeta(meta);

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
        return new String[]{"potion"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getItemMeta() instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) i.getItemMeta();
            if (meta.getBasePotionData() != null) {
                output.add("potion:" + meta.getBasePotionData().getType().name() + "#" + meta.getBasePotionData()
                        .isExtended() + "#" + meta.getBasePotionData().isUpgraded());
            }
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (shopItem.getItemMeta() instanceof PotionMeta) {


            if (!(playerItem.getItemMeta() instanceof PotionMeta)) {
                return false;
            }

            PotionMeta ms = (PotionMeta) shopItem.getItemMeta();
            PotionMeta mp = (PotionMeta) playerItem.getItemMeta();

            if (ms.getBasePotionData().getType() == PotionType.WATER
                    || ms.getBasePotionData().getType() == PotionType.UNCRAFTABLE) {
                return true;
            }

            if (ms.getBasePotionData().getType() != mp.getBasePotionData().getType()) {
                return false;
            }

            if (ms.getBasePotionData().isExtended() & !mp.getBasePotionData().isExtended()) {
                return false;
            }
            return !(ms.getBasePotionData().isUpgraded() & !mp.getBasePotionData().isUpgraded());

        }
        return true;
    }

}
