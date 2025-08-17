package studio.magemonkey.genesis.managers.external.spawners;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

/**
 * Vanilla implementation of {@code ISpawnerHandler} that uses the Bukkit API to
 * read and write the spawned {@link EntityType} of a spawner {@link ItemStack}.
 * <p>
 * This handler operates directly on {@link BlockStateMeta} and the embedded
 * {@link CreatureSpawner} block state.
 */
public class SpawnerHandlerVanilla implements ISpawnerHandler {

    /**
     * Transforms the provided spawner item to spawn the given entity type.
     *
     * <p>Behavior:
     * - Parses {@code entityName} as an {@link EntityType} (case-insensitive via uppercasing).
     * - If the name is invalid, logs the error and returns {@code null}.
     * - If the item lacks {@link BlockStateMeta}, returns the original item unchanged.
     * - Otherwise, updates the underlying {@link CreatureSpawner} to the parsed type,
     *   updates the meta, and sets a display name in the form "{entityName} &7Spawner".
     *
     * <p>Note: The provided {@link ItemStack} is mutated in place and also returned.
     *
     * @param monsterSpawner the spawner {@link ItemStack} expected to contain {@link BlockStateMeta}
     * @param entityName     the entity type name corresponding to an {@link EntityType} constant
     * @return the same {@link ItemStack} with updated meta; {@code null} if {@code entityName} is invalid
     */
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

    /**
     * Reads the spawned {@link EntityType} from the provided spawner item.
     *
     * <p>Behavior:
     * - If the item lacks {@link BlockStateMeta} or the spawner has no type, returns an empty string.
     * - Otherwise, returns the {@link EntityType} name via {@code toString()}.
     *
     * @param monsterSpawner the spawner {@link ItemStack} to inspect
     * @return the spawned entity type name, or an empty string if unavailable
     */
    @Override
    public String readSpawner(ItemStack monsterSpawner) {
        BlockStateMeta spawnerMeta = (BlockStateMeta) monsterSpawner.getItemMeta();
        if (spawnerMeta == null) return "";

        CreatureSpawner creatureSpawner = (CreatureSpawner) spawnerMeta.getBlockState();
        if (creatureSpawner.getSpawnedType() == null) return "";

        return creatureSpawner.getSpawnedType().toString();
    }
}