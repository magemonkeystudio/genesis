package studio.magemonkey.genesis.managers.external.spawners;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class SpawnerHandlerVanilla implements ISpawnerHandler {

    @Override
    public ItemStack transformSpawner(ItemStack monsterSpawner, String entityName) {
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(entityName.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        BlockStateMeta spawnerMeta = (BlockStateMeta) monsterSpawner.getItemMeta();
        if (spawnerMeta == null) return monsterSpawner;

        CreatureSpawner creatureSpawner = (CreatureSpawner) spawnerMeta.getBlockState();
        creatureSpawner.setSpawnedType(entityType);
        spawnerMeta.setBlockState(creatureSpawner);
        spawnerMeta.setDisplayName(entityName + " &7Spawner");
        monsterSpawner.setItemMeta(spawnerMeta);
        return monsterSpawner;
    }

    @Override
    public String readSpawner(ItemStack monsterSpawner) {
        BlockStateMeta spawnerMeta = (BlockStateMeta) monsterSpawner.getItemMeta();
        if (spawnerMeta == null) return "";

        CreatureSpawner creatureSpawner = (CreatureSpawner) spawnerMeta.getBlockState();
        if (creatureSpawner.getSpawnedType() == null) return "";

        return creatureSpawner.getSpawnedType().toString();
    }
}
