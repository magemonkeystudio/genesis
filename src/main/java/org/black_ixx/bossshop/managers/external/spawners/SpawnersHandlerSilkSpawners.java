package org.black_ixx.bossshop.managers.external.spawners;

import de.dustplanet.util.SilkUtil;
import org.bukkit.inventory.ItemStack;


public class SpawnersHandlerSilkSpawners implements ISpawnerHandler, ISpawnEggHandler {

    private final SilkUtil util;


    public SpawnersHandlerSilkSpawners() {
        util = SilkUtil.hookIntoSilkSpanwers();
    }

    public ItemStack transformSpawner(ItemStack i, String entityName) {
        return util.newSpawnerItem(entityName, null, i.getAmount(), false);
    }

    public ItemStack transformEgg(ItemStack i, String entityName) {
        return util.newEggItem(entityName, i.getAmount(), null);
    }


    public String readSpawner(ItemStack i) {
        return util.getCreatureName(util.getStoredSpawnerItemEntityID(i));
    }

    public String readEgg(ItemStack i) {
        return util.getCreatureName(util.getStoredEggEntityID(i));
    }
}
