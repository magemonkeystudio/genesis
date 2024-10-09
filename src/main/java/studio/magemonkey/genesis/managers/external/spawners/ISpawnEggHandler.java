package studio.magemonkey.genesis.managers.external.spawners;

import org.bukkit.inventory.ItemStack;

public interface ISpawnEggHandler {


    ItemStack transformEgg(ItemStack i, String entityName);

    String readEgg(ItemStack i);
}
