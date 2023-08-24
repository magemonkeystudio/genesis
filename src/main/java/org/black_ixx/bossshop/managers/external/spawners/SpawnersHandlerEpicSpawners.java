package org.black_ixx.bossshop.managers.external.spawners;

import com.craftaro.epicspawners.api.EpicSpawnersApi;
import org.bukkit.inventory.ItemStack;

public class SpawnersHandlerEpicSpawners implements ISpawnerHandler {

    public ItemStack transformSpawner(ItemStack i, String entityType) {
        return EpicSpawnersApi.getSpawnerManager().getSpawnerData(entityType).getFirstTier().toItemStack();
    }

    public String readSpawner(ItemStack i) {
        return EpicSpawnersApi.getSpawnerManager().getSpawnerTier(i).getIdentifyingName();
    }
}
