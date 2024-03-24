package com.promcteam.genesis.managers.item;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ItemDataPartMobEgg extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (ClassManager.manager.getSpawnEggHandler() == null) {
            ClassManager.manager.getBugFinder()
                    .warn("Unable to work with ItemData of type " + createNames()[0]
                            + ": Requires the plugin SilkSpawners, which was not found.");
            return item;
        }

        ItemStack egg = ClassManager.manager.getSpawnEggHandler().transformEgg(item, argument);
        if (egg == null) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid mob type ('" + usedName + "').");
            return item;
        }

        return egg;
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
        return new String[]{"monsteregg", "mobegg", "spawnegg"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getType().name().endsWith("SPAWN_EGG")) {
            if (ClassManager.manager.getSpawnEggHandler() != null) {
                output.add("monsteregg:" + ClassManager.manager.getSpawnEggHandler().readEgg(i));
            } else {
                // no custom output needed: Knowing the material type is enough
            }
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        if (!shopItem.getType().equals(playerItem.getType())) {
            return false;
        }

        if (ClassManager.manager.getSpawnEggHandler()
                != null) { //just necessary for custom mob types: General ones are determined by material name
            String mobs = ClassManager.manager.getSpawnEggHandler().readEgg(shopItem);
            String mobp = ClassManager.manager.getSpawnEggHandler().readEgg(playerItem);
            return mobs.equalsIgnoreCase(mobp);
        }
        return true;
    }

}
