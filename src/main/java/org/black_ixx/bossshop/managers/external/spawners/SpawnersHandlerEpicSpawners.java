package org.black_ixx.bossshop.managers.external.spawners;

import com.songoda.epicspawners.EpicSpawners;
import org.bukkit.inventory.ItemStack;

public class SpawnersHandlerEpicSpawners implements ISpawnerHandler {

    private final EpicSpawners util;


    public SpawnersHandlerEpicSpawners() {
        util = EpicSpawners.getInstance();
    }

    public ItemStack transformSpawner(ItemStack i, String entityType) {
        return util.getSpawnerManager().getSpawnerData(entityType).getFirstTier().toItemStack();
    }

    public String readSpawner(ItemStack i) {
        return util.getSpawnerManager().getSpawnerTier(i).getIdentifyingName();
    }
}
